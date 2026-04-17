package com.oxysystem.general.repository.tenant.posmaster.custom;

import com.oxysystem.general.dto.posmaster.grabFoodGroup.view.GrabFoodItemGroupViewDTO;

import java.util.List;

public interface GrabFoodItemGroupRepositoryCustom {
    List<GrabFoodItemGroupViewDTO> getGrabFoodItemGroups(String name);
}
