package com.oxysystem.general.scheduler.general;

import com.oxysystem.general.dto.general.apiAppSync.view.ApiAppSyncViewDTO;
import com.oxysystem.general.dto.grab.data.BatchUpdateMenuRequestDTO;
import com.oxysystem.general.dto.grab.data.SubmitOrderRequestDTO;
import com.oxysystem.general.enums.grab.Product;
import com.oxysystem.general.model.tenant.general.ApiApp;
import com.oxysystem.general.model.tenant.general.ApiAppSyncSetup;
import com.oxysystem.general.model.tenant.general.Location;
import com.oxysystem.general.service.general.ApiAppService;
import com.oxysystem.general.service.general.ApiAppSyncService;
import com.oxysystem.general.service.general.LocationService;
import com.oxysystem.general.service.grab.client.grabMart.GrabMartMenuSyncServiceImpl;
import com.oxysystem.general.service.grab.client.grabMart.GrabMartOAuthServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@ConditionalOnProperty(value = "schedule.grab-mart-menus-sync.enabled", havingValue = "true")
public class ApiAppSyncGrabMartScheduler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiAppSyncGrabMartScheduler.class);

    private final ApiAppService apiAppService;
    private final ApiAppSyncService apiAppSyncService;
    private final LocationService locationService;
    private final GrabMartMenuSyncServiceImpl grabMartMenuSyncService;
    private final ThreadPoolTaskExecutor grabMenuSyncExecutor;
    private final GrabMartOAuthServiceImpl grabMartOAuthService;

    // ===== Token cache (race-safe) =====
    private final Object tokenLock = new Object();
    private volatile String cachedToken;
    private volatile long cachedTokenExpiryMillis = 0L;
    private volatile Mono<String> inFlightTokenMono;

    // TTL constants
    private static final long DEFAULT_TOKEN_TTL_SECONDS = 3300; // 55 menit
    private static final long SAFETY_MARGIN_SECONDS = 300;      // 5 menit
    private static final long MIN_TTL_SECONDS = 60;             // 1 menit min

    // Prevent overlapping schedule runs
    private final AtomicBoolean running = new AtomicBoolean(false);

    public ApiAppSyncGrabMartScheduler(ApiAppService apiAppService,
                                       ApiAppSyncService apiAppSyncService,
                                       LocationService locationService,
                                       GrabMartMenuSyncServiceImpl grabMartMenuSyncService,
                                       @Qualifier("grabMenuSyncExecutor") ThreadPoolTaskExecutor grabMenuSyncExecutor,
                                       GrabMartOAuthServiceImpl grabMartOAuthService) {
        this.apiAppService = apiAppService;
        this.apiAppSyncService = apiAppSyncService;
        this.locationService = locationService;
        this.grabMartMenuSyncService = grabMartMenuSyncService;
        this.grabMenuSyncExecutor = grabMenuSyncExecutor;
        this.grabMartOAuthService = grabMartOAuthService;
    }

    @Scheduled(cron = "${schedule.grab-mart-menus-sync.cron}")
    public void grabBatchUpdateMenuSchedule() {
        if (!running.compareAndSet(false, true)) {
            LOGGER.warn("Previous grab menu sync is still running, skip this tick.");
            return;
        }

        Mono.fromCallable(() -> {
                    // 1) Ambil setup Grab
                    Optional<ApiApp> apiAppOpt = apiAppService.findApiAppByName(Product.GRAB_MART.name());
                    Optional<ApiAppSyncSetup> syncSetup = apiAppOpt.flatMap(apiApp ->
                            apiApp.getApiAppSyncSetups().stream()
                                    .filter(s -> s.getStatus() == 1 && "pos_item_master".equalsIgnoreCase(s.getTableName()))
                                    .findFirst()
                    );
                    if (!syncSetup.isPresent()) return Collections.<String, Object>emptyMap();

                    // 2) Ambil data yang belum tersinkron
                    List<ApiAppSyncViewDTO> apiAppSyncViewDTOS =
                            apiAppSyncService.findApiAppSyncGrabNotSyncByTableName("pos_item_master");
                    if (apiAppSyncViewDTOS == null || apiAppSyncViewDTOS.isEmpty()) {
                        return Collections.<String, Object>emptyMap();
                    }

                    // 3) Siapkan peta location
                    Set<Long> locationIds = apiAppSyncViewDTOS.stream()
                            .map(ApiAppSyncViewDTO::getLocationId)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toSet());
                    List<Location> locations = locationService.findAllByIDs(new ArrayList<>(locationIds));
                    Map<Long, Location> locationMap = locations.stream()
                            .collect(Collectors.toMap(Location::getLocationId, Function.identity()));

                    // 4) Group berdasar grabMerchantId
                    Map<String, List<ApiAppSyncViewDTO>> groupByGrabMerchantId = apiAppSyncViewDTOS.stream()
                            .filter(a -> a.getGrabMerchantId() != null && !a.getGrabMerchantId().isEmpty())
                            .collect(Collectors.groupingBy(ApiAppSyncViewDTO::getGrabMerchantId));

                    // 5) Peta merchant -> daftar apiSyncId untuk update status nanti
                    Map<String, List<Long>> merchantToApiSyncIds = apiAppSyncViewDTOS.stream()
                            .filter(a -> a.getGrabMerchantId() != null && !a.getGrabMerchantId().isEmpty())
                            .collect(Collectors.groupingBy(ApiAppSyncViewDTO::getGrabMerchantId,
                                    Collectors.mapping(ApiAppSyncViewDTO::getApiSyncId, Collectors.toList())));

                    // 6) Build request per merchant
                    List<BatchUpdateMenuRequestDTO> requests = groupByGrabMerchantId.entrySet().stream()
                            .map(entry -> {
                                String grabMerchantId = entry.getKey();
                                List<ApiAppSyncViewDTO> list = entry.getValue();
                                if (list.isEmpty()) return null;

                                Long locationId = list.get(0).getLocationId();
                                Location location = locationMap.get(locationId);
                                if (location == null) return null;

                                List<SubmitOrderRequestDTO.Item> items = list.stream()
                                        .map(apiApp -> {
                                            SubmitOrderRequestDTO.Item item = new SubmitOrderRequestDTO.Item();
                                            item.setId(String.valueOf(apiApp.getOwnerId()));
                                            return item;
                                        })
                                        .collect(Collectors.toList());

                                return grabMartMenuSyncService.createBatchUpdate(items, grabMerchantId, location);
                            })
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());

                    Map<String, Object> result = new HashMap<>();
                    result.put("requests", requests);
                    result.put("merchantToApiSyncIds", merchantToApiSyncIds);
                    return result;
                })
                .subscribeOn(Schedulers.boundedElastic())
                .flatMapMany(map -> {
                    if (map == null) return Flux.empty();

                    @SuppressWarnings("unchecked")
                    List<BatchUpdateMenuRequestDTO> requests =
                            (List<BatchUpdateMenuRequestDTO>) map.get("requests");
                    @SuppressWarnings("unchecked")
                    Map<String, List<Long>> merchantToApiSyncIds =
                            (Map<String, List<Long>>) map.get("merchantToApiSyncIds");

                    if (requests == null || requests.isEmpty()) {
                        LOGGER.info("No Grab menu sync requests to process");
                        return Flux.empty();
                    }

                    Mono<String> tokenMono = getGrabTokenMono()
                            .doOnError(e -> LOGGER.error("Failed to obtain Grab token: {}", e.getMessage()));

                    List<Long> apiSyncIdsSuccess = Collections.synchronizedList(new ArrayList<>());
                    List<Long> apiSyncIdsFailed = Collections.synchronizedList(new ArrayList<>());

                    // Sequential + jeda 1 detik
                    return tokenMono.flatMapMany(initialToken ->
                            Flux.fromIterable(requests)
                                    .concatMap(request ->
                                            Mono.delay(Duration.ofSeconds(1), Schedulers.parallel()) // delay pakai scheduler reactor
                                                    .then(Mono.defer(() ->
                                                            Mono.fromRunnable(() -> {}) // placeholder supaya publishOn tetap kepakai
                                                                    .publishOn(Schedulers.fromExecutor(grabMenuSyncExecutor)) // eksekusi reactive pakai executor custom
                                                                    .then(executeWithTokenRetry(
                                                                            request,
                                                                            initialToken,
                                                                            merchantToApiSyncIds,
                                                                            apiSyncIdsSuccess,
                                                                            apiSyncIdsFailed
                                                                    ))
                                                    ))
                                    )

                                    .then(Mono.fromRunnable(() -> {
                                        if (!apiSyncIdsSuccess.isEmpty()) {
                                            try {
                                                apiAppSyncService.updateApiAppSyncStatusByApiSyncIds(1, apiSyncIdsSuccess);
                                            } catch (Exception ex) {
                                                LOGGER.error("Failed to update success statuses: {}", ex.getMessage());
                                            }
                                        }
                                        if (!apiSyncIdsFailed.isEmpty()) {
                                            try {
                                                apiAppSyncService.updateApiAppSyncStatusByApiSyncIds(2, apiSyncIdsFailed);
                                            } catch (Exception ex) {
                                                LOGGER.error("Failed to update failed statuses: {}", ex.getMessage());
                                            }
                                        }
                                        LOGGER.info("Scheduled grab menu sync done, success={}, failed={}",
                                                apiSyncIdsSuccess.size(), apiSyncIdsFailed.size());
                                    }))
                    );
                })
                .doOnError(err -> LOGGER.error("Unexpected error in grab menu sync job: {}", err.getMessage()))
                .doFinally(sig -> running.set(false))
                .subscribe(
                        null,
                        err -> LOGGER.error("grabBatchUpdateMenuSchedule terminated with error: {}", err.getMessage())
                );
    }

    /**
     * Jalankan 1 request dengan token; jika 401, invalidate token, refresh, dan retry sekali.
     */
    private Mono<Void> executeWithTokenRetry(BatchUpdateMenuRequestDTO request,
                                             String token,
                                             Map<String, List<Long>> merchantToApiSyncIds,
                                             List<Long> apiSyncIdsSuccess,
                                             List<Long> apiSyncIdsFailed) {

        return grabMartMenuSyncService.batchUpdateMenuReactive(request, token)
                .doOnSuccess(v -> {
                    List<Long> ids = merchantToApiSyncIds.getOrDefault(request.getMerchantID(), Collections.emptyList());
                    apiSyncIdsSuccess.addAll(ids);
                })
                .onErrorResume(err -> {
                    if (isUnauthorized(err)) {
                        LOGGER.warn("401 Unauthorized for merchant {}. Refreshing token and retrying once.", request.getMerchantID());
                        invalidateToken();
                        return getGrabTokenMono().flatMap(newToken ->
                                grabMartMenuSyncService.batchUpdateMenuReactive(request, newToken)
                                        .doOnSuccess(v2 -> {
                                            List<Long> ids = merchantToApiSyncIds.getOrDefault(request.getMerchantID(), Collections.emptyList());
                                            apiSyncIdsSuccess.addAll(ids);
                                        })
                        ).onErrorResume(retryErr -> {
                            LOGGER.error("Retry failed for merchant {}: {}", request.getMerchantID(), retryErr.getMessage());
                            List<Long> ids = merchantToApiSyncIds.getOrDefault(request.getMerchantID(), Collections.emptyList());
                            apiSyncIdsFailed.addAll(ids);
                            return Mono.empty();
                        });
                    } else {
                        LOGGER.error("Error updating merchant {}: {}", request.getMerchantID(), err.getMessage());
                        List<Long> ids = merchantToApiSyncIds.getOrDefault(request.getMerchantID(), Collections.emptyList());
                        apiSyncIdsFailed.addAll(ids);
                        return Mono.empty();
                    }
                });
    }

    /**
     * Ambil token Grab dengan cache + refresh aman:
     * - Jika token masih valid (dengan safety margin), return Mono.just(token)
     * - Jika expired, hanya SATU fetch yang jalan; subscriber lain share hasil yang sama.
     */
    private Mono<String> getGrabTokenMono() {
        long now = System.currentTimeMillis();
        String token = this.cachedToken;
        if (token != null && now < this.cachedTokenExpiryMillis) {
            return Mono.just(token);
        }

        Mono<String> currentInFlight = this.inFlightTokenMono;
        if (currentInFlight != null) {
            return currentInFlight;
        }

        synchronized (tokenLock) {
            token = this.cachedToken;
            if (token != null && System.currentTimeMillis() < this.cachedTokenExpiryMillis) {
                return Mono.just(token);
            }
            if (this.inFlightTokenMono != null) {
                return this.inFlightTokenMono;
            }

            this.inFlightTokenMono = Mono.fromCallable(grabMartOAuthService::getAccessToken)
                    .subscribeOn(Schedulers.boundedElastic())
                    .timeout(Duration.ofSeconds(10))
                    .flatMap(resp -> {
                        if (resp == null || resp.getAccessToken() == null) {
                            return Mono.error(new IllegalStateException("Grab token is null"));
                        }
                        long ttlFromApi = resp.getExpiresIn() > 0 ? resp.getExpiresIn() : DEFAULT_TOKEN_TTL_SECONDS;
                        long effectiveTtl = Math.max(MIN_TTL_SECONDS, ttlFromApi - SAFETY_MARGIN_SECONDS);
                        long expiryMillis = System.currentTimeMillis() + (effectiveTtl * 1000L);

                        synchronized (tokenLock) {
                            this.cachedToken = resp.getAccessToken();
                            this.cachedTokenExpiryMillis = expiryMillis;
                        }
                        return Mono.just(resp.getAccessToken());
                    })
                    // share hasil fetch ke semua subscriber yang menunggu
                    .cache()
                    // apapun akhirnya, kosongkan penanda in-flight agar fetch berikutnya bisa jalan saat perlu
                    .doFinally(sig -> this.inFlightTokenMono = null);

            return this.inFlightTokenMono;
        }
    }

    private void invalidateToken() {
        synchronized (tokenLock) {
            this.cachedToken = null;
            this.cachedTokenExpiryMillis = 0L;
        }
    }

    private boolean isUnauthorized(Throwable t) {
        if (t == null) return false;
        if (t instanceof WebClientResponseException) {
            try {
                return ((WebClientResponseException) t).getStatusCode().value() == 401;
            } catch (Throwable ignored) { /* fallback below */ }
        }
        String className = t.getClass().getName();
        if (className.contains("Unauthorized")) return true;
        String msg = t.getMessage();
        return msg != null && (msg.contains("401") || msg.toLowerCase(Locale.ROOT).contains("unauthorized"));
    }
}
