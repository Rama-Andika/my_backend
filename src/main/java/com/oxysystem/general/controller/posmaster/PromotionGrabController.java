package com.oxysystem.general.controller.posmaster;

import com.oxysystem.general.dto.posmaster.promotionGrab.data.PromotionGrabDTO;
import com.oxysystem.general.service.posmaster.PromotionGrabService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class PromotionGrabController {
    private final PromotionGrabService promotionGrabService;

    public PromotionGrabController(PromotionGrabService promotionGrabService) {
        this.promotionGrabService = promotionGrabService;
    }

    @PostMapping("/promotion-grab")
    public ResponseEntity<?> save(@Valid @RequestBody PromotionGrabDTO promotionGrabDTO){
        return promotionGrabService.save(promotionGrabDTO);
    }

    @PutMapping("/promotion-grab/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody PromotionGrabDTO promotionGrabDTO){
        return promotionGrabService.update(id, promotionGrabDTO);
    }

    @GetMapping("/promotions-grab")
    public ResponseEntity<?> getPromotionsGrab(Long locationId, String name, String product,
                                               @RequestParam(required = false, defaultValue = "0") int page,
                                               @RequestParam(required = false, defaultValue = "20") int size){
        return promotionGrabService.getPromotionsGrab(locationId, name, product, page, size);
    }

    @GetMapping("/promotion-grab/{id}")
    public ResponseEntity<?> findByIdForApi(@PathVariable Long id){
        return promotionGrabService.findByIdForApi(id);
    }

    @DeleteMapping("/promotion-grab-detail/{id}")
    public ResponseEntity<?> deletePromotionGrabDetail(@PathVariable Long id){
        return promotionGrabService.deletePromotionGrabDetail(id);
    }

    @PutMapping("/promotion-grab/terminated/{id}")
    public ResponseEntity<?> deletePromotionGrab(@PathVariable Long id){
        return promotionGrabService.terminatePromotionGrab(id);
    }
}
