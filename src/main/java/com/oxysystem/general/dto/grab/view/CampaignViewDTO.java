package com.oxysystem.general.dto.grab.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class CampaignViewDTO {
    private List<CampaignDetail> ongoing;
    private List<CampaignDetail> upcoming;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class CampaignDetail {
        private String id;
        private String createdBy;
        private String merchantID;
        private String name;
        private Quota quotas;
        private Condition conditions;
        private Discount discount;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Quota{
        private Integer totalCount = 0;
        private Integer totalCountPerUser = 0;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Condition{
        private String startTime;
        private String endTime;
        private String eaterType;
        private Double minBasketAmount = 0.0;
        private Integer bundleQuantity = 0;
        private Map<String, List<PeriodDTO>> workingHour;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Discount{
        private String type;
        private Double cap = 0.0;
        private Double value = 0.0;
        private Scope scope;

        @Getter
        @Setter
        @NoArgsConstructor
        public static class Scope{
            private String type;
            private List<String> objectIDs;
        }
    }
}
