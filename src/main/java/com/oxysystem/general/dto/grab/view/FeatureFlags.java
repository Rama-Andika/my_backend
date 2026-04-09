package com.oxysystem.general.dto.grab.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.oxysystem.general.enums.grab.OrderAcceptedType;
import com.oxysystem.general.enums.grab.OrderType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class FeatureFlags {
        @NotNull(message = "order accepted type is invalid!")
        private String orderAcceptedType;

        @NotNull(message = "order type is invalid!")
        private String orderType;

        @JsonProperty("isMexEditOrder")
        private Boolean mexEditOrder;

}
