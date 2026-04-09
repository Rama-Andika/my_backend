package com.oxysystem.general.dto.transaction.sales.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SalesViewDTO {
    private String salesId;

    private String number;

    private String grabMerchantId;

    private String grabFoodMerchantId;
}
