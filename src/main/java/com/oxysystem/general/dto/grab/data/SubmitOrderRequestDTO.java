package com.oxysystem.general.dto.grab.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.oxysystem.general.dto.grab.view.CurrencyDTO;
import com.oxysystem.general.dto.grab.view.FeatureFlags;
import com.oxysystem.general.dto.grab.view.OutOfStockInstruction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubmitOrderRequestDTO {
    @NotNull(message = "order ID cannot be empty!")
    private String orderID;

    @NotNull(message = "short order number cannot be empty!")
    private String shortOrderNumber;

    @NotNull(message = "merchant ID cannot be empty!")
    private String merchantID;

    private String partnerMerchantID;

    @NotNull(message = "payment type is invalid")
    private String paymentType;

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
    private FeatureFlags featureFlags;

    @NotNull(message = "items cannot be empty")
    private List<Item> items;

    private List<Campaign> campaigns;

    private List<Promo> promos;

    @NotNull(message = "price cannot be empty!")
    private Price price;

    private Receiver receiver;

    private OrderReadyEstimation orderReadyEstimation;

    private String membershipID;

    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Item{
        @NotNull(message = "ID cannot be empty!")
        private String id;

        @NotNull(message = "grab item ID cannot be empty")
        private String grabItemID;

        @NotNull(message = "quantity cannot be empty!")
        private Integer quantity;

        @NotNull(message = "price cannot be empty!")
        private Integer price;

        private Integer tax;

        private String specifications;

        private OutOfStockInstruction outOfStockInstruction;

        private List<Modifier> modifiers;

        @Getter
        @Setter
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class Modifier{
            private String id;

            private Integer price;

            private Integer tax;

            private Integer quantity;
        }
    }

    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Campaign{
        private String id;

        private String name;

        private String campaignNameForMex;

        private String level;

        private String type;

        private Integer usageCount;

        private Integer mexFundedRatio;

        private Integer deductedAmount;

        private String deductedPart;

        private List<String> appliedItemIDs;

        private FreeItem freeItem;

        @Getter
        @Setter
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class FreeItem{
            private String id;

            private String name;

            private Integer quantity;

            private Integer price;
        }
    }

    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Promo{
        private String code;

        private String description;

        private String name;

        private Integer promoAmount;

        private Integer mexFundedRatio;

        private Integer mexFundedAmount;

        private Integer targetedPrice;

        private Integer promoAmountInMin;
    }

    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Price{
        @NotNull(message = "subtotal cannot be empty!")
        private Integer subtotal;

        private Integer tax;

        private Integer merchantChargeFee;

        private Integer grabFundPromo;

        private Integer merchantFundPromo;

        private Integer basketPromo;

        private Integer deliveryFee;

        private Integer smallOrderFee;

        private Integer eaterPayment;

        private Integer total;

        private MerchantEarning merchantEarning;

        @Getter
        @Setter
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class MerchantEarning {
            private Integer revenue;

            private Integer netEarning;

            private Integer mexFundDiscount;

            private Integer commission;
        }
    }

    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class DineIn{
        private String tableId;

        private Integer eaterCount;
    }

    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Receiver{
        private String name;

        private String phones;

        private Address address;

        private VirtualContact virtualContact;

        @Getter
        @Setter
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class Address{
            private String unitNumber;

            private String deliveryInstruction;

            @JsonInclude(JsonInclude.Include.NON_NULL)
            private String poiSource;

            private String poiID;

            private String address;

            private String postcode;

            private Coordinates coordinates;

            @Getter
            @Setter
            @JsonInclude(JsonInclude.Include.NON_NULL)
            public static class Coordinates{
                private Double latitude;

                private Double longitude;
            }
        }

        @Getter
        @Setter
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class VirtualContact{
            private String virtualPhoneNumber;

            private String PIN;

            private String expiredAt;

            @JsonInclude(JsonInclude.Include.NON_NULL)
            private String status;
        }
    }

    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class OrderReadyEstimation{
        private boolean allowChange;

        private Instant estimatedOrderReadyTime;

        private Instant maxOrderReadyTime;

        private Instant newOrderReadyTime;
    }

    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Discount{
        private String code;

        private String id;

        private String name;

        private Integer deductAmountInMin;

        private String level;

        private String type;

        private Integer mexFundedAmountInMin;

        private List<String> appliedItemIDs;
    }

    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Payment{
        private String method;

        private String fundingType;

        private Integer amountInMin;
    }
}
