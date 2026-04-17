package com.oxysystem.general.service.posmaster.impl;

import com.oxysystem.general.dto.general.apiAppSync.data.ApiAppSyncDTO;
import com.oxysystem.general.dto.general.location.view.LocationGrabForSelectDTO;
import com.oxysystem.general.dto.posmaster.grabFoodItemMapping.data.GrabFoodItemByItemMasterDTO;
import com.oxysystem.general.dto.posmaster.grabFoodItemMapping.view.GrabfoodItemByItemMasterViewDTO;
import com.oxysystem.general.dto.posmaster.grabFoodItemMapping.view.GrabfoodItemMappingViewDTO;
import com.oxysystem.general.enums.grab.Product;
import com.oxysystem.general.exception.ResourceNotFoundException;
import com.oxysystem.general.model.tenant.general.ApiApp;
import com.oxysystem.general.model.tenant.general.Location;
import com.oxysystem.general.model.tenant.posmaster.*;
import com.oxysystem.general.repository.tenant.posmaster.GrabfoodItemMappingRepository;
import com.oxysystem.general.response.SuccessResponse;
import com.oxysystem.general.service.general.ApiAppService;
import com.oxysystem.general.service.general.ApiAppSyncService;
import com.oxysystem.general.service.general.LocationService;
import com.oxysystem.general.service.posmaster.GrabfoodItemGroupService;
import com.oxysystem.general.service.posmaster.GrabfoodItemMappingService;
import com.oxysystem.general.service.posmaster.ItemMasterService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GrabfoodItemMappingServiceImpl implements GrabfoodItemMappingService {
    private final GrabfoodItemMappingRepository grabfoodItemMappingRepository;
    private final ItemMasterService itemMasterService;
    private final GrabfoodItemGroupService grabfoodItemGroupService;
    private final LocationService locationService;
    private final ApiAppService apiAppService;
    private final ApiAppSyncService apiAppSyncService;

    public GrabfoodItemMappingServiceImpl(GrabfoodItemMappingRepository grabfoodItemMappingRepository, ItemMasterService itemMasterService, GrabfoodItemGroupService grabfoodItemGroupService, LocationService locationService, ApiAppService apiAppService, ApiAppSyncService apiAppSyncService) {
        this.grabfoodItemMappingRepository = grabfoodItemMappingRepository;
        this.itemMasterService = itemMasterService;
        this.grabfoodItemGroupService = grabfoodItemGroupService;
        this.locationService = locationService;
        this.apiAppService = apiAppService;
        this.apiAppSyncService = apiAppSyncService;
    }

    @Override
    public List<GrabfoodItemMappingViewDTO> getListGrabfoodItemMapping(String golPrice, Long locationId) {
        return grabfoodItemMappingRepository.getGrabfoodItemMapping(golPrice, locationId);
    }

    @Override
    @Transactional(rollbackFor = {Throwable.class})
    public ResponseEntity<?> saveGrabfoodItem(GrabFoodItemByItemMasterDTO grabFoodItemByItemMasterDTO) {
        // Delete grab food item mapping if data exists
        if(!grabfoodItemMappingRepository.findGrabfoodItemMappingByItemMasterId(Long.valueOf(grabFoodItemByItemMasterDTO.getItemId())).isEmpty()){
            grabfoodItemMappingRepository.deleteGrabfoodItemMappingByItemMasterId(Long.valueOf(grabFoodItemByItemMasterDTO.getItemId()));
        }

        ItemMaster itemMaster = itemMasterService.findItemMasterById(Long.valueOf(grabFoodItemByItemMasterDTO.getItemId())).orElseThrow(() -> new ResourceNotFoundException("item not found!"));
        GrabfoodItemGroup itemGroup = grabfoodItemGroupService.findById(Long.valueOf(grabFoodItemByItemMasterDTO.getGrabItemGroupId())).orElseThrow(() -> new ResourceNotFoundException("grab food item group not found!"));

        List<Long> locationIDs = grabFoodItemByItemMasterDTO.getLocations().stream()
                .map(l -> Long.valueOf(l.getId()))
                .collect(Collectors.toList());

        List<Location> locations = locationService.findAllByIDs(locationIDs);

        if (locations.isEmpty()) throw new ResourceNotFoundException("select at least one location!");

        List<GrabfoodItemMapping> itemMappings = new ArrayList<>();

        for(Location location: locations){
            GrabfoodItemMapping mapping = new GrabfoodItemMapping();
            mapping.setItemName(itemMaster.getName());
            mapping.setBarcode(itemMaster.getBarcode());
            mapping.setItemMaster(itemMaster);
            mapping.setDescription(grabFoodItemByItemMasterDTO.getDescription());
            mapping.setGrabfoodItemGroup(itemGroup);
            mapping.setIsPublished(grabFoodItemByItemMasterDTO.getIsPublished());
            mapping.setLocation(location);
            mapping.setSpecialType(grabFoodItemByItemMasterDTO.getSpecialType());
            itemMappings.add(mapping);
        }

        grabfoodItemMappingRepository.saveAll(itemMappings);

        // find grab api app
        ApiApp apiApp = apiAppService.findApiAppByName(Product.GRAB_FOOD.name()).orElse(null);

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
    public ResponseEntity<?> findGrabfoodItemMappingByItemMasterId(Long id) {
        GrabfoodItemByItemMasterViewDTO content = new GrabfoodItemByItemMasterViewDTO();

        ItemMaster itemMaster = itemMasterService.findItemMasterById(id).orElseThrow(() -> new ResourceNotFoundException("item not found!"));
        content.setItemId(String.valueOf(itemMaster.getItemMasterId()));
        content.setItemName(itemMaster.getName());
        content.setBarcode(itemMaster.getBarcode());
        content.setCode(itemMaster.getCode());

        List<GrabfoodItemMapping> grabfoodItemMappings = grabfoodItemMappingRepository.findGrabfoodItemMappingByItemMasterId(itemMaster.getItemMasterId());

        GrabfoodItemMapping grabfoodItemMapping = new GrabfoodItemMapping();
        if(!grabfoodItemMappings.isEmpty()){
            grabfoodItemMapping = grabfoodItemMappings.get(0);
            content.setGrabItemGroupId(String.valueOf(grabfoodItemMapping.getGrabfoodItemGroup().getId()));
            content.setGrabItemGroup(grabfoodItemMapping.getGrabfoodItemGroup().getName());
            content.setIsPublished(grabfoodItemMapping.getIsPublished());
            content.setSpecialType(grabfoodItemMapping.getSpecialType());
        }

        // Get List grab store
        List<LocationGrabForSelectDTO> stores = locationService.getLocationsGrabForSelect(Product.GRAB_FOOD.name());

        List<GrabfoodItemByItemMasterViewDTO.Location> locations = new ArrayList<>();
        for(LocationGrabForSelectDTO store: stores){
            GrabfoodItemMapping mapping = grabfoodItemMappings.stream().filter(o -> o.getLocation() != null &&
                    o.getLocation().getLocationId() != null &&
                    o.getLocation().getLocationId().equals(Long.valueOf(store.getId()))).findFirst().orElse(null);

            GrabfoodItemByItemMasterViewDTO.Location location = new GrabfoodItemByItemMasterViewDTO.Location();
            location.setId(store.getId());
            location.setName(store.getName());

            if(mapping != null) {location.setCheck(true);}

            locations.add(location);
        }
        content.setLocations(locations);

        SuccessResponse<GrabfoodItemByItemMasterViewDTO> response = new SuccessResponse<>("success", content);
        return ResponseEntity.ok(response);
    }
}
