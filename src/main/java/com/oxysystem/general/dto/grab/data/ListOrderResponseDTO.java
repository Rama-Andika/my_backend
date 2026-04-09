package com.oxysystem.general.dto.grab.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.oxysystem.general.dto.grab.view.CurrencyDTO;
import com.oxysystem.general.dto.grab.view.FeatureFlags;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListOrderResponseDTO {
    private Boolean more;

    private List<Order> orders;

    @Getter
    @Setter
    public static class Order{
        @NotNull(message = "order ID cannot be empty!")
        private String orderID;

        @NotNull(message = "short order number cannot be empty!")
        private String shortOrderNumber;

        @NotNull(message = "merchant ID cannot be empty!")
        private String merchantID;

        private String partnerMerchantID;

        @NotNull(message = "payment type cannot be empty!")
        private String paymentType;

        private Boolean cutlery;

        @NotNull(message = "order time cannot be empty!")
        private Instant orderTime;

        private Instant submitTime;

        private Instant completeTime;

        private Instant scheduledTime;

        private String orderState;

        @NotNull(message = "currency cannot be empty!")
        @Valid
        private CurrencyDTO currency;

        @NotNull(message = "feature flags cannot be empty!")
        @Valid
        private FeatureFlags featureFlags;

        @NotNull(message = "items cannot be empty!")
        @Valid
        private List<SubmitOrderRequestDTO.Item> items;

        private List<SubmitOrderRequestDTO.Campaign> campaigns;

        private List<SubmitOrderRequestDTO.Promo> promos;

        private SubmitOrderRequestDTO.Price price;

        private SubmitOrderRequestDTO.DineIn dineIn;

        private SubmitOrderRequestDTO.Receiver receiver;

        private SubmitOrderRequestDTO.OrderReadyEstimation orderReadyEstimation;

        private String membershipID;

        private List<SubmitOrderRequestDTO.Discount> discounts;

        private List<SubmitOrderRequestDTO.Payment>  payments;
    }
}
