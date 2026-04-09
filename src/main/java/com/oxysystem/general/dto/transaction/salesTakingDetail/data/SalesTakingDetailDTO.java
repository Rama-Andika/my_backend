package com.oxysystem.general.dto.transaction.salesTakingDetail.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SalesTakingDetailDTO {
    private String id;
    @NotNull(message = "Item is required")
    private String itemMasterId;
    private Double price;
    private Double qty;
    private Double discountItem;
    private String unit;
    private String uomId;
    private Double convQty;
    private Double qtyReturn;
}
