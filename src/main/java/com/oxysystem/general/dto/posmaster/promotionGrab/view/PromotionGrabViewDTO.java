package com.oxysystem.general.dto.posmaster.promotionGrab.view;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PromotionGrabViewDTO {
    private String id;
    private String name;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    private String eaterType;
    private Double minBasketAmount = 0.0;
    private Integer bundleQuantity = 0;
    private Integer totalCount = 0;
    private Integer totalCountPerUser = 0;
    private String type;
    private Double cap = 0.0;
    private Double value = 0.0;
    private String scopeType;
    private String customTag;
    private String createdBy;
    private String status;
    private String product;

    private List<PromotionGrabDetailViewDTO> promotionGrabDetails;
    private List<PromotionGrabLocationViewDTO> promotionGrabLocations;

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PromotionGrabDetailViewDTO {
        private String id;
        private String itemId;
        private String itemName;
        private String itemBarcode;
        private String grabMartItemGroupId;
        private String grabMartItem;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PromotionGrabLocationViewDTO{
        private String id;
        private String locationId;
        private String locationName;
        private boolean checked;
    }

    public PromotionGrabViewDTO(String id, String name, LocalDateTime startTime, LocalDateTime endTime, String type, String status, String product) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.type = type;
        this.status = status;
        this.product = product;
    }
}
