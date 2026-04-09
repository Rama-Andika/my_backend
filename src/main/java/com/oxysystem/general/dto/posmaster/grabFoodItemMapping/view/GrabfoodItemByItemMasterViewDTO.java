package com.oxysystem.general.dto.posmaster.grabFoodItemMapping.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GrabfoodItemByItemMasterViewDTO {
    private String itemId;
    private String itemName;
    private String description;
    private String barcode;
    private String code;
    private String grabItemGroupId;
    private String grabItemGroup;
    private Integer isPublished;
    private String specialType;
    private List<Location> locations;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Location {
        private String id;
        private String name;
        private boolean check;
    }
}
