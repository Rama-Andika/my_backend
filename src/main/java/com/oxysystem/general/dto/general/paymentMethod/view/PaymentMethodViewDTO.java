package com.oxysystem.general.dto.general.paymentMethod.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentMethodViewDTO {
    private String paymentMethodId;

    private String description;

    private Integer sortOrder = 0;

    private Integer posCode = 0;

    private Integer type = 0;
}
