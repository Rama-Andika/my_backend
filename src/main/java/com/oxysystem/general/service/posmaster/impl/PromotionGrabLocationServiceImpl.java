package com.oxysystem.general.service.posmaster.impl;

import com.oxysystem.general.model.tenant.posmaster.PromotionGrabLocation;
import com.oxysystem.general.repository.tenant.posmaster.PromotionGrabLocationRepository;
import com.oxysystem.general.service.posmaster.PromotionGrabLocationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PromotionGrabLocationServiceImpl implements PromotionGrabLocationService {
    private final PromotionGrabLocationRepository promotionGrabLocationRepository;


    public PromotionGrabLocationServiceImpl(PromotionGrabLocationRepository promotionGrabLocationRepository) {
        this.promotionGrabLocationRepository = promotionGrabLocationRepository;
    }

    @Override
    public PromotionGrabLocation save(PromotionGrabLocation promotionGrabLocation) {
        return promotionGrabLocationRepository.save(promotionGrabLocation);
    }

    @Override
    public void saveAll(List<PromotionGrabLocation> promotionGrabLocationList) {
        promotionGrabLocationRepository.saveAll(promotionGrabLocationList);
    }

    @Override
    public Optional<PromotionGrabLocation> findById(Long id) {
        return promotionGrabLocationRepository.findById(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deletePromotionGrabLocationByPromotionGrabId(Long promotionGrabId) {
        promotionGrabLocationRepository.deletePromotionGrabLocationByPromotionGrabId(promotionGrabId);
    }


}
