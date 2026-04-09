package com.oxysystem.general.dto.grab.data;

import com.oxysystem.general.enums.grab.CampaignLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CampaignMapDTO {
    private String id;

    private String campaignLevel;

    private String campaignType;

    private Integer deductedAmount;

    private Integer mexFundedRatio;
}
