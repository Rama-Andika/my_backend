package com.oxysystem.general.scheduler.grab.grabMart;

import com.oxysystem.general.dto.grab.data.ListOrderResponseDTO;
import com.oxysystem.general.dto.transaction.sales.view.SalesViewDTO;
import com.oxysystem.general.enums.grab.Product;
import com.oxysystem.general.model.db1.system.SystemMain;
import com.oxysystem.general.service.grab.client.grabMart.GrabMartOAuthServiceImpl;
import com.oxysystem.general.service.grab.client.grabMart.GrabMartOrderSyncServiceImpl;
import com.oxysystem.general.service.system.SystemMainService;
import com.oxysystem.general.service.transaction.sales.SalesService;
import com.oxysystem.general.util.GrabNumberChecker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import javax.annotation.PreDestroy;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@ConditionalOnProperty(value = "schedule.split-order-grab-mart.enabled", havingValue = "true")
@Slf4j
public class SplitOrderMartScheduler {
    private final SalesService salesService;
    private final SystemMainService systemMainService;
    private final GrabMartOAuthServiceImpl oAuthService;
    private final GrabMartOrderSyncServiceImpl orderSyncService;
    private final Scheduler scheduler;
    private final RateLimiter rateLimiter;

    public SplitOrderMartScheduler(SalesService salesService, SystemMainService systemMainService, @Qualifier("splitOrderGrabScheduler") Scheduler scheduler,
                                   GrabMartOAuthServiceImpl oAuthService, GrabMartOrderSyncServiceImpl orderSyncService,
                                   @Qualifier("splitOrderGrabMartRateLimiter") RateLimiter rateLimiter) {
        this.salesService = salesService;
        this.systemMainService = systemMainService;
        this.oAuthService = oAuthService;
        this.orderSyncService = orderSyncService;
        this.scheduler = scheduler;
        this.rateLimiter = rateLimiter;
    }

    @Scheduled(cron = "${schedule.split-order-grab-mart.cron}")
    public void splitOrderGrabMart()
    {
        // get grab mart token
        Mono<String> grabTokenMono = oAuthService.getGrabTokenReactive();

        String date = String.valueOf(LocalDate.now().minusDays(1));

        systemMainService.findSystemPropertyNameReactive("GRABMART_SALES_USER_ID")
                .flatMap(optional -> {
                    if(!optional.isPresent()) {
                        log.error("Grab mart sales not found!");
                        return Mono.empty();
                    }

                    SystemMain systemMain = optional.get();
                    return salesService.getSalesGrabSplitOrderReactive(date, systemMain.getValueprop())
                            .collect(Collectors.groupingBy(SalesViewDTO::getGrabMerchantId));
                })
                .zipWith(grabTokenMono)
                .flatMapMany(tuple -> {
                    String token = tuple.getT2();
                    Map<String, List<SalesViewDTO>> map = tuple.getT1();

                    if(map.isEmpty()){
                        log.warn("No Grab split order found");
                        return Flux.empty();
                    }

                    log.info("Processing split order {} merchants", map.size());

                    return Flux.fromIterable(map.entrySet())
                            .flatMap(entry -> {
                                String grabMerchantId =  entry.getKey();

                                if(entry.getValue().isEmpty()){
                                    log.warn("[Merchant {}] detail sales not found", grabMerchantId);
                                    return Flux.empty();
                                }

                                return Flux.fromIterable(entry.getValue())
                                        .buffer(10)
                                        .flatMap(batch -> {
                                            List<String> orderIds = batch.stream()
                                                    .map(SalesViewDTO::getNumber)
                                                    .collect(Collectors.toList());

                                            log.info("[Merchant {}] Request list order", grabMerchantId);

                                            return rateLimiter.execute(
                                                            orderSyncService.getListOrderReactive(token, grabMerchantId, null, 1, orderIds)
                                                    )
                                                    .doOnError(e -> log.error("[Merchant {}] get list order error: {}", grabMerchantId, e.getMessage()))
                                                    .onErrorResume(e -> {
                                                        log.error("[Merchant {}] get list order error: {}", grabMerchantId, e.getMessage());
                                                        return Mono.empty();
                                                    })
                                                    .flatMapMany(r -> {
                                                        if(r.getOrders() == null || r.getOrders().isEmpty()){
                                                            log.warn("[Merchant {}] get list order error because order is empty", grabMerchantId);
                                                            return Flux.empty();
                                                        }

                                                        Map<String, List<ListOrderResponseDTO.Order>> collect = r.getOrders()
                                                                .stream()
                                                                .collect(Collectors.groupingBy(o -> {
                                                                    GrabNumberChecker.ParsedId parsedId = GrabNumberChecker.parse(o.getOrderID()).orElse(null);
                                                                    if(parsedId == null) return "UNKNOWN";

                                                                    return parsedId.getPrefixNumeric() + "-" + parsedId.getCode();
                                                                }));

                                                        return Flux.fromIterable(collect.entrySet());
                                                    })
                                                    .flatMap(listEntry -> {
                                                        String orderId = listEntry.getKey();
                                                        List<ListOrderResponseDTO.Order> orders = listEntry.getValue();

                                                        return salesService.grabSplitOrderFixDiscountReactive(orders, Product.GRAB_MART.name())
                                                                .doOnError(e -> log.error("❌ [{}][{}] fixDiscount failed: {}", grabMerchantId, orderId, e.getMessage()))
                                                                .onErrorResume(e ->{
                                                                    log.error("❌ [{}][{}] fixDiscount failed: {}", grabMerchantId, orderId, e.getMessage());
                                                                    return Mono.empty();
                                                                });
                                                    });
                                        })
                                        .doOnComplete(() -> log.info("[Merchant {}] ✅ All batches processed", grabMerchantId))
                                        .doOnError(e -> log.error("[Merchant {}] ❌ Error in merchant pipeline: {}", grabMerchantId, e.getMessage()))
                                        .onErrorResume(e -> Flux.empty());

                            })
                            .onErrorResume(e -> {
                                log.error("Split order pipeline error recovered: {}", e.getMessage(), e);
                                return Flux.empty();
                            });
                })
                .publishOn(scheduler)
                .doOnComplete(() -> log.info("Split order pipeline completed"))
                .doOnError(e -> log.error("Split order pipeline is error {}", e.getMessage()))
                .subscribe();
    }


    @PreDestroy
    public void shutdown() {
        log.info("Shutting down splitOrderGrabScheduler...");
        rateLimiter.shutdown();
    }

    public static class RateLimiter {
        private final java.util.concurrent.Semaphore semaphore = new java.util.concurrent.Semaphore(4);
        private final Scheduler scheduler;
        private final Disposable refillTask;

        public RateLimiter(Scheduler scheduler) {
            this.scheduler = scheduler;
            // refill permit tiap detik
            this.refillTask = Flux.interval(java.time.Duration.ofSeconds(1), scheduler)
                    .doOnNext(tick -> {
                        int toRelease = 4 - semaphore.availablePermits();
                        if (toRelease > 0) semaphore.release(toRelease);
                    })
                    .subscribe();
        }

        public <T> Mono<T> execute(Mono<T> supplier) {
            return Mono.defer(() -> {
                if (semaphore.tryAcquire()) {
                    return supplier.doFinally(sig -> semaphore.release());
                } else {
                    return Mono.delay(java.time.Duration.ofMillis(250), scheduler)
                            .flatMap(t -> execute(supplier));
                }
            });
        }

        public void shutdown() {
            if (!refillTask.isDisposed()) {
                refillTask.dispose();
                log.info("✅ RateLimiter shut down cleanly");
            }
        }
    }
}
