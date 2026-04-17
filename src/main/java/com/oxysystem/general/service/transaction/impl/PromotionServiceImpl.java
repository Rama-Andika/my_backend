package com.oxysystem.general.service.transaction.impl;

import com.oxysystem.general.dto.transaction.promotion.view.PromotionViewDto;
import com.oxysystem.general.repository.tenant.transaction.PromotionRepository;
import com.oxysystem.general.response.PaginationResponse;
import com.oxysystem.general.response.SuccessPaginationResponse;
import com.oxysystem.general.service.transaction.PromotionService;
import com.oxysystem.general.util.ResponseUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PromotionServiceImpl implements PromotionService {
    private final PromotionRepository promotionRepository;

    public PromotionServiceImpl(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    @Override
    public ResponseEntity<?> getPromotions(String number, String startDate, String endDate, Integer type, Integer subType, String description, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PromotionViewDto> result = promotionRepository.getPromotions(number, startDate, endDate, type, subType, description, pageable);
        PaginationResponse paginationResponse = ResponseUtils.createPaginationResponse(result);

        SuccessPaginationResponse<?> response = new SuccessPaginationResponse<>("success", paginationResponse, result.getContent());
        return ResponseEntity.ok(response);
    }
}
