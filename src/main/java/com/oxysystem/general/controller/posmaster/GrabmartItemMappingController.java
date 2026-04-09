package com.oxysystem.general.controller.posmaster;

import com.oxysystem.general.dto.posmaster.grabMartItemMapping.data.GrabMartItemByItemMasterDTO;
import com.oxysystem.general.dto.posmaster.grabMartItemMapping.data.UploadItemMasterGrabMartDTO;
import com.oxysystem.general.service.posmaster.GrabmartItemMappingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class GrabmartItemMappingController {
    private final GrabmartItemMappingService grabmartItemMappingService;

    public GrabmartItemMappingController(GrabmartItemMappingService grabmartItemMappingService) {
        this.grabmartItemMappingService = grabmartItemMappingService;
    }

    @GetMapping("/grabmart-item-mapping/by-item-master/{itemId}")
    public ResponseEntity<?> findGrabmartItemMappingByItemMasterId(@PathVariable Long itemId){
        return grabmartItemMappingService.findGrabmartItemMappingByItemMasterId(itemId);
    }

    @PostMapping("/grabmart-item-mapping")
    public ResponseEntity<?> saveGrabmartItem(@RequestBody GrabMartItemByItemMasterDTO grabItemByItemMasterDTO){
        return grabmartItemMappingService.saveGrabmartItem(grabItemByItemMasterDTO);
    }

    @PostMapping("/grabmart-upload-item/{locationId}")
    public ResponseEntity<Object> uploadItemMasterGrabMart(@PathVariable Long locationId, @RequestBody List<UploadItemMasterGrabMartDTO> data){
        return grabmartItemMappingService.uploadItemMasterGrabMart(data, locationId);
    }
}