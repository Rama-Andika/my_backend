package com.oxysystem.general.dto.grab.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GrabMenuDTO {
    private String merchantID;

    private String partnerMerchantID;

    private CurrencyDTO currency;

    private List<SellingTimeDTO> sellingTimes;

    private List<CategoryDTO> categories;
}
