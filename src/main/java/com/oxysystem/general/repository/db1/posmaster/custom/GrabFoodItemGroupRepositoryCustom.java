package com.oxysystem.general.repository.db1.posmaster.custom;

import com.oxysystem.general.dto.posmaster.grabFoodGroup.view.GrabFoodItemGroupViewDTO;

import java.util.List;

public interface GrabFoodItemGroupRepositoryCustom {
    List<GrabFoodItemGroupViewDTO> getGrabFoodItemGroups(String name);
}
