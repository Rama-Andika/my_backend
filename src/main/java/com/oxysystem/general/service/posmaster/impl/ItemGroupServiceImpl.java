package com.oxysystem.general.service.posmaster.impl;

import com.oxysystem.general.dto.posmaster.itemGroup.view.ItemGroupForSelectDTO;
import com.oxysystem.general.model.tenant.posmaster.ItemCategory;
import com.oxysystem.general.model.tenant.posmaster.ItemGroup;
import com.oxysystem.general.repository.tenant.posmaster.ItemGroupRepository;
import com.oxysystem.general.response.SuccessResponse;
import com.oxysystem.general.service.posmaster.ItemGroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemGroupServiceImpl implements ItemGroupService {
    private final ItemGroupRepository itemGroupRepository;

    public ItemGroupServiceImpl(ItemGroupRepository itemGroupRepository) {
        this.itemGroupRepository = itemGroupRepository;
    }

    @Override
    public ResponseEntity<?> getItemGroupForSelect() {
        List<ItemGroup> groups = itemGroupRepository.findAll();

        List<ItemGroupForSelectDTO> selectItemGroups = new ArrayList<>();
        for(ItemGroup itemGroup: groups){
            ItemGroupForSelectDTO itemGroupSelect = new ItemGroupForSelectDTO();
            itemGroupSelect.setId(itemGroup.getItemGroupId().toString());
            itemGroupSelect.setName(itemGroup.getName());
            List<ItemGroupForSelectDTO.ItemCategoryForSelectDTO> itemCategoryForSelect = new ArrayList<>();

            for(ItemCategory itemCategory: itemGroup.getItemCategories()){
                ItemGroupForSelectDTO.ItemCategoryForSelectDTO itemCategorySelect = new ItemGroupForSelectDTO.ItemCategoryForSelectDTO();
                itemCategorySelect.setId(itemCategory.getItemCategoryId().toString());
                itemCategorySelect.setName(itemCategory.getName());
                itemCategoryForSelect.add(itemCategorySelect);
            }

            itemGroupSelect.setSubCategories(itemCategoryForSelect);

            selectItemGroups.add(itemGroupSelect);
        }
        SuccessResponse<List<ItemGroupForSelectDTO>> response = new SuccessResponse<>("success", selectItemGroups);
        return ResponseEntity.ok(response);
    }
}
