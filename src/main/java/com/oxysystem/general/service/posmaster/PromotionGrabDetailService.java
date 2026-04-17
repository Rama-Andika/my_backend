package com.oxysystem.general.service.posmaster;

import com.oxysystem.general.model.tenant.posmaster.PromotionGrabDetail;

import java.util.Optional;

public interface PromotionGrabDetailService {
    Optional<PromotionGrabDetail> findById(Long id);
    void delete(PromotionGrabDetail promotionGrabDetail);
    void deletePromotionGrabDetailByPromotionGrabId(Long promotionGrabId);
}
