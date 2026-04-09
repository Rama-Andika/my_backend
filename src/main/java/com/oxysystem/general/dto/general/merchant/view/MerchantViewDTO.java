package com.oxysystem.general.dto.general.merchant.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MerchantViewDTO {
    private String merchantId;

    private String bankId;

    private String locationId;

    private String paymentMethodId;

    private String description;

    private Double persenExpense;

    private Integer postingExpense;

    private Integer paymentBy;
}
