package com.oxysystem.general.service.posmaster;

import com.oxysystem.general.model.db1.posmaster.PromotionGrabLocation;

import java.util.List;
import java.util.Optional;

public interface PromotionGrabLocationService {
    PromotionGrabLocation save(PromotionGrabLocation promotionGrabLocation);
    void saveAll(List<PromotionGrabLocation> promotionGrabLocationList);
    Optional<PromotionGrabLocation> findById(Long id);
    void deletePromotionGrabLocationByPromotionGrabId(Long promotionGrabId);
}
