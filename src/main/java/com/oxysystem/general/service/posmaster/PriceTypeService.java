package com.oxysystem.general.service.posmaster;

import com.oxysystem.general.dto.posmaster.priceType.data.PriceTypeDTO;
import org.springframework.http.ResponseEntity;

public interface PriceTypeService {
    ResponseEntity<?> getPriceByItemMaster(PriceTypeDTO body);
}
