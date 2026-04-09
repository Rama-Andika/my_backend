package com.oxysystem.general.scheduler.grab.grabFood;

import com.oxysystem.general.dto.grab.data.ListOrderResponseDTO;
import com.oxysystem.general.enums.grab.StateStatus;
import com.oxysystem.general.model.db1.posmaster.StrukKasir;
import com.oxysystem.general.model.db1.transaction.sales.Sales;
import com.oxysystem.general.service.grab.client.grabFood.GrabFoodOAuthServiceImpl;
import com.oxysystem.general.service.grab.client.grabFood.GrabFoodOrderSyncServiceImpl;
import com.oxysystem.general.service.grab.partner.grabFood.GrabFoodOrderSyncPartnerServiceImpl;
import com.oxysystem.general.service.posmaster.StrukKasirService;
import com.oxysystem.general.service.transaction.sales.SalesService;
import com.oxysystem.general.service.transaction.stock.StockService;
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
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@ConditionalOnProperty(value = "schedule.grab-food-upload-sales.enabled", havingValue = "true")
public class UploadSalesFoodScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadSalesFoodScheduler.class);

    private final GrabFoodOrderSyncServiceImpl orderSyncService;
    private final GrabFoodOrderSyncPartnerServiceImpl orderSyncPartnerService;
    private final SalesService salesService;
    private final GrabFoodOAuthServiceImpl oAuthService;
    private final StrukKasirService strukKasirService;
    private final StockService stockService;
    private final RateLimiter rateLimiter;

    public UploadSalesFoodScheduler(GrabFoodOrderSyncServiceImpl orderSyncService, GrabFoodOrderSyncPartnerServiceImpl orderSyncPartnerService, SalesService salesService, GrabFoodOAuthServiceImpl oAuthService, StrukKasirService strukKasirService, StockService stockService, @Qualifier("uploadSalesGrabFoodScheduler") Scheduler scheduler) {
        this.orderSyncService = orderSyncService;
        this.orderSyncPartnerService = orderSyncPartnerService;
        this.salesService = salesService;
        this.oAuthService = oAuthService;
        this.strukKasirService = strukKasirService;
        this.stockService = stockService;
        this.rateLimiter = new RateLimiter(scheduler);
    }

    @Scheduled(cron = "${schedule.grab-food-upload-sales.cron}")
    public void uploadSales() {
        Mono<String> tokenMono = oAuthService.getGrabTokenReactive()
                .doOnNext(token -> LOGGER.info("Grab food token fetched length={}", token.length()));

        Mono<List<String>> merchantIdsMono = Mono.fromCallable(() ->
                        strukKasirService.findStrukKasirGrabFoodMerchantIDNotNull()
                                .stream()
                                .map(StrukKasir::getGrabFoodMerchantId)
                                .filter(Objects::nonNull)
                                .distinct()
                                .collect(Collectors.toList())
                )
                .subscribeOn(Schedulers.boundedElastic());

        Mono.zip(tokenMono, merchantIdsMono)
                .flatMapMany(tuple -> {
                    String token = tuple.getT1();
                    List<String> merchantIds = tuple.getT2();

                    if (token.isEmpty()) {
                        LOGGER.error("Grab food token is empty, aborting upload sales pipeline");
                        return Flux.empty();
                    }

                    if (merchantIds.isEmpty()) {
                        LOGGER.info("No merchants to process for upload sales");
                        return Flux.empty();
                    }

                    LOGGER.info("Starting upload sales pipeline for {} merchants", merchantIds.size());
                    return rateLimiter.process(merchantIds, token, orderSyncService, orderSyncPartnerService, salesService, stockService);
                })
                .doOnError(e -> LOGGER.error("Pipeline-level error: {}", e.getMessage(), e))
                .doOnComplete(() -> LOGGER.info("Upload sales pipeline completed."))
                .subscribe();
    }

    @PreDestroy
    public void shutdown() {
        LOGGER.info("Shutting down UploadSalesScheduler...");
        rateLimiter.shutdown();
    }

    // ================================
    // RATE LIMITER
    // ================================
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

        public Flux<Void> process(List<String> merchantIds,
                                  String token,
                                  GrabFoodOrderSyncServiceImpl orderSyncService,
                                  GrabFoodOrderSyncPartnerServiceImpl orderSyncPartnerService,
                                  SalesService salesService,
                                  StockService stockService) {
            if (merchantIds.isEmpty()) {
                return Flux.empty();
            }

            AtomicInteger counter = new AtomicInteger(0);

            return Flux.fromIterable(merchantIds)
                    .flatMap(id ->
                                    tryProcessWithPermit(id, token, orderSyncService, orderSyncPartnerService, salesService, stockService, counter),
                            10 // concurrency
                    );
        }

        private Mono<Void> tryProcessWithPermit(String merchantId,
                                                String token,
                                                GrabFoodOrderSyncServiceImpl orderSyncService,
                                                GrabFoodOrderSyncPartnerServiceImpl orderSyncPartnerService,
                                                SalesService salesService,
                                                StockService stockService,
                                                AtomicInteger counter) {

            return Mono.defer(() -> {
                if (semaphore.tryAcquire()) {
                    int index = counter.getAndIncrement();
                    int batchNumber = (index / 10) + 1;
                    LocalDate currentDate = LocalDate.now();

                    LOGGER.info("[START][Batch {}] Merchant {} mulai diproses", batchNumber, merchantId);

                    return Flux.just(currentDate.minusDays(1), currentDate)
                            .concatMap(date ->
                                    orderSyncService.getListOrderReactive(
                                            token,
                                            merchantId,
                                            String.valueOf(date),
                                            1,
                                            Collections.emptyList()
                                    )
                            )
                            .flatMap(orders -> processOrders(merchantId, orders, salesService, orderSyncPartnerService, stockService))
                            .then()
                            .doFinally(signal -> {
                                semaphore.release();
                                LOGGER.info("[DONE][Batch {}] Merchant {} selesai, release permit (available={})",
                                        batchNumber, merchantId, semaphore.availablePermits());
                            })
                            .onErrorResume(e -> {
                                LOGGER.error("Error while processing merchant {}: {}", merchantId, e.getMessage(), e);
                                return Mono.empty();
                            });

                } else {
                    LOGGER.debug("No permit available for merchant {}, retrying...", merchantId);
                    return Mono.delay(Duration.ofMillis(100), scheduler)
                            .then(tryProcessWithPermit(merchantId, token, orderSyncService, orderSyncPartnerService, salesService, stockService, counter));
                }
            });
        }

        private Mono<Void> processOrders(String merchantId,
                                         ListOrderResponseDTO orders,
                                         SalesService salesService,
                                         GrabFoodOrderSyncPartnerServiceImpl orderSyncPartnerService,
                                         StockService stockService) {
            if (orders == null || orders.getOrders() == null || orders.getOrders().isEmpty()) {
                LOGGER.info("No orders for merchant {}", merchantId);
                return Mono.empty();
            }

            Set<String> orderIdsDelivered = orders.getOrders().stream()
                    .filter(s -> s.getOrderState().equalsIgnoreCase(StateStatus.DELIVERED.name()))
                    .map(ListOrderResponseDTO.Order::getOrderID)
                    .collect(Collectors.toSet());

            LOGGER.info("Order Ids delivered {}", orderIdsDelivered);

            Set<String> orderIdsCanceled = orders.getOrders().stream()
                    .filter(s -> s.getOrderState().equalsIgnoreCase(StateStatus.CANCELLED.name()))
                    .map(ListOrderResponseDTO.Order::getOrderID)
                    .collect(Collectors.toSet());


            LOGGER.info("Order Ids Canceled {}", orderIdsCanceled);

            List<Sales> salesList = salesService.findAllSalesByNumber(new ArrayList<>(orderIdsDelivered));

            Map<String, Sales> salesMap = salesList.stream()
                    .collect(Collectors.toMap(Sales::getNumber, Function.identity()));

            int uploadedCount = 0;

            int skippedCount = 0;

            for (ListOrderResponseDTO.Order order : orders.getOrders()) {
                if (!salesMap.containsKey(order.getOrderID())) {
                    orderSyncPartnerService.automaticUploadSales(order);
                    uploadedCount++;
                } else {
                    skippedCount++;
                }
            }

            LOGGER.info("[SUMMARY][Merchant {}] Total orders: {}, Uploaded: {}, Skipped(existing): {}",
                    merchantId, orderIdsDelivered.size(), uploadedCount, skippedCount);

            return Mono.empty();
        }

        public void shutdown() {
            if (!refillTask.isDisposed()) {
                refillTask.dispose();
            }
        }
    }
}