package com.oxysystem.general.dto.grab.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdvancedPricingDTO {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("Delivery_OnDemand_GrabApp")
    private Integer deliveryOnDemandGrabApp;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("Delivery_Scheduled_GrabApp")
    private Integer deliveryScheduledGrabApp;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("SelfPickUp_OnDemand_GrabApp")
    private Integer selfPickupOnDemandGrabApp;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("Delivery_OnDemand_StoreFront")
    private Integer deliveryOnDemandStoreFront;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("Delivery_Scheduled_StoreFront")
    private Integer deliveryScheduledStoreFront;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("SelfPickUp_OnDemand_StoreFront")
    private Integer selfPickupOnDemandStoreFront;
}
