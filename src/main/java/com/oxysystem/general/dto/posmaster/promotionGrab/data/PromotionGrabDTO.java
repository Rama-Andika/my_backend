package com.oxysystem.general.dto.posmaster.promotionGrab.data;
import com.oxysystem.general.enums.DocumentStatus;
import com.oxysystem.general.validation.annotation.ValidEnum;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PromotionGrabDTO {
    @NotNull(message = "user cannot be empty!")
    private Long userId;

    @NotBlank(message = "name cannot be empty!")
    private String name;

    @NotNull(message = "start time cannot be empty!")
    private LocalDateTime startTime;

    @NotNull(message = "end time cannot be empty!")
    private LocalDateTime endTime;

    @NotBlank(message = "eater type cannot be empty!")
    private String eaterType;
    private Double minBasketAmount = 0.0;
    private Integer bundleQuantity = 0;
    private Integer totalCount = 0;
    private Integer totalCountPerUser = 0;

    @NotBlank(message = "promotion type cannot be empty!")
    private String type;
    private Double cap = 0.0;
    private Double value = 0.0;

    @NotBlank(message = "scope type cannot be empty!")
    private String scopeType;
    private String customTag;
    private String createdBy;

    @ValidEnum(message = "status is not valid!", enumClass = DocumentStatus.class)
    private String status;

    private String product;

    private List<PromotionGrabDetailDTO> promotionGrabDetailDTOS;
    private List<PromotionGrabLocationDTO> promotionGrabLocationDTOS;

    @Getter
    @Setter
    public static class PromotionGrabDetailDTO {
        private String id;
        private String objectId;
    }

    @Getter
    @Setter
    public static class PromotionGrabLocationDTO {
        private String id;
        private Long locationId;
    }
}
