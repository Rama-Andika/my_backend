package com.oxysystem.general.controller.posmaster;

import com.oxysystem.general.dto.posmaster.priceType.data.PriceTypeDTO;
import com.oxysystem.general.service.posmaster.PriceTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class PriceTypeController {
    private final PriceTypeService priceTypeService;

    public PriceTypeController(PriceTypeService priceTypeService) {
        this.priceTypeService = priceTypeService;
    }

    @PostMapping("/item-master/price")
    public ResponseEntity<?> getPriceByItemMaster(@RequestBody PriceTypeDTO body) {
        return priceTypeService.getPriceByItemMaster(body);
    }
}
