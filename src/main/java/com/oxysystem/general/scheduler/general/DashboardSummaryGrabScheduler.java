package com.oxysystem.general.scheduler.general;

import com.oxysystem.general.enums.CashMasterType;
import com.oxysystem.general.enums.grab.Product;
import java.util.List;
import com.oxysystem.general.config.tenant.TenantContext;
import com.oxysystem.general.model.master.Tenant;
import com.oxysystem.general.repository.master.TenantRepository;
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
    private final TenantRepository tenantRepository;

    public DashboardSummaryGrabScheduler(DashboardGrabService dashboardGrabService, TenantRepository tenantRepository) {
        this.dashboardGrabService = dashboardGrabService;
        this.tenantRepository = tenantRepository;
    }

    @Scheduled(cron = "${schedule.dashboard-grab-mart-summary.cron}")
    public void updateDataToDashboardSummaryGrabMart() {
        LOGGER.info("Update data to dashboard summary grab mart started");
        List<Tenant> tenants = tenantRepository.findAll();
        for (Tenant tenant : tenants) {
            try {
                TenantContext.setCurrentTenant(tenant.getTenantId());
                dashboardGrabService.save(CashMasterType.GRAB_POS.ordinal(), Product.GRAB_MART.name());
            } catch (Exception e) {
                LOGGER.error("Schedule error in updateDataToDashboardSummaryGrabMart for tenant {}: {}", tenant.getTenantId(), e.getMessage());
            } finally {
                TenantContext.clear();
            }
        }
        LOGGER.info("Update data to dashboard summary grab completed");
    }

    @Scheduled(cron = "${schedule.dashboard-grab-food-summary.cron}")
    public void updateDataToDashboardSummaryGrabFood() {
        LOGGER.info("Update data to dashboard summary grab food started");
        List<Tenant> tenants = tenantRepository.findAll();
        for (Tenant tenant : tenants) {
            try {
                TenantContext.setCurrentTenant(tenant.getTenantId());
                dashboardGrabService.save(CashMasterType.GRAB_FOOD_POS.ordinal(), Product.GRAB_FOOD.name());
            } catch (Exception e) {
                LOGGER.error("Schedule error in updateDataToDashboardSummaryGrabFood for tenant {}: {}", tenant.getTenantId(), e.getMessage());
            } finally {
                TenantContext.clear();
            }
        }
        LOGGER.info("Update data to dashboard summary grab completed");
    }
}
