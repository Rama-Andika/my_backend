package com.oxysystem.general.dto.grab.view;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CurrencyDTO {
    @NotNull(message = "currency code cannot be empty!")
    private String code;

    @NotNull(message = "currency symbol cannot be empty!")
    private String symbol;

    @NotNull(message = "exponent cannot be empty!")
    private Integer exponent;
}
