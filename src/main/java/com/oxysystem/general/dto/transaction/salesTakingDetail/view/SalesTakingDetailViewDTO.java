package com.oxysystem.general.dto.transaction.salesTakingDetail.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SalesTakingDetailViewDTO {
    private String id;

    private String salesDetailReturnId;

    private String itemMasterName;

    private String itemMasterBarcode;

    private Double price;

    private Double qty;

    private Double total;

    private Double discountItem;

    private String itemMasterId;

    private String uomId;

    private Double convQty;

    private Double prevReturn;
}
