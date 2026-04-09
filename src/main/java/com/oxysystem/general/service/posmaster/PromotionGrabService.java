package com.oxysystem.general.service.posmaster;

import com.oxysystem.general.dto.posmaster.promotionGrab.data.PromotionGrabDTO;
import org.springframework.http.ResponseEntity;

public interface PromotionGrabService {
    ResponseEntity<?> save(PromotionGrabDTO promotionGrabDTO);
    ResponseEntity<?> update(Long id, PromotionGrabDTO promotionGrabDTO);
    ResponseEntity<?> findByIdForApi(Long id);
    ResponseEntity<?> getPromotionsGrab(Long locationId, String name, String product, int page, int size);
    ResponseEntity<?> deletePromotionGrabDetail(Long detailId);
    ResponseEntity<?> terminatePromotionGrab(Long id);
}
