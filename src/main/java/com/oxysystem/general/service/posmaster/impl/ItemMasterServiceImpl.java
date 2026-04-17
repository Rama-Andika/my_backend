package com.oxysystem.general.service.posmaster.impl;

import com.oxysystem.general.dto.posmaster.itemMaster.view.ItemMasterView2DTO;
import com.oxysystem.general.dto.posmaster.itemMaster.view.ItemMasterViewDTO;
import com.oxysystem.general.exception.ResourceNotFoundException;
import com.oxysystem.general.model.tenant.general.Location;
import com.oxysystem.general.model.tenant.posmaster.ItemMaster;
import com.oxysystem.general.model.tenant.system.SystemMain;
import com.oxysystem.general.repository.tenant.posmaster.ItemMasterRepository;
import com.oxysystem.general.response.PaginationResponse;
import com.oxysystem.general.response.SuccessPaginationResponse;
import com.oxysystem.general.response.SuccessResponse;
import com.oxysystem.general.service.general.LocationService;
import com.oxysystem.general.service.posmaster.ItemMasterService;
import com.oxysystem.general.service.system.SystemMainService;
import com.oxysystem.general.util.ResponseUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemMasterServiceImpl implements ItemMasterService {
    private final ItemMasterRepository itemMasterRepository;
    private final SystemMainService  systemMainService;
    private final LocationService locationService;

    public ItemMasterServiceImpl(ItemMasterRepository itemMasterRepository, SystemMainService systemMainService, LocationService locationService) {
        this.itemMasterRepository = itemMasterRepository;
        this.systemMainService = systemMainService;
        this.locationService = locationService;
    }

    @Override
    public ResponseEntity<?> getItemMastersForApi(String name, String barcode, String code, Long itemGroupId, Long itemCategoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ItemMasterViewDTO> itemMasterPage = itemMasterRepository.getItemMasters(name, barcode, code, itemGroupId, itemCategoryId, pageable);

        PaginationResponse paginationResponse = ResponseUtils.createPaginationResponse(itemMasterPage);
        SuccessPaginationResponse<List<ItemMasterViewDTO>> response = new SuccessPaginationResponse<>("success", paginationResponse, itemMasterPage.getContent());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> getItemMastersWithPrice(String name, String barcode, String code, Long itemGroupId, Long itemCategoryId, Long locationId, int page, int size) {
        Location location = locationService.findById(locationId).orElseThrow(() -> new ResourceNotFoundException("Location not found"));
        Pageable pageable = PageRequest.of(page, size);
        Page<ItemMasterViewDTO> itemMasterPage = itemMasterRepository.getItemMastersWithPrice(name, barcode, code, itemGroupId, itemCategoryId, location.getGolPrice(), pageable);

        PaginationResponse paginationResponse = ResponseUtils.createPaginationResponse(itemMasterPage);
        SuccessPaginationResponse<List<ItemMasterViewDTO>> response = new SuccessPaginationResponse<>("success", paginationResponse, itemMasterPage.getContent());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> getItemMastersByLocation(String name, String barcode, String code, Long locationId, String category, boolean availability, int page, int size) {
        Location location = locationService.findById(locationId).orElseThrow(() -> new ResourceNotFoundException("Location not found"));
        SystemMain systemMainImgLink = systemMainService.findSystemPropertyName("IMG_LINK").orElse(null);
        String imgLink = "";
        if (systemMainImgLink != null) imgLink = systemMainImgLink.getValueprop();

        Pageable pageable = PageRequest.of(page, size);
        Page<ItemMasterView2DTO> results = itemMasterRepository.getItemMastersByLocation(name, barcode, code, imgLink, location.getGolPrice(), locationId, category, availability, pageable);

        PaginationResponse paginationResponse = ResponseUtils.createPaginationResponse(results);
        SuccessPaginationResponse<?>  response = new SuccessPaginationResponse<>("success", paginationResponse, results.getContent());
        return ResponseEntity.ok(response);
    }

    @Override
    public Page<ItemMasterViewDTO> getItemMasters(String name, String barcode, String code, Long itemGroupId, Long itemCategoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return itemMasterRepository.getItemMasters(name, barcode, code, itemGroupId, itemCategoryId, pageable);
    }

    @Override
    public Optional<ItemMaster> findItemMasterById(Long id) {
        return itemMasterRepository.findById(id);
    }

    @Override
    public List<ItemMaster> findItemMastersByItemMasterIds(List<Long> ids) {
        return itemMasterRepository.findItemMastersByItemMasterIds(ids);
    }

    @Override
    public Optional<ItemMaster> findItemMasterByCodeOrBarcode(String barcode) {
        return itemMasterRepository.findItemMasterByCodeOrBarcode(barcode);
    }

    @Override
    public List<ItemMaster> findItemMasterWithPriceTypeByIds(List<Long> itemMasterIds) {
        return itemMasterRepository.findItemMasterWithPriceTypeByIds(itemMasterIds);
    }

    @Override
    public ResponseEntity<?> getItemMastersPlayground() {
        SystemMain sysMainCategoryId = systemMainService.findSystemPropertyName("CATEGORY_ID_FOR_PLAYGROUND").orElseThrow(() -> new ResourceNotFoundException("playground item not found!"));
        String categoryId = sysMainCategoryId.getValueprop();

        List<ItemMasterViewDTO> itemMasters = itemMasterRepository.findItemMasterByItemGroup(Long.valueOf(categoryId));

        SuccessResponse<?> response = new SuccessResponse<>("success", itemMasters);
        return ResponseEntity.ok(response);
    }
}
