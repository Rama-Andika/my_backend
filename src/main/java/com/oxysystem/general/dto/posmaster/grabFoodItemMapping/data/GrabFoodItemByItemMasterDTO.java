package com.oxysystem.general.dto.posmaster.grabFoodItemMapping.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GrabFoodItemByItemMasterDTO {
    private String itemId;
    private String description;
    private String grabItemGroupId;
    private Integer isPublished;
    private String specialType;
    private List<Location> locations;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Location {
        private String id;
        private boolean check;
    }
}
