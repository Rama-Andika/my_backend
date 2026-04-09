package com.oxysystem.general.scheduler.general;

import com.oxysystem.general.enums.CashMasterType;
import com.oxysystem.general.enums.grab.Product;
import com.oxysystem.general.service.general.DashboardGrabService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@ConditionalOnProperty(value = "schedule.dashboard-summary.enabled", havingValue = "true")
public class DashboardSummaryGrabScheduler {
    private static final Logger LOGGER = LoggerFactory.getLogger(DashboardSummaryGrabScheduler.class);

    private final DashboardGrabService dashboardGrabService;

    public DashboardSummaryGrabScheduler(DashboardGrabService dashboardGrabService) {
        this.dashboardGrabService = dashboardGrabService;
    }

    @Scheduled(cron = "${schedule.dashboard-grab-mart-summary.cron}")
    public void updateDataToDashboardSummaryGrabMart() {
        LOGGER.info("Update data to dashboard summary grab started");

        dashboardGrabService.saveAsync(CashMasterType.GRAB_POS.ordinal(), Product.GRAB_MART.name())
                .doOnSuccess(v -> LOGGER.info("Update data to dashboard summary grab completed"))
                .doOnError(e -> LOGGER.error("Schedule error in updateDataToDashboardSummaryGrabAsync", e))
                .subscribe();
    }

    @Scheduled(cron = "${schedule.dashboard-grab-food-summary.cron}")
    public void updateDataToDashboardSummaryGrabFood() {
        LOGGER.info("Update data to dashboard summary grab started");

        dashboardGrabService.saveAsync(CashMasterType.GRAB_FOOD_POS.ordinal(), Product.GRAB_FOOD.name())
                .doOnSuccess(v -> LOGGER.info("Update data to dashboard summary grab completed"))
                .doOnError(e -> LOGGER.error("Schedule error in updateDataToDashboardSummaryGrabAsync", e))
                .subscribe();
    }
}
