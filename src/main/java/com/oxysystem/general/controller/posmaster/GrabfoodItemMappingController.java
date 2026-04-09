package com.oxysystem.general.controller.posmaster;

import com.oxysystem.general.dto.posmaster.grabFoodItemMapping.data.GrabFoodItemByItemMasterDTO;
import com.oxysystem.general.service.posmaster.GrabfoodItemMappingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class GrabfoodItemMappingController {
    private final GrabfoodItemMappingService grabfoodItemMappingService;

    public GrabfoodItemMappingController(GrabfoodItemMappingService grabfoodItemMappingService) {
        this.grabfoodItemMappingService = grabfoodItemMappingService;
    }

    @GetMapping("/grabfood-item-mapping/by-item-master/{itemId}")
    public ResponseEntity<?> findGrabmartItemMappingByItemMasterId(@PathVariable Long itemId){
        return grabfoodItemMappingService.findGrabfoodItemMappingByItemMasterId(itemId);
    }

    @PostMapping("/grabfood-item-mapping")
    public ResponseEntity<?> saveGrabmartItem(@RequestBody GrabFoodItemByItemMasterDTO grabItemByItemMasterDTO){
        return grabfoodItemMappingService.saveGrabfoodItem(grabItemByItemMasterDTO);
    }
}
