package com.oxysystem.general.dto.transaction.payment.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentViewDTO {
    private String id;

    private Double amount = 0.0;

    private Double costCardAmount = 0.0;

    private Double costCardPercent = 0.0;

    private String bank;

    private String merchant;

    private String paymentMethod;
}
