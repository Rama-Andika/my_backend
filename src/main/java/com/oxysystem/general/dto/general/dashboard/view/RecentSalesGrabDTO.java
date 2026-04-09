package com.oxysystem.general.dto.general.dashboard.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class RecentSalesGrabDTO {
    private String store;
    private String number;
    private LocalDate date;
    private BigDecimal totalSales;

    public RecentSalesGrabDTO(String store,String number, LocalDate date, BigDecimal totalSales) {
        this.store = store;
        this.number = number;
        this.date = date;
        this.totalSales = totalSales != null ? totalSales : BigDecimal.ZERO;
    }
}
