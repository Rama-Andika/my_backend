package com.oxysystem.general.service.posmaster.impl;

import com.oxysystem.general.dto.general.apiAppSync.data.ApiAppSyncDTO;
import com.oxysystem.general.dto.general.location.view.LocationGrabForSelectDTO;
import com.oxysystem.general.dto.posmaster.grabMartItemMapping.data.GrabMartItemByItemMasterDTO;
import com.oxysystem.general.dto.posmaster.grabMartItemMapping.data.UploadItemMasterGrabMartDTO;
import com.oxysystem.general.dto.posmaster.grabMartItemMapping.view.GrabmartItemByItemMasterViewDTO;
import com.oxysystem.general.dto.posmaster.grabMartItemMapping.view.GrabmartItemMappingViewDTO;
import com.oxysystem.general.enums.grab.Product;
import com.oxysystem.general.exception.ResourceNotFoundException;
import com.oxysystem.general.model.tenant.general.ApiApp;
import com.oxysystem.general.model.tenant.general.Location;
import com.oxysystem.general.model.tenant.posmaster.GrabmartItemCategory;
import com.oxysystem.general.model.tenant.posmaster.GrabmartItemGroup;
import com.oxysystem.general.model.tenant.posmaster.GrabmartItemMapping;
import com.oxysystem.general.model.tenant.posmaster.ItemMaster;
import com.oxysystem.general.repository.tenant.posmaster.GrabmartItemMappingRepository;
import com.oxysystem.general.response.SuccessResponse;
import com.oxysystem.general.service.general.ApiAppService;
import com.oxysystem.general.service.general.ApiAppSyncService;
import com.oxysystem.general.service.general.LocationService;
import com.oxysystem.general.service.posmaster.GrabmartItemCategoryService;
import com.oxysystem.general.service.posmaster.GrabmartItemGroupService;
import com.oxysystem.general.service.posmaster.GrabmartItemMappingService;
import com.oxysystem.general.service.posmaster.ItemMasterService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class GrabmartItemMappingServiceImpl implements GrabmartItemMappingService {
    private final GrabmartItemMappingRepository grabmartItemMappingRepository;
    private final ItemMasterService itemMasterService;
    private final LocationService locationService;
    private final GrabmartItemGroupService grabmartItemGroupService;
    private final GrabmartItemCategoryService grabmartItemCategoryService;
    private final ApiAppService apiAppService;
    private final ApiAppSyncService apiAppSyncService;

    public GrabmartItemMappingServiceImpl(GrabmartItemMappingRepository grabmartItemMappingRepository, ItemMasterService itemMasterService, LocationService locationService, GrabmartItemGroupService grabmartItemGroupService, GrabmartItemCategoryService grabmartItemCategoryService, ApiAppService apiAppService, ApiAppSyncService apiAppSyncService) {
        this.grabmartItemMappingRepository = grabmartItemMappingRepository;
        this.itemMasterService = itemMasterService;
        this.locationService = locationService;
        this.grabmartItemGroupService = grabmartItemGroupService;
        this.grabmartItemCategoryService = grabmartItemCategoryService;
        this.apiAppService = apiAppService;
        this.apiAppSyncService = apiAppSyncService;
    }

    @Override
    public List<GrabmartItemMappingViewDTO> getListGrabmartItemMapping(String golPrice, Long locationId) {
        return grabmartItemMappingRepository.getGrabmartItemMapping(golPrice, locationId);
    }

    @Override
    @Transactional(rollbackFor = {Throwable.class})
    public ResponseEntity<?> saveGrabmartItem(GrabMartItemByItemMasterDTO grabItemByItemMasterDTO) {
        // Delete grab mart item mapping if data exists
        if(!grabmartItemMappingRepository.findGrabmartItemMappingByItemMasterId(Long.valueOf(grabItemByItemMasterDTO.getItemId())).isEmpty()){
            grabmartItemMappingRepository.deleteGrabmartItemMappingByItemMasterId(Long.valueOf(grabItemByItemMasterDTO.getItemId()));
        }

        ItemMaster itemMaster = itemMasterService.findItemMasterById(Long.valueOf(grabItemByItemMasterDTO.getItemId())).orElseThrow(() -> new ResourceNotFoundException("item not found!"));
        GrabmartItemGroup itemGroup = grabmartItemGroupService.findById(Long.valueOf(grabItemByItemMasterDTO.getGrabItemGroupId())).orElseThrow(() -> new ResourceNotFoundException("grab mart item group not found!"));
        GrabmartItemCategory itemCategory = grabmartItemCategoryService.findById(Long.valueOf(grabItemByItemMasterDTO.getGrabItemCategoryId())).orElseThrow(() -> new ResourceNotFoundException("grab mart item category not found!"));

        List<Long> locationIDs = grabItemByItemMasterDTO.getLocations().stream()
                .map(l -> Long.valueOf(l.getId()))
                .collect(Collectors.toList());

        List<Location> locations = locationService.findAllByIDs(locationIDs);

        if (locations.isEmpty()) throw new ResourceNotFoundException("select at least one location!");

        List<GrabmartItemMapping> itemMappings = new ArrayList<>();

        for(Location location: locations){
            GrabmartItemMapping mapping = new GrabmartItemMapping();
            mapping.setItemName(itemMaster.getName());
            mapping.setItemMaster(itemMaster);
            mapping.setGrabmartUnit(grabItemByItemMasterDTO.getGrabUnit());
            mapping.setValue(grabItemByItemMasterDTO.getValue());
            mapping.setGrabmartItemGroup(itemGroup);
            mapping.setGrabmartItemCategory(itemCategory);
            mapping.setIsPublished(grabItemByItemMasterDTO.getIsPublished());
            mapping.setLocation(location);
            mapping.setSpecialType(grabItemByItemMasterDTO.getSpecialType());
            itemMappings.add(mapping);
        }

        grabmartItemMappingRepository.saveAll(itemMappings);

        // find grab api app
        ApiApp apiApp = apiAppService.findApiAppByName(Product.GRAB_MART.name()).orElse(null);

        List<ApiAppSyncDTO> apiAppSyncDTOS = new ArrayList<>();
        if(apiApp != null){
            for(Location location: locations){
                ApiAppSyncDTO apiAppSyncDTO = new ApiAppSyncDTO();
                apiApp.getApiAppSyncSetups().stream().filter(a -> a.getStatus() == 1 && a.getTableName().equalsIgnoreCase("pos_item_master")).findFirst().ifPresent(apiAppSyncSetup -> {
                    apiAppSyncDTO.setTableName("pos_item_master");
                    apiAppSyncDTO.setAction("update");
                    apiAppSyncDTO.setOwnerId(itemMaster.getItemMasterId());
                    apiAppSyncDTO.setLocation(location);

                    apiAppSyncDTOS.add(apiAppSyncDTO);
                });
            }

            try{
                apiAppSyncService.batchSaveApiAppSync(apiAppSyncDTOS, apiApp);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }

        SuccessResponse<String> response = new SuccessResponse<>("success",null);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> findGrabmartItemMappingByItemMasterId(Long id) {
        GrabmartItemByItemMasterViewDTO content = new GrabmartItemByItemMasterViewDTO();

        ItemMaster itemMaster = itemMasterService.findItemMasterById(id).orElseThrow(() -> new ResourceNotFoundException("item not found!"));
        content.setItemId(String.valueOf(itemMaster.getItemMasterId()));
        content.setItemName(itemMaster.getName());
        content.setBarcode(itemMaster.getBarcode());
        content.setCode(itemMaster.getCode());

        List<GrabmartItemMapping> grabmartItemMappings = grabmartItemMappingRepository.findGrabmartItemMappingByItemMasterId(itemMaster.getItemMasterId());

        GrabmartItemMapping grabmartItemMapping = new GrabmartItemMapping();
        if(!grabmartItemMappings.isEmpty()){
            grabmartItemMapping = grabmartItemMappings.get(0);
            content.setGrabUnit(grabmartItemMapping.getGrabmartUnit());
            content.setValue(grabmartItemMapping.getValue());
            content.setGrabItemGroupId(String.valueOf(grabmartItemMapping.getGrabmartItemGroup().getId()));
            content.setGrabItemGroup(grabmartItemMapping.getGrabmartItemGroup().getName());
            content.setGrabItemCategoryId(String.valueOf(grabmartItemMapping.getGrabmartItemCategory().getId()));
            content.setGrabItemCategory(grabmartItemMapping.getGrabmartItemCategory().getName());
            content.setIsPublished(grabmartItemMapping.getIsPublished());
            content.setSpecialType(grabmartItemMapping.getSpecialType());
        }

        // Get List grab store
        List<LocationGrabForSelectDTO> stores = locationService.getLocationsGrabForSelect(Product.GRAB_MART.name());

        List<GrabmartItemByItemMasterViewDTO.Location> locations = new ArrayList<>();
        for(LocationGrabForSelectDTO store: stores){
            GrabmartItemMapping mapping = grabmartItemMappings.stream().filter(o -> o.getLocation() != null &&
                    o.getLocation().getLocationId() != null &&
                    o.getLocation().getLocationId().equals(Long.valueOf(store.getId()))).findFirst().orElse(null);

            GrabmartItemByItemMasterViewDTO.Location location = new GrabmartItemByItemMasterViewDTO.Location();
            location.setId(store.getId());
            location.setName(store.getName());

            if(mapping != null) {location.setCheck(true);}

            locations.add(location);
        }
        content.setLocations(locations);

        SuccessResponse<GrabmartItemByItemMasterViewDTO> response = new SuccessResponse<>("success", content);
        return ResponseEntity.ok(response);
    }

    @Override
    @Transactional( rollbackFor = {Throwable.class})
    public ResponseEntity<Object> uploadItemMasterGrabMart(List<UploadItemMasterGrabMartDTO> data, Long locationId) {
        Location location = locationService.findById(locationId).orElseThrow(() -> new ResourceNotFoundException("location not found!"));
        Set<String> barcodes = data.stream().map(UploadItemMasterGrabMartDTO::getBarcode).collect(Collectors.toSet());

        if(barcodes.isEmpty()) throw new ResourceNotFoundException("barcode not found!");

        List<GrabmartItemMappingViewDTO> grabmartItemMappingViewDTOS = grabmartItemMappingRepository.getGrabMartItemMappingByBarcodes(new ArrayList<>(barcodes));
        Set<Long> grabMartItemGroupIds = grabmartItemMappingViewDTOS.stream().map(GrabmartItemMappingViewDTO::getGrabmartItemGroupId).collect(Collectors.toSet());
        Set<Long> grabMartItemCategoryIds = grabmartItemMappingViewDTOS.stream().map(GrabmartItemMappingViewDTO::getGrabmartItemCategoryId).collect(Collectors.toSet());
        Set<Long> itemMasterIds = grabmartItemMappingViewDTOS.stream().map(GrabmartItemMappingViewDTO::getItemMasterId).collect(Collectors.toSet());

        // mapping grabmart item group
        List<GrabmartItemGroup> grabmartItemGroups = grabmartItemGroupService.findAllGrabMartItemGroupByIds(new ArrayList<>(grabMartItemGroupIds));
        Map<Long, GrabmartItemGroup> grabmartItemGroupMap = grabmartItemGroups.stream().collect(Collectors.toMap(GrabmartItemGroup::getId, Function.identity()));

        // mapping grabmart item category
        List<GrabmartItemCategory> grabmartItemCategories = grabmartItemCategoryService.findAllGrabMartItemCategoryByIds(new ArrayList<>(grabMartItemCategoryIds));
        Map<Long, GrabmartItemCategory> grabmartItemCategoryMap = grabmartItemCategories.stream().collect(Collectors.toMap(GrabmartItemCategory::getId, Function.identity()));

        // mapping item master
        List<ItemMaster> itemMasters = itemMasterService.findItemMastersByItemMasterIds(new ArrayList<>(itemMasterIds));
        Map<Long, ItemMaster> itemMasterMap = itemMasters.stream().collect(Collectors.toMap(ItemMaster::getItemMasterId, Function.identity()));

        List<GrabmartItemMapping>  grabmartItemMappings = new ArrayList<>();

        for(GrabmartItemMappingViewDTO view:  grabmartItemMappingViewDTOS){
            GrabmartItemMapping grabmartItemMapping = new GrabmartItemMapping();

            GrabmartItemGroup  grabmartItemGroup = grabmartItemGroupMap.get(view.getGrabmartItemGroupId());
            GrabmartItemCategory grabmartItemCategory = grabmartItemCategoryMap.get(view.getGrabmartItemCategoryId());
            ItemMaster itemMaster = itemMasterMap.get(view.getItemMasterId());

            if(grabmartItemGroup != null) {
                grabmartItemMapping.setGrabmartItemGroup(grabmartItemGroup);
            }

            if(grabmartItemCategory != null) {
                grabmartItemMapping.setGrabmartItemCategory(grabmartItemCategory);
            }

            if (itemMaster != null) {
                grabmartItemMapping.setItemMaster(itemMaster);
                grabmartItemMapping.setItemName(itemMaster.getName());
            }

            grabmartItemMapping.setGrabmartUnit(view.getGrabmartUnit());
            grabmartItemMapping.setValue(view.getValue());
            grabmartItemMapping.setSpecialType(view.getSpecialType());
            grabmartItemMapping.setIsPublished(1);
            grabmartItemMapping.setLocation(location);

            grabmartItemMappings.add(grabmartItemMapping);
        }

        // find grab api app
        ApiApp apiApp = apiAppService.findApiAppByName("Grab").orElse(null);
        List<ApiAppSyncDTO> apiAppSyncDTOS = new ArrayList<>();
        if(apiApp != null){
            for(GrabmartItemMappingViewDTO dto: grabmartItemMappingViewDTOS){
                ApiAppSyncDTO apiAppSyncDTO = new ApiAppSyncDTO();
                apiApp.getApiAppSyncSetups().stream().filter(a -> a.getStatus() == 1 && a.getTableName().equalsIgnoreCase("pos_item_master")).findFirst().ifPresent(apiAppSyncSetup -> {
                    apiAppSyncDTO.setTableName("pos_item_master");
                    apiAppSyncDTO.setAction("update");
                    apiAppSyncDTO.setOwnerId(dto.getItemMasterId());
                    apiAppSyncDTO.setLocation(location);

                    apiAppSyncDTOS.add(apiAppSyncDTO);
                });
            }

            try{
                apiAppSyncService.batchSaveApiAppSync(apiAppSyncDTOS, apiApp);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }

        grabmartItemMappingRepository.saveAll(grabmartItemMappings);
        SuccessResponse<Object> response = new SuccessResponse<>("success", null);
        return ResponseEntity.ok(response);
    }
}
