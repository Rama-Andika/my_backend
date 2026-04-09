package com.oxysystem.general.dto.general.company.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyViewDTO {
    private String id;
    private String name;
    private String address;
    private String phone;
    private Double governmentVat;
}
