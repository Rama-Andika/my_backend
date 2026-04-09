package com.oxysystem.general.dto.posmaster.grabMartItemMapping.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GrabmartItemMappingViewDTO {
    private Long grabmartItemGroupId;

    private Long grabmartItemCategoryId;

    private Long itemMasterId;

    private String grabmartCategory;

    private String grabmartCategoryId;

    private Integer grabmartCategorySequence;

    private String grabmartSubCategory;

    private String grabmartSubCategoryId;

    private Integer grabmartSubCategorySequence;

    private String itemName;

    private String barcode;

    private double price;

    private double stock;

    private String specialType;

    private String grabmartUnit;

    private Double value;

    public GrabmartItemMappingViewDTO(String itemName, String barcode) {
        this.itemName = itemName;
        this.barcode = barcode;
    }

    public GrabmartItemMappingViewDTO(Long grabmartItemGroupId, Long grabmartItemCategoryId, Long itemMasterId, String itemName, String barcode, String specialType, String grabmartUnit, Double value) {
        this.grabmartItemGroupId = grabmartItemGroupId;
        this.grabmartItemCategoryId = grabmartItemCategoryId;
        this.itemMasterId = itemMasterId;
        this.itemName = itemName;
        this.barcode = barcode;
        this.specialType = specialType;
        this.grabmartUnit = grabmartUnit;
        this.value = value;
    }
}
