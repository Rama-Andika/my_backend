package com.oxysystem.general.service.posmaster.impl;

import com.oxysystem.general.dto.posmaster.grabPackaging.data.GrabPackagingItemDTO;
import com.oxysystem.general.enums.grab.Product;
import com.oxysystem.general.exception.ResourceNotFoundException;
import com.oxysystem.general.model.tenant.general.Location;
import com.oxysystem.general.model.tenant.posmaster.GrabPackagingItem;
import com.oxysystem.general.model.tenant.posmaster.ItemMaster;
import com.oxysystem.general.repository.tenant.posmaster.GrabPackagingItemRepository;
import com.oxysystem.general.response.SuccessResponse;
import com.oxysystem.general.service.general.LocationService;
import com.oxysystem.general.service.posmaster.GrabPackagingItemService;
import com.oxysystem.general.service.posmaster.ItemMasterService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

@Service
public class GrabPackagingItemServiceImpl implements GrabPackagingItemService {
    private final GrabPackagingItemRepository grabPackagingItemRepository;
    private final ItemMasterService itemMasterService;
    private final LocationService locationService;

    public GrabPackagingItemServiceImpl(GrabPackagingItemRepository grabPackagingItemRepository, ItemMasterService itemMasterService, LocationService locationService) {
        this.grabPackagingItemRepository = grabPackagingItemRepository;
        this.itemMasterService = itemMasterService;
        this.locationService = locationService;
    }

    @Override
    @Transactional( rollbackFor = {Throwable.class})
    public ResponseEntity<Object> save(GrabPackagingItemDTO body) {
        GrabPackagingItem grabPackagingItem = new GrabPackagingItem();

        ItemMaster itemMaster = itemMasterService.findItemMasterById(body.getItemMasterId()).orElseThrow(() -> new ResourceNotFoundException("Packaging item not found"));
        Location location = locationService.findById(body.getLocationId()).orElseThrow(() -> new ResourceNotFoundException("Packaging location not found"));
        grabPackagingItem.setProduct(Product.valueOf(body.getProduct()));
        grabPackagingItem.setItemMaster(itemMaster);
        grabPackagingItem.setLocation(location);
        grabPackagingItem =  grabPackagingItemRepository.save(grabPackagingItem);

        SuccessResponse<Object> response = new SuccessResponse<>("success", String.valueOf(grabPackagingItem.getId()));
        return ResponseEntity.ok(response);
    }

    @Override
    @Transactional( rollbackFor = {Throwable.class})
    public ResponseEntity<Object> update(GrabPackagingItemDTO body, Long id) {
        GrabPackagingItem grabPackagingItem = grabPackagingItemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Packaging item not found"));

        ItemMaster itemMaster = itemMasterService.findItemMasterById(body.getItemMasterId()).orElseThrow(() -> new ResourceNotFoundException("Packaging item not found"));
        Location location = locationService.findById(body.getLocationId()).orElseThrow(() -> new ResourceNotFoundException("Packaging location not found"));
        grabPackagingItem.setProduct(Product.valueOf(body.getProduct()));
        grabPackagingItem.setItemMaster(itemMaster);
        grabPackagingItem.setLocation(location);
        grabPackagingItem =  grabPackagingItemRepository.save(grabPackagingItem);

        SuccessResponse<Object> response = new SuccessResponse<>("success", String.valueOf(grabPackagingItem.getId()));
        return ResponseEntity.ok(response);
    }

    @Override
    public Optional<GrabPackagingItem> findGrabPackagingItemByLocationIdAndProduct(Long locationId, String product) {
        return grabPackagingItemRepository.findGrabPackagingItemByLocationIdAndProduct(locationId, product);
    }
}
