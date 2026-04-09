package com.oxysystem.general.service.posmaster;

import com.oxysystem.general.dto.posmaster.grabFoodItemMapping.data.GrabFoodItemByItemMasterDTO;
import com.oxysystem.general.dto.posmaster.grabFoodItemMapping.view.GrabfoodItemMappingViewDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface GrabfoodItemMappingService {
    List<GrabfoodItemMappingViewDTO> getListGrabfoodItemMapping(String golPrice, Long locationId);
    ResponseEntity<?> saveGrabfoodItem(GrabFoodItemByItemMasterDTO grabFoodItemByItemMasterDTO);
    ResponseEntity<?> findGrabfoodItemMappingByItemMasterId(Long id);
}
