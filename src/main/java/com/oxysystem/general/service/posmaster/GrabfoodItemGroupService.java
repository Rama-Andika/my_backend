package com.oxysystem.general.service.posmaster;

import com.oxysystem.general.dto.posmaster.grabFoodGroup.data.GrabFoodItemGroupDTO;
import com.oxysystem.general.model.db1.posmaster.GrabfoodItemGroup;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface GrabfoodItemGroupService {
    ResponseEntity<?> update(Long id, GrabFoodItemGroupDTO body);
    ResponseEntity<?> getGrabFoodItemGroups(String name);
    ResponseEntity<?> getGrabfoodItemGroupForSelectForApi();
    Optional<GrabfoodItemGroup> findById(Long id);
    List<GrabfoodItemGroup> findAllGrabFoodItemGroupByIds(List<Long> ids);
}
