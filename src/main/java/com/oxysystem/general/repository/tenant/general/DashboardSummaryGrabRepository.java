package com.oxysystem.general.repository.tenant.general;

import com.oxysystem.general.dto.general.dashboard.view.DashboardSummaryGrabDTO;
import com.oxysystem.general.model.tenant.general.DashboardSummaryGrab;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DashboardSummaryGrabRepository extends JpaRepository<DashboardSummaryGrab,Long> {
    @Query("SELECT new com.oxysystem.general.dto.general.dashboard.view.DashboardSummaryGrabDTO(d.salesAmount, d.salesAmountToday, d.totalSales, d.totalSalesToday, d.product) " +
           "FROM DashboardSummaryGrab d WHERE d.product = :product")
    DashboardSummaryGrabDTO getDashboardSummaryGrab(@Param("product") String product);

    @Query("SELECT d FROM DashboardSummaryGrab d WHERE d.product = :product")
    Optional<DashboardSummaryGrab> getDashboardSummaryGrabByProduct(@Param("product") String product);
}
