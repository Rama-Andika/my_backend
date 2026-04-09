package com.oxysystem.general.dto.transaction.stock.view;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockViewDTO {
    private Double stock;
    private Long itemMasterId;

    public StockViewDTO(Double stock, Long itemMasterId) {
        this.stock = stock != null ? stock : 0.0;
        this.itemMasterId = itemMasterId;
    }
}
