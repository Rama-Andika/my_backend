package com.oxysystem.general.dto.posmaster.grabFoodItemMapping.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GrabfoodItemMappingViewDTO {
    private Long grabItemGroupId;

    private Long itemMasterId;

    private String grabCategory;

    private String grabCategoryId;

    private Integer grabCategorySequence;

    private String itemName;

    private String barcode;

    private double price;

    private double stock;

    private String specialType;

    private String description;
}
