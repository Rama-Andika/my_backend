package com.oxysystem.general.dto.general.dashboard.view;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@Setter

public class DashboardSummaryGrabDTO {
    private BigDecimal salesAmount;
    private BigDecimal salesAmountToday;
    private Integer totalSales;
    private Integer totalSalesToday;
    private String product;

    public DashboardSummaryGrabDTO(BigDecimal salesAmount, BigDecimal salesAmountToday, Integer totalSales, Integer totalSalesToday,  String product) {
        this.salesAmount = salesAmount != null ? salesAmount.setScale(2, RoundingMode.HALF_UP) : BigDecimal.valueOf(0);
        this.salesAmountToday = salesAmountToday != null ? salesAmountToday.setScale(2, RoundingMode.HALF_UP) : BigDecimal.valueOf(0);
        this.totalSales = totalSales != null ? totalSales : 0;
        this.totalSalesToday = totalSalesToday != null ? totalSalesToday : 0;
        this.product = product;
    }
}
