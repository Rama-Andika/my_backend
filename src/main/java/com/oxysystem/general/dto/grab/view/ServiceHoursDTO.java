package com.oxysystem.general.dto.grab.view;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ServiceHoursDTO {
    private String openPeriodType;
    private List<PeriodDTO> periods;
}
