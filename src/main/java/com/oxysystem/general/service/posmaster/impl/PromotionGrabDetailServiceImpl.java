package com.oxysystem.general.service.posmaster.impl;

import com.oxysystem.general.model.db1.posmaster.PromotionGrabDetail;
import com.oxysystem.general.repository.db1.posmaster.PromotionGrabDetailRepository;
import com.oxysystem.general.service.posmaster.PromotionGrabDetailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PromotionGrabDetailServiceImpl implements PromotionGrabDetailService {
    private final PromotionGrabDetailRepository promotionGrabDetailRepository;

    public PromotionGrabDetailServiceImpl(PromotionGrabDetailRepository promotionGrabDetailRepository) {
        this.promotionGrabDetailRepository = promotionGrabDetailRepository;
    }

    @Override
    public Optional<PromotionGrabDetail> findById(Long id) {
        return promotionGrabDetailRepository.findById(id);
    }

    @Override
    public void delete(PromotionGrabDetail promotionGrabDetail) {
        promotionGrabDetailRepository.delete(promotionGrabDetail);
    }

    @Override
    @Transactional()
    public void deletePromotionGrabDetailByPromotionGrabId(Long promotionGrabId) {
        promotionGrabDetailRepository.deletePromotionGrabDetailByPromotionGrabId(promotionGrabId);
    }
}
