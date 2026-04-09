package com.oxysystem.general.dto.general.location.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LocationViewDTO {
    private String id;
    private String name;
    private String grabMerchantId;
    private String grabMenuMartSyncStatus;
    private String grabFoodMerchantId;
    private String grabMenuFoodSyncStatus;
}
