package com.oxysystem.general.repository.db1.posmaster.custom;

import com.oxysystem.general.dto.posmaster.promotionGrab.view.PromotionGrabViewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PromotionGrabRepositoryCustom {
    Page<PromotionGrabViewDTO> getPromotionsGrab(Long locationId, String name, String product, Pageable pageable);
}
