package com.oxysystem.general.service.general.impl;

import com.oxysystem.general.dto.general.dashboard.view.DashboardSummaryGrabDTO;
import com.oxysystem.general.dto.general.dashboard.view.RecentSalesGrabDTO;
import com.oxysystem.general.model.tenant.general.DashboardSummaryGrab;
import com.oxysystem.general.repository.tenant.general.DashboardSummaryGrabRepository;
import com.oxysystem.general.response.SuccessResponse;
import com.oxysystem.general.service.general.DashboardGrabService;
import com.oxysystem.general.service.transaction.sales.SalesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Service
public class DashboardGrabServiceImpl implements DashboardGrabService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DashboardGrabServiceImpl.class);
    private final DashboardSummaryGrabRepository dashboardSummaryGrabRepository;
    private final SalesService salesService;
    private final ThreadPoolTaskExecutor dashboardSummaryGrabExecutor;

    public DashboardGrabServiceImpl(DashboardSummaryGrabRepository dashboardSummaryGrabRepository, SalesService salesService, @Qualifier("dashboardSummaryGrabExecutor") ThreadPoolTaskExecutor dashboardSummaryGrabExecutor) {
        this.dashboardSummaryGrabRepository = dashboardSummaryGrabRepository;
        this.salesService = salesService;
        this.dashboardSummaryGrabExecutor = dashboardSummaryGrabExecutor;
    }

    private Mono<DashboardSummaryGrabDTO> getDashboardSummaryDtoReactive(int type) {
        return Mono.fromCallable(() -> salesService.mappingToDashboardSummaryByCashMasterType(type))
                .subscribeOn(Schedulers.fromExecutor(dashboardSummaryGrabExecutor));
    }

    private Mono<DashboardSummaryGrab> findFirstDashboardSummaryGrabReactive(String product) {
        return Mono.fromCallable(() -> {
                    DashboardSummaryGrab dashboardSummaryGrab = dashboardSummaryGrabRepository.getDashboardSummaryGrabByProduct(product).orElse(null);
                    return dashboardSummaryGrab;
                })
                .subscribeOn(Schedulers.fromExecutor(dashboardSummaryGrabExecutor))
                .flatMap(d -> d == null ? Mono.empty() : Mono.just(d));
    }

    private Mono<DashboardSummaryGrab> saveDashboardSummaryGrab(DashboardSummaryGrab grab) {
        return Mono.fromCallable(() -> dashboardSummaryGrabRepository.save(grab))
                .subscribeOn(Schedulers.fromExecutor(dashboardSummaryGrabExecutor));
    }

    @Override
    public Mono<Void> saveAsync(int type, String product) {
        return getDashboardSummaryDtoReactive(type)
                .flatMap(data ->
                        findFirstDashboardSummaryGrabReactive(product)
                                .switchIfEmpty(Mono.just(new DashboardSummaryGrab()))
                                .flatMap(existing -> {
                                    existing.setSalesAmount(data.getSalesAmount());
                                    existing.setSalesAmountToday(data.getSalesAmountToday());
                                    existing.setTotalSales(data.getTotalSales());
                                    existing.setTotalSalesToday(data.getTotalSalesToday());
                                    existing.setProduct(product);
                                    return saveDashboardSummaryGrab(existing);
                                })
                )
                .doOnSuccess(v -> LOGGER.debug("Saved dashboard summary successfully"))
                .doOnError(e -> LOGGER.error("Error during saveAsync: {}", e.getMessage()))
                .then();
    }

    @Override
    public void save(int type, String product) {
        DashboardSummaryGrabDTO data = salesService.mappingToDashboardSummaryByCashMasterType(type);
        DashboardSummaryGrab existing = dashboardSummaryGrabRepository.getDashboardSummaryGrabByProduct(product)
                .orElse(new DashboardSummaryGrab());
        existing.setSalesAmount(data.getSalesAmount());
        existing.setSalesAmountToday(data.getSalesAmountToday());
        existing.setTotalSales(data.getTotalSales());
        existing.setTotalSalesToday(data.getTotalSalesToday());
        existing.setProduct(product);
        dashboardSummaryGrabRepository.save(existing);
    }

    @Override
    public ResponseEntity<?> getDashboardSummaryGrab(String product) {
        DashboardSummaryGrabDTO data = dashboardSummaryGrabRepository.getDashboardSummaryGrab(product);
        SuccessResponse<DashboardSummaryGrabDTO> response = new SuccessResponse<>("success", data);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> getRecentSalesByCashMasterType(int type) {
        List<RecentSalesGrabDTO> recentSales = salesService.recentSalesByCashMasterType(type);
        SuccessResponse<List<RecentSalesGrabDTO>> response = new SuccessResponse<>("success", recentSales);
        return ResponseEntity.ok(response);
    }
}
