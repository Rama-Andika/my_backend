package com.oxysystem.general.controller.general;

import com.oxysystem.general.service.general.MerchantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class MerchantController {
    private final MerchantService merchantService;

    public MerchantController(MerchantService merchantService) {
        this.merchantService = merchantService;
    }

    @GetMapping("/merchants-by-location/for-select/{locationId}")
    public ResponseEntity<?> getMerchantsByLocationId(@PathVariable Long locationId) {
        return merchantService.getMerchantsByLocationId(locationId);
    }
}
