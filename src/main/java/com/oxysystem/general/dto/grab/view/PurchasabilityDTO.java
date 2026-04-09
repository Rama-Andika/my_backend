package com.oxysystem.general.dto.grab.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchasabilityDTO {
    @JsonProperty("Delivery_OnDemand_GrabApp")
    private boolean deliveryOnDemandGrabApp;

    @JsonProperty("Delivery_Scheduled_GrabApp")
    private boolean deliveryScheduledGrabApp;

    @JsonProperty("SelfPickUp_OnDemand_GrabApp")
    private boolean selfPickupOnDemandGrabApp;

    @JsonProperty("Delivery_OnDemand_StoreFront")
    private boolean deliveryOnDemandStoreFront;

    @JsonProperty("Delivery_Scheduled_StoreFront")
    private boolean deliveryScheduledStoreFront;

    @JsonProperty("SelfPickUp_OnDemand_StoreFront")
    private boolean selfPickupOnDemandStoreFront;
}
