package com.oxysystem.general.mapper.general;

import com.oxysystem.general.dto.general.strukKasir.data.StrukKasirDTO;
import com.oxysystem.general.model.db1.posmaster.StrukKasir;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class StrukKasirMapper {
    public StrukKasir toEntity(StrukKasir strukKasir, StrukKasirDTO body){
        if(strukKasir == null) strukKasir = new StrukKasir();
        strukKasir.setLocationId(body.getLocationId());
        strukKasir.setDate(LocalDate.now());
        strukKasir.setUserId(body.getUserId());
        strukKasir.setGrabMerchantId(body.getGrabMerchantId());
        strukKasir.setGrabFoodMerchantId(body.getGrabFoodMerchantId());

        return strukKasir;
    }
}
