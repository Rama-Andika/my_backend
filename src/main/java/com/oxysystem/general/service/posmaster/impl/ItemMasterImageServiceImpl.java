package com.oxysystem.general.service.posmaster.impl;

import com.oxysystem.general.model.tenant.posmaster.ItemMasterImage;
import com.oxysystem.general.repository.tenant.posmaster.ItemMasterImageRepository;
import com.oxysystem.general.service.posmaster.ItemMasterImageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemMasterImageServiceImpl implements ItemMasterImageService {
    private final ItemMasterImageRepository itemMasterImageRepository;

    public ItemMasterImageServiceImpl(ItemMasterImageRepository itemMasterImageRepository) {
        this.itemMasterImageRepository = itemMasterImageRepository;
    }

    @Override
    public List<ItemMasterImage> getImagesByItems(List<Long> itemMasterIds) {
        return itemMasterImageRepository.getImagesByItems(itemMasterIds);
    }
}
