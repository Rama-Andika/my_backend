package com.oxysystem.general.service.transaction.sales;

import com.oxysystem.general.dto.general.dashboard.view.DashboardSummaryGrabDTO;
import com.oxysystem.general.dto.general.dashboard.view.RecentSalesGrabDTO;
import com.oxysystem.general.dto.grab.data.ListOrderResponseDTO;
import com.oxysystem.general.dto.transaction.sales.view.SalesViewDTO;
import com.oxysystem.general.model.tenant.transaction.sales.Sales;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

public interface SalesService {
    Sales save(Sales sales);

    Optional<Integer> countSalesByCashCashierId(Long id);

    Optional<Integer> countSalesDetailByCashCashierId(Long id);

    DashboardSummaryGrabDTO mappingToDashboardSummaryByCashMasterType(int type);

    List<RecentSalesGrabDTO> recentSalesByCashMasterType(int type);

    Optional<Sales> findSalesByNumber(String number);

    List<Sales> findAllSalesByNumber(List<String> numbers);

    Flux<SalesViewDTO> getSalesGrabSplitOrderReactive(String date, String userId);

    List<SalesViewDTO> getSalesGrabSplitOrder(String date, String userId);

    Mono<Void> grabSplitOrderFixDiscountReactive(List<ListOrderResponseDTO.Order> orders, String product);
}
