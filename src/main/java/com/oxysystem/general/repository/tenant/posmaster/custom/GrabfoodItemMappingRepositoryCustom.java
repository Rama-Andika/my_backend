package com.oxysystem.general.repository.tenant.posmaster.custom;

import com.oxysystem.general.dto.posmaster.grabFoodItemMapping.view.GrabfoodItemMappingViewDTO;

import java.util.List;

public interface GrabfoodItemMappingRepositoryCustom {
    List<GrabfoodItemMappingViewDTO> getGrabfoodItemMapping(String golPrice, Long locationId);
}
