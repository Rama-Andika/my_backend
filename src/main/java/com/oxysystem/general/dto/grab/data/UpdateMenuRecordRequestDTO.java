package com.oxysystem.general.dto.grab.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.oxysystem.general.enums.grab.AvailableStatus;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateMenuRecordRequestDTO {
    @NotNull(message = "merchant ID cannot be empty!")
    private String merchantID;

    @NotNull(message = "field cannot be empty!")
    private String field;

    @NotNull(message = "id cannot be empty!")
    private String id;

    private Integer price;

    private String name;

    private Boolean isFree;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private AvailableStatus availableStatus;

    private Integer maxStock;

    private List<Map<String, Object>> advancedPricings;

    private List<Map<String, Object>> purchasabilities;
}
