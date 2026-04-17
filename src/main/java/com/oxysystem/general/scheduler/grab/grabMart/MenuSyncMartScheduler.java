package com.oxysystem.general.scheduler.grab.grabMart;

import com.oxysystem.general.dto.grab.data.UpdateMenuNotificationRequestDTO;
import com.oxysystem.general.model.tenant.posmaster.StrukKasir;
import com.oxysystem.general.config.tenant.TenantContext;
import com.oxysystem.general.model.master.Tenant;
import com.oxysystem.general.repository.master.TenantRepository;
import com.oxysystem.general.service.grab.client.grabMart.GrabMartMenuSyncServiceImpl;
import com.oxysystem.general.service.grab.client.grabMart.GrabMartOAuthServiceImpl;
import com.oxysystem.general.service.posmaster.StrukKasirService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import javax.annotation.PreDestroy;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
@ConditionalOnProperty(value = "schedule.grab-mart-update-menus-notification.enabled", havingValue = "true")
public class MenuSyncMartScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MenuSyncMartScheduler.class);

    private final GrabMartMenuSyncServiceImpl grabMartMenuSyncService;
    private final GrabMartOAuthServiceImpl grabMartOAuthService;
    private final StrukKasirService strukKasirService;
    private final TenantRepository tenantRepository;
    private final RateLimiter rateLimiter;

    public MenuSyncMartScheduler(GrabMartMenuSyncServiceImpl grabMartMenuSyncService,
                                 GrabMartOAuthServiceImpl grabMartOAuthService,
                                 StrukKasirService strukKasirService,
                                 TenantRepository tenantRepository,
                                 @Qualifier("menuNotifyScheduler") Scheduler scheduler) {
        this.grabMartMenuSyncService = grabMartMenuSyncService;
        this.grabMartOAuthService = grabMartOAuthService;
        this.strukKasirService = strukKasirService;
        this.tenantRepository = tenantRepository;
        this.rateLimiter = new RateLimiter(scheduler);
    }

    @Scheduled(cron = "${schedule.grab-mart-update-menus-notification.cron}")
    public void updateAllMenuNotification() {
        Mono<String> tokenMono = Mono.fromCallable(grabMartOAuthService::getGrabToken)
                .subscribeOn(Schedulers.boundedElastic());

        Mono<List<String>> merchantIdsMono = Mono.fromCallable(() -> {
                    List<Tenant> tenants = tenantRepository.findAll();
                    List<String> allMerchantIds = new java.util.ArrayList<>();
                    for (Tenant tenant : tenants) {
                        try {
                            TenantContext.setCurrentTenant(tenant.getTenantId());
                            List<String> ids = strukKasirService.findStrukKasirGrabMerchantIDNotNull()
                                    .stream()
                                    .map(StrukKasir::getGrabMerchantId)
                                    .filter(Objects::nonNull)
                                    .distinct()
                                    .collect(Collectors.toList());
                            allMerchantIds.addAll(ids);
                        } catch (Exception e) {
                            LOGGER.error("Failed to fetch GrabMart merchants for tenant {}: {}", tenant.getTenantId(), e.getMessage());
                        } finally {
                            TenantContext.clear();
                        }
                    }
                    return allMerchantIds.stream().distinct().collect(Collectors.toList());
                })
                .subscribeOn(Schedulers.boundedElastic());

        Mono.zip(tokenMono, merchantIdsMono)
                .flatMapMany(tuple -> {
                    String token = tuple.getT1();
                    List<String> merchantIds = tuple.getT2();

                    if (merchantIds.isEmpty()) {
                        LOGGER.info("No merchants to process for menu notifications.");
                        return Flux.empty();
                    }

                    LOGGER.info("Starting menu notification update pipeline for {} merchants", merchantIds.size());
                    return rateLimiter.process(merchantIds, token, grabMartMenuSyncService);
                })
                .doOnError(e -> LOGGER.error("Pipeline-level error: {}", e.getMessage(), e))
                .doOnComplete(() -> LOGGER.info("Menu notification update pipeline completed."))
                .subscribe();
    }

    @PreDestroy
    public void shutdown() {
        LOGGER.info("Shutting down MenuSyncScheduler...");
        rateLimiter.shutdown();
    }

    static class RateLimiter {
        private static final Logger LOGGER = LoggerFactory.getLogger(RateLimiter.class);

        private final Semaphore semaphore = new Semaphore(10);
        private final Scheduler scheduler;
        private final Disposable refillTask;

        public RateLimiter(Scheduler scheduler) {
            this.scheduler = scheduler;
            this.refillTask = Flux.interval(Duration.ofSeconds(1), Schedulers.parallel())
                    .doOnNext(tick -> {
                        int toRelease = 10 - semaphore.availablePermits();
                        if (toRelease > 0) {
                            semaphore.release(toRelease);
                            LOGGER.trace("Refilled {} permits, now available={}", toRelease, semaphore.availablePermits());
                        }
                    })
                    .subscribe();
        }

        public Flux<Void> process(List<String> merchantIds, String token, GrabMartMenuSyncServiceImpl grabMartMenuSyncService) {
            if (merchantIds.isEmpty()) {
                return Flux.empty();
            }

            AtomicInteger counter = new AtomicInteger(0);

            return Flux.fromIterable(merchantIds)
                    .flatMap(id -> tryProcessWithPermit(id, token, grabMartMenuSyncService, counter), 10);
        }

        private Mono<Void> tryProcessWithPermit(String id, String token, GrabMartMenuSyncServiceImpl grabMartMenuSyncService, AtomicInteger counter) {
            return Mono.defer(() -> {
                if (semaphore.tryAcquire()) {
                    int index = counter.getAndIncrement();
                    int batchNumber = (index / 10) + 1;

                    LOGGER.info("[START][Batch {}] Merchant {} mulai diproses", batchNumber, id);

                    return grabMartMenuSyncService.updateMenuNotifyReactive(token, new UpdateMenuNotificationRequestDTO(id))
                            .doFinally(signal -> {
                                semaphore.release();
                                LOGGER.info("[DONE][Batch {}] Merchant {} selesai, release permit (available={})",
                                        batchNumber, id, semaphore.availablePermits());
                            })
                            .onErrorResume(e -> {
                                LOGGER.error("Failed updating merchantId={} : {}", id, e.getMessage(), e);
                                return Mono.empty();
                            });
                } else {
                    LOGGER.debug("No permit available for merchant {}, retrying...", id);
                    return Mono.delay(Duration.ofMillis(100), scheduler)
                            .then(tryProcessWithPermit(id, token, grabMartMenuSyncService, counter));
                }
            });
        }

        public void shutdown() {
            if (!refillTask.isDisposed()) {
                refillTask.dispose();
            }
        }
    }
}
