package com.oxysystem.general.dto.grab.view;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
public class WeightDTO {
    private String unit;
    private Double value;
    private Integer count;
}
