package com.oxysystem.general.dto.grab.data;

import com.oxysystem.general.enums.grab.StateStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PushOrderStateRequestDTO {
    @NotNull(message = "merchant ID cannot be empty!")
    private String merchantID;

    private String partnerMerchantID;

    @NotNull(message = "order ID cannot be empty!")
    private String orderID;

    @NotNull(message = "status cannot be empty!")
    private String state;

    private Integer driverETA;

    private String code;

    private String message;
}
