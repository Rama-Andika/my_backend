package com.oxysystem.general.dto.grab.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemDTO {

    private String id;

    private String name;

    private String availableStatus;

    private String description;

    private Long price;

    private List<String> photos;

    private String specialType;

    private boolean taxable;

    private String barcode;

    private Integer maxStock;

    private Integer maxCount;

    private WeightDTO weight;

    private boolean soldByWeight;

    private SellingUomDTO sellingUom;

    private String sellingTimeID;

    private Integer sequence;

    private AdvancedPricingDTO advancedPricing;

    private PurchasabilityDTO purchasability;

    private List<ModifierGroupDTO> modifierGroups;
}
