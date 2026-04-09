package com.oxysystem.general.dto.posmaster.itemMaster.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemMasterView2DTO {
    private String itemId;

    private String itemName;

    private String code;

    private String barcode;

    private String category;

    private String subCategory;

    private Double stock;

    private Double price;

    private List<String> images;
}
