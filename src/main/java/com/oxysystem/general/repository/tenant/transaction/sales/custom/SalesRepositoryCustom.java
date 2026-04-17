package com.oxysystem.general.repository.tenant.transaction.sales.custom;

import com.oxysystem.general.dto.general.dashboard.view.DashboardSummaryGrabDTO;
import com.oxysystem.general.dto.general.dashboard.view.RecentSalesGrabDTO;
import com.oxysystem.general.dto.transaction.sales.view.SalesViewDTO;

import java.util.List;

public interface SalesRepositoryCustom {
    DashboardSummaryGrabDTO mappingToDashboardSummaryByCashMasterType(int type);
    List<RecentSalesGrabDTO> recentSalesByCashMasterType(int type);
    List<SalesViewDTO> getSalesGrabSplitOrder(String date, String userId);

}
