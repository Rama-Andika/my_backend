package com.oxysystem.general.controller.transaction;

import com.oxysystem.general.service.transaction.PromotionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class PromotionController {
    private final PromotionService promotionService;

    public PromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @GetMapping("/promotions")
    public ResponseEntity<?> getPromotions(String number, String startDate, String endDate, Integer type, Integer subType, String description,
                                           @RequestParam(required = false, defaultValue = "0") Integer page,
                                           @RequestParam(required = false, defaultValue = "25") Integer size) {
        return promotionService.getPromotions(number, startDate, endDate, type, subType, description, page, size);
    }
}
