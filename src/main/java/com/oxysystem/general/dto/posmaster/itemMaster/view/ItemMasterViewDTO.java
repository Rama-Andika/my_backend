package com.oxysystem.general.dto.posmaster.itemMaster.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemMasterViewDTO {
    private String itemId;
    private String itemName;
    private String barcode;
    private String code;
    private String itemGroup;
    private String itemCategory;
    private String unit;
    private Double price;
    private String uomId;
    private Double convQty;

    public ItemMasterViewDTO(String itemId, String itemName, String barcode, String code, String itemGroup, String itemCategory) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.barcode = barcode;
        this.code = code;
        this.itemGroup = itemGroup;
        this.itemCategory = itemCategory;
    }
}
