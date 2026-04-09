package com.oxysystem.general.dto.grab.data;

import com.oxysystem.general.enums.grab.IntegrationStatus;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PushIntegrationStatusRequestDTO {
    @NotNull(message = "partner merchant ID cannot be empty!")
    private String partnerMerchantID;

    @NotNull(message = "grab merchant ID cannot be empty!")
    private String grabMerchantID;

    @NotNull(message = "integration status cannot be empty!")
    private String integrationStatus;
}
