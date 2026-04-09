package com.oxysystem.general.service.transaction;

import org.springframework.http.ResponseEntity;

public interface PromotionService {
    ResponseEntity<?> getPromotions(String number, String startDate, String endDate, Integer type, Integer subType, String description, int page, int size);
}
