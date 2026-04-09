package com.oxysystem.general.dto.grab.view;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ModifierGroupDTO {
    private String id;

    private String name;

    private String availableStatus;

    private Integer selectionRangeMin;

    private Integer selectionRangeMax;

    private Integer sequence;

    private List<ModifierDTO> modifiers;
}
