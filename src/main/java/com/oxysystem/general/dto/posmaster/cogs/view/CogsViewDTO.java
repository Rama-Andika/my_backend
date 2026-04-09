package com.oxysystem.general.dto.posmaster.cogs.view;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CogsViewDTO {
    private Long itemMasterId;

    private Double cogs;

    public CogsViewDTO(Long itemMasterId, Double cogs) {
        this.itemMasterId = itemMasterId;
        this.cogs = cogs != null ? cogs : 0;
    }
}
