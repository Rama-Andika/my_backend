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
public class BatchUpdateMenuRequestDTO {
    @NotNull(message = "merchant ID cannot be empty!")
    private String merchantID;

    @NotNull(message = "field cannot be empty!")
    private String field;

    private List<MenuEntity> menuEntities;


    @Getter
    @Setter
    public static class MenuEntity{
        @NotNull(message = "id cannot be empty!")
        private String id;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Integer price;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private AvailableStatus availableStatus;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Integer maxStock;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private List<Map<String, Object>> advancedPricings;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private List<Map<String, Object>> purchasabilities;
    }
}
