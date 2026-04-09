package com.oxysystem.general.dto.grab.view;

import com.oxysystem.general.enums.grab.AvailableStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
public class ModifierDTO {
    private String id;

    private String name;

    private String availableStatus;

    private Long price;

    private String barcode;

    private AdvancedPricingDTO advancedPricing;
}
