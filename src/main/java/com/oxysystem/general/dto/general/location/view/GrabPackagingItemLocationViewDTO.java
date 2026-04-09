package com.oxysystem.general.dto.general.location.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GrabPackagingItemLocationViewDTO {
    private String id;
    private String name;
    private String itemName;
    private String barcode;
    private String itemId;
    private String grabPackagingItemId;
}
