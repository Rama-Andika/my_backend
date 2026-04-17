package com.oxysystem.general.repository.tenant.transaction.custom;

import com.oxysystem.general.dto.transaction.promotion.view.PromotionViewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PromotionRepositoryCustom {
    Page<PromotionViewDto> getPromotions(String number, String startDate, String endDate, Integer type, Integer subType, String description, Pageable pageable);
}
