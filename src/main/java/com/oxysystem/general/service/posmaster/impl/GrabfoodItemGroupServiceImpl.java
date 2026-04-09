package com.oxysystem.general.service.posmaster.impl;

import com.oxysystem.general.dto.posmaster.grabFoodGroup.data.GrabFoodItemGroupDTO;
import com.oxysystem.general.dto.posmaster.grabFoodGroup.view.GrabFoodItemGroupViewDTO;
import com.oxysystem.general.dto.posmaster.grabFoodGroup.view.GrabfoodItemGroupForSelectDTO;
import com.oxysystem.general.exception.ResourceNotFoundException;
import com.oxysystem.general.model.db1.posmaster.GrabfoodItemGroup;
import com.oxysystem.general.repository.db1.posmaster.GrabfoodItemGroupRepository;
import com.oxysystem.general.response.SuccessResponse;
import com.oxysystem.general.service.posmaster.GrabfoodItemGroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GrabfoodItemGroupServiceImpl implements GrabfoodItemGroupService {
    private final GrabfoodItemGroupRepository grabfoodItemGroupRepository;

    public GrabfoodItemGroupServiceImpl(GrabfoodItemGroupRepository grabfoodItemGroupRepository) {
        this.grabfoodItemGroupRepository = grabfoodItemGroupRepository;
    }

    @Override
    public ResponseEntity<?> update(Long id, GrabFoodItemGroupDTO body) {
        GrabfoodItemGroup grabfoodItemGroup = grabfoodItemGroupRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("grab category not found!"));
        grabfoodItemGroup.setSequence(body.getSequence());
        grabfoodItemGroupRepository.save(grabfoodItemGroup);

        SuccessResponse<String> response = new SuccessResponse<>("success", String.valueOf(id));
        return ResponseEntity.ok(response);
    }


    @Override
    public ResponseEntity<?> getGrabFoodItemGroups(String name) {
        SuccessResponse<List<GrabFoodItemGroupViewDTO>> response = new SuccessResponse<>("success", grabfoodItemGroupRepository.getGrabFoodItemGroups(name));
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> getGrabfoodItemGroupForSelectForApi() {
        List<GrabfoodItemGroup> itemGroups = grabfoodItemGroupRepository.findAll();

        itemGroups.sort(Comparator.comparing(GrabfoodItemGroup::getName));
        List<GrabfoodItemGroupForSelectDTO> itemGroupsForSelect = new ArrayList<>();

        for(GrabfoodItemGroup grabfoodItemGroup: itemGroups){
            GrabfoodItemGroupForSelectDTO content = new GrabfoodItemGroupForSelectDTO();
            content.setId(grabfoodItemGroup.getId().toString());
            content.setName(grabfoodItemGroup.getName());

            itemGroupsForSelect.add(content);
        }

        SuccessResponse<Object> response = new SuccessResponse<>("success",itemGroupsForSelect);
        return ResponseEntity.ok(response);
    }

    @Override
    public Optional<GrabfoodItemGroup> findById(Long id) {
        return grabfoodItemGroupRepository.findById(id);
    }

    @Override
    public List<GrabfoodItemGroup> findAllGrabFoodItemGroupByIds(List<Long> ids) {
        return grabfoodItemGroupRepository.findAllGrabFoodItemGroupByIds(ids);
    }
}
