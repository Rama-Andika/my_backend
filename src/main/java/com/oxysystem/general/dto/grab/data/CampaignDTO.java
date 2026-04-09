package com.oxysystem.general.dto.grab.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.oxysystem.general.dto.grab.view.PeriodDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CampaignDTO {
    private String merchantID;
    private String name;
    private Quota quotas;
    private Condition conditions;
    private Discount discount;
    private String customTag;


    @Getter
    @Setter
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Quota{
        private Integer totalCount = 0;
        private Integer totalCountPerUser = 0;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Condition{
        private String startTime;
        private String endTime;
        private String eaterType;
        private Double minBasketAmount = 0.0;
        private Integer bundleQuantity = 0;
        private Map<String, DayPeriodDTO> workingHour;


        @Getter
        @Setter
        @NoArgsConstructor
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class DayPeriodDTO{
            List<PeriodDTO> periods;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Discount{
        private String type;
        private Double cap = 0.0;
        private Double value = 0.0;
        private Scope scope;

        @Getter
        @Setter
        @NoArgsConstructor
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class Scope{
            private String type;
            private List<String> objectIDs;
        }
    }
}
