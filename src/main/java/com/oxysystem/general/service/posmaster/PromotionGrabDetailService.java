package com.oxysystem.general.service.posmaster;

import com.oxysystem.general.model.db1.posmaster.PromotionGrabDetail;

import java.util.Optional;

public interface PromotionGrabDetailService {
    Optional<PromotionGrabDetail> findById(Long id);
    void delete(PromotionGrabDetail promotionGrabDetail);
    void deletePromotionGrabDetailByPromotionGrabId(Long promotionGrabId);
}
