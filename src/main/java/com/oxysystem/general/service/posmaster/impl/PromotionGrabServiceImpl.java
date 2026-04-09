package com.oxysystem.general.service.posmaster.impl;

import com.oxysystem.general.dto.general.location.view.LocationGrabForSelectDTO;
import com.oxysystem.general.dto.grab.data.CampaignDTO;
import com.oxysystem.general.dto.grab.data.CreateCampaignResponseDTO;
import com.oxysystem.general.dto.grab.view.PeriodDTO;
import com.oxysystem.general.dto.posmaster.promotionGrab.data.PromotionGrabDTO;
import com.oxysystem.general.dto.posmaster.promotionGrab.view.PromotionGrabViewDTO;
import com.oxysystem.general.enums.grab.CampaignCreateBy;
import com.oxysystem.general.enums.DocumentStatus;
import com.oxysystem.general.enums.grab.CampaignType;
import com.oxysystem.general.enums.grab.Product;
import com.oxysystem.general.exception.GrabException;
import com.oxysystem.general.exception.ResourceNotFoundException;
import com.oxysystem.general.model.db1.admin.User;
import com.oxysystem.general.model.db1.general.Location;
import com.oxysystem.general.model.db1.posmaster.*;
import com.oxysystem.general.repository.db1.posmaster.PromotionGrabRepository;
import com.oxysystem.general.response.PaginationResponse;
import com.oxysystem.general.response.SuccessPaginationResponse;
import com.oxysystem.general.response.SuccessResponse;
import com.oxysystem.general.response.grab.GrabFailedResponse;
import com.oxysystem.general.service.admin.UserService;
import com.oxysystem.general.service.general.LocationService;
import com.oxysystem.general.service.grab.client.grabFood.GrabFoodCampaignServiceImpl;
import com.oxysystem.general.service.grab.client.grabFood.GrabFoodOAuthServiceImpl;
import com.oxysystem.general.service.grab.client.grabMart.GrabMartCampaignServiceImpl;
import com.oxysystem.general.service.grab.client.grabMart.GrabMartOAuthServiceImpl;
import com.oxysystem.general.service.posmaster.*;
import com.oxysystem.general.util.DateUtils;
import com.oxysystem.general.util.ResponseUtils;
import com.oxysystem.general.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PromotionGrabServiceImpl implements PromotionGrabService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PromotionGrabServiceImpl.class);

    @Value("${grabmart.endpoint-create-campaign}")
    private String endpointCreateCampaign;
    private final PromotionGrabRepository promotionGrabRepository;
    private final UserService userService;
    private final LocationService locationService;
    private final WebClient grabMartClient;
    private final WebClient grabFoodClient;
    private final GrabMartOAuthServiceImpl grabMartOAuthService;
    private final GrabFoodOAuthServiceImpl grabFoodOAuthService;
    private final StrukKasirService strukKasirService;
    private final ItemMasterService itemMasterService;
    private final GrabmartItemGroupService grabmartItemGroupService;
    private final PromotionGrabDetailService promotionGrabDetailService;
    private final PromotionGrabLocationService promotionGrabLocationService;
    private final GrabMartCampaignServiceImpl grabMartCampaignService;
    private final GrabFoodCampaignServiceImpl grabFoodCampaignService;

    @Override
    @Transactional(value = "db1TransactionManager", rollbackFor = {Throwable.class})
    public ResponseEntity<?> save(PromotionGrabDTO promotionGrabDTO) {
        PromotionGrab promotionGrab = new PromotionGrab();

        if (promotionGrabDTO.getPromotionGrabLocationDTOS() == null || promotionGrabDTO.getPromotionGrabLocationDTOS().isEmpty())
            throw new ResourceNotFoundException("promotion grab location cannot be empty!");

        User user = new User();
        Long userId = promotionGrabDTO.getUserId();
        user = userService.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found!"));

        String numberPrefix = "PRMGRB" + DateUtils.formatDate(LocalDateTime.now(), "MM") + DateUtils.formatDate(LocalDateTime.now(), "yy");
        int maxCounter = promotionGrabRepository.getMaxCounterPromotionGrabByNumberPrefix(numberPrefix).orElse(0);
        maxCounter++;

        String number = numberPrefix + StringUtils.formatCounter(6, maxCounter);

        promotionGrab.setNumber(number);
        promotionGrab.setNumberPrefix(numberPrefix);
        promotionGrab.setCounter(maxCounter);
        promotionGrab.setUser(user);
        promotionGrab.setName(promotionGrabDTO.getName());
        promotionGrab.setStartTime(promotionGrabDTO.getStartTime());
        promotionGrab.setEndTime(promotionGrabDTO.getEndTime());
        promotionGrab.setEaterType(promotionGrabDTO.getEaterType());
        promotionGrab.setMinBasketAmount(promotionGrabDTO.getMinBasketAmount());
        promotionGrab.setBundleQuantity(promotionGrabDTO.getBundleQuantity());
        promotionGrab.setTotalCount(promotionGrabDTO.getTotalCount());
        promotionGrab.setTotalCountPerUser(promotionGrabDTO.getTotalCountPerUser());

        CampaignType.getKeyByDisplayName(promotionGrabDTO.getType());
        promotionGrab.setType(promotionGrabDTO.getType());
        promotionGrab.setCap(promotionGrabDTO.getCap());
        promotionGrab.setValue((double) Math.round(promotionGrabDTO.getValue()));
        promotionGrab.setScopeType(promotionGrabDTO.getScopeType());
        promotionGrab.setCustomTag(promotionGrabDTO.getCustomTag());
        promotionGrab.setCreatedBy(CampaignCreateBy.PARTNER.getDisplayName());
        promotionGrab.setStatus(DocumentStatus.DRAFT);
        promotionGrab.setProduct(promotionGrabDTO.getProduct());

        List<PromotionGrabDetail> promotionGrabDetails = new ArrayList<>();
        List<PromotionGrabLocation> promotionGrabLocations = new ArrayList<>();

        // Promotion Grab Detail

        // for searching item master or grab mart category
        Map<Long, ItemMaster> itemMasterMap = new HashMap<>();
        Map<Long, GrabmartItemGroup> itemGroupMap = new HashMap<>();
        if (promotionGrab.getScopeType().equals("items")) {
            itemMasterMap = getItemMasterMap(promotionGrabDTO);
        } else if (promotionGrab.getScopeType().equals("category")) {
            itemGroupMap = getGrabMartItemGroupMap(promotionGrabDTO);
        }

        if (promotionGrabDTO.getPromotionGrabDetailDTOS() != null && !promotionGrabDTO.getPromotionGrabDetailDTOS().isEmpty()) {
            for (PromotionGrabDTO.PromotionGrabDetailDTO promotionGrabDetailDTO : promotionGrabDTO.getPromotionGrabDetailDTOS()) {
                PromotionGrabDetail promotionGrabDetail = new PromotionGrabDetail();
                promotionGrabDetail.setPromotionGrab(promotionGrab);

                ItemMaster itemMaster = itemMasterMap.get(Long.valueOf(promotionGrabDetailDTO.getObjectId()));
                GrabmartItemGroup grabmartItemGroup = itemGroupMap.get(Long.valueOf(promotionGrabDetailDTO.getObjectId()));

                if (itemMaster != null) promotionGrabDetail.setObjectId(String.valueOf(itemMaster.getItemMasterId()));
                if (grabmartItemGroup != null)
                    promotionGrabDetail.setObjectId(String.valueOf(grabmartItemGroup.getId()));

                promotionGrabDetails.add(promotionGrabDetail);
            }
            promotionGrab.setPromotionGrabDetails(promotionGrabDetails);
        }

        // Promotion Grab Location

        // for searching location
        Map<Long, Location> locationMap = getLocationMap(promotionGrabDTO);

        if (promotionGrabDTO.getPromotionGrabLocationDTOS() != null && !promotionGrabDTO.getPromotionGrabLocationDTOS().isEmpty()) {
            for (PromotionGrabDTO.PromotionGrabLocationDTO promotionGrabLocationDTO : promotionGrabDTO.getPromotionGrabLocationDTOS()) {
                PromotionGrabLocation promotionGrabLocation = new PromotionGrabLocation();
                promotionGrabLocation.setPromotionGrab(promotionGrab);

                Location location = locationMap.get(promotionGrabLocationDTO.getLocationId());
                if (location != null) {
                    promotionGrabLocation.setLocation(location);

                    promotionGrabLocations.add(promotionGrabLocation);
                }

            }
            promotionGrab.setPromotionGrabLocations(promotionGrabLocations);
        }

        promotionGrab = promotionGrabRepository.save(promotionGrab);
        SuccessResponse<String> response = new SuccessResponse<>("success", String.valueOf(promotionGrab.getId()));
        return ResponseEntity.ok(response);
    }

    @Override
    @Transactional(value = "db1TransactionManager", rollbackFor = {Throwable.class})
    public ResponseEntity<?> update(Long id, PromotionGrabDTO promotionGrabDTO) {
        PromotionGrab promotionGrab = promotionGrabRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("promotion not found!"));

        if (promotionGrabDTO.getPromotionGrabLocationDTOS() == null || promotionGrabDTO.getPromotionGrabLocationDTOS().isEmpty())
            throw new ResourceNotFoundException("promotion grab location cannot be empty!");

        // Delete promotion grab detail if scope type has change
        if (!promotionGrab.getScopeType().equals(promotionGrabDTO.getScopeType())) {
            promotionGrabDetailService.deletePromotionGrabDetailByPromotionGrabId(promotionGrab.getId());
        }

        promotionGrab.setName(promotionGrabDTO.getName());
        promotionGrab.setStartTime(promotionGrabDTO.getStartTime());
        promotionGrab.setEndTime(promotionGrabDTO.getEndTime());
        promotionGrab.setEaterType(promotionGrabDTO.getEaterType());
        promotionGrab.setMinBasketAmount(promotionGrabDTO.getMinBasketAmount());
        promotionGrab.setBundleQuantity(promotionGrabDTO.getBundleQuantity());
        promotionGrab.setTotalCount(promotionGrabDTO.getTotalCount());
        promotionGrab.setTotalCountPerUser(promotionGrabDTO.getTotalCountPerUser());
        promotionGrab.setType(promotionGrabDTO.getType());
        promotionGrab.setCap(promotionGrabDTO.getCap());
        promotionGrab.setValue((double) Math.round(promotionGrabDTO.getValue()));
        promotionGrab.setScopeType(promotionGrabDTO.getScopeType());
        promotionGrab.setCustomTag(promotionGrabDTO.getCustomTag());
        promotionGrab.setCreatedBy(CampaignCreateBy.PARTNER.getDisplayName());
        promotionGrab.setStatus(DocumentStatus.valueOf(promotionGrabDTO.getStatus()));
        promotionGrab.setProduct(promotionGrabDTO.getProduct());

        List<PromotionGrabDetail> promotionGrabDetails = new ArrayList<>();
        List<PromotionGrabLocation> promotionGrabLocations = new ArrayList<>();

        Map<Long, PromotionGrabDetail> promotionGrabDetailMap = promotionGrab.getPromotionGrabDetails().stream()
                .collect(Collectors.toMap(PromotionGrabDetail::getId, promotionGrabDetail -> promotionGrabDetail));

        // Promotion Grab Detail

        // for searching item master or grab mart category
        Map<Long, ItemMaster> itemMasterMap = new HashMap<>();
        Map<Long, GrabmartItemGroup> itemGroupMap = new HashMap<>();
        if (promotionGrab.getScopeType().equals("items")) {
            itemMasterMap = getItemMasterMap(promotionGrabDTO);
        } else if (promotionGrab.getScopeType().equals("category")) {
            itemGroupMap = getGrabMartItemGroupMap(promotionGrabDTO);
        }
        if (promotionGrabDTO.getPromotionGrabDetailDTOS() != null && !promotionGrabDTO.getPromotionGrabDetailDTOS().isEmpty()) {
            for (PromotionGrabDTO.PromotionGrabDetailDTO promotionGrabDetailDTO : promotionGrabDTO.getPromotionGrabDetailDTOS()) {
                PromotionGrabDetail promotionGrabDetail = new PromotionGrabDetail();
                if (promotionGrabDetailDTO.getId() != null && !promotionGrabDetailDTO.getId().isEmpty()) {
                    promotionGrabDetail = promotionGrabDetailMap.get(Long.valueOf(promotionGrabDetailDTO.getId()));
                }
                if (promotionGrabDetail == null) {
                    promotionGrabDetail = new PromotionGrabDetail();
                }

                promotionGrabDetail.setPromotionGrab(promotionGrab);

                ItemMaster itemMaster = itemMasterMap.get(Long.valueOf(promotionGrabDetailDTO.getObjectId()));
                GrabmartItemGroup grabmartItemGroup = itemGroupMap.get(Long.valueOf(promotionGrabDetailDTO.getObjectId()));

                if (itemMaster != null) promotionGrabDetail.setObjectId(String.valueOf(itemMaster.getItemMasterId()));
                if (grabmartItemGroup != null)
                    promotionGrabDetail.setObjectId(String.valueOf(grabmartItemGroup.getId()));

                promotionGrabDetails.add(promotionGrabDetail);
            }
            promotionGrab.setPromotionGrabDetails(promotionGrabDetails);
        }

        // delete promotion grab location
        promotionGrabLocationService.deletePromotionGrabLocationByPromotionGrabId(promotionGrab.getId());

        // Promotion Grab Location

        // for searching location
        Map<Long, Location> locationMap =
                getLocationMap(promotionGrabDTO);
        if (promotionGrabDTO.getPromotionGrabLocationDTOS() != null && !promotionGrabDTO.getPromotionGrabLocationDTOS().isEmpty()) {
            for (PromotionGrabDTO.PromotionGrabLocationDTO promotionGrabLocationDTO : promotionGrabDTO.getPromotionGrabLocationDTOS()) {
                PromotionGrabLocation promotionGrabLocation = new PromotionGrabLocation();

                promotionGrabLocation.setPromotionGrab(promotionGrab);

                Location location = locationMap.get(promotionGrabLocationDTO.getLocationId());
                if (location != null) {
                    promotionGrabLocation.setLocation(location);

                    promotionGrabLocations.add(promotionGrabLocation);
                }
            }
            promotionGrab.setPromotionGrabLocations(promotionGrabLocations);
        }

        Set<Long> locationIds = promotionGrab.getPromotionGrabLocations().stream()
                .map(r -> r.getLocation().getLocationId())
                .collect(Collectors.toSet());

        List<StrukKasir> strukKasirList = strukKasirService.findStrukKasirByLocationIds(new ArrayList<>(locationIds));
        Map<Long, StrukKasir> strukKasirMap = strukKasirList.stream()
                .collect(Collectors.toMap(StrukKasir::getLocationId, Function.identity()));

        promotionGrab = promotionGrabRepository.save(promotionGrab);

        if (promotionGrab.getStatus().equals(DocumentStatus.APPROVED)) {
            List<PromotionGrabLocation> updatePromotionLocationList = new ArrayList<>();

            List<String> createdCampaignIds = new ArrayList<>();
            // Save promotion to grabmart API

            LOGGER.info("Starting insert promo to grab api");
            if (promotionGrab.getPromotionGrabLocations() != null && !promotionGrab.getPromotionGrabLocations().isEmpty()) {

                String grabToken = "";
                if (promotionGrab.getProduct().equals(Product.GRAB_MART.name())) {
                    grabToken = grabMartOAuthService.getGrabToken();
                } else if (promotionGrab.getProduct().equals(Product.GRAB_FOOD.name())) {
                    grabToken = grabFoodOAuthService.getGrabToken();
                }

                try {
                    for (PromotionGrabLocation promotionGrabLocation : promotionGrab.getPromotionGrabLocations()) {
                        CampaignDTO campaignDTO = new CampaignDTO();
                        Location location = promotionGrabLocation.getLocation();
                        StrukKasir strukKasir = strukKasirMap.get(location.getLocationId());
                        campaignDTO.setMerchantID(strukKasir.getGrabMerchantId());
                        campaignDTO.setName(promotionGrab.getName().replaceAll("\\s+", " ").trim());

                        LOGGER.info("Starting insert promo to grab api MERCHANT {}", strukKasir.getGrabMerchantId());

                        CampaignDTO.Quota quota = new CampaignDTO.Quota();

                        if (promotionGrab.getTotalCount() != null && promotionGrab.getTotalCount() > 0) {
                            quota.setTotalCount(promotionGrab.getTotalCount());
                        }
                        if (promotionGrab.getTotalCountPerUser() != null && promotionGrab.getTotalCountPerUser() > 0) {
                            quota.setTotalCountPerUser(promotionGrabDTO.getTotalCountPerUser());
                        }

                        if ((quota.getTotalCount() != null && quota.getTotalCount() > 0) || (quota.getTotalCountPerUser() != null && quota.getTotalCountPerUser() > 0)) {
                            campaignDTO.setQuotas(quota);
                        }

                        CampaignDTO.Condition condition = new CampaignDTO.Condition();

                        Instant startTime = promotionGrabDTO.getStartTime().minusHours(8).toInstant(ZoneOffset.UTC);
                        condition.setStartTime(DateTimeFormatter.ISO_INSTANT.format(startTime));
                        Instant endTime = promotionGrabDTO.getEndTime().minusHours(8).toInstant(ZoneOffset.UTC);
                        condition.setEndTime(DateTimeFormatter.ISO_INSTANT.format(endTime));
                        condition.setEaterType(promotionGrab.getEaterType());
                        condition.setMinBasketAmount(promotionGrab.getMinBasketAmount());
                        condition.setBundleQuantity(promotionGrab.getBundleQuantity());
                        Map<String, CampaignDTO.Condition.DayPeriodDTO> workingHour = new LinkedHashMap<>();

                        List<PeriodDTO> periodDTOS = new ArrayList<>();
                        PeriodDTO periodDTO = new PeriodDTO();
                        periodDTO.setStartTime("09:00");
                        periodDTO.setEndTime("23:59");
                        periodDTOS.add(periodDTO);
                        CampaignDTO.Condition.DayPeriodDTO dayPeriodDTO = new CampaignDTO.Condition.DayPeriodDTO();
                        dayPeriodDTO.setPeriods(periodDTOS);

                        List<String> days = Arrays.asList("mon", "tue", "wed", "thu", "fri", "sat", "sun");
                        for (String day : days) {
                            workingHour.put(day, dayPeriodDTO);
                        }
                        condition.setWorkingHour(workingHour);
                        campaignDTO.setConditions(condition);

                        CampaignDTO.Discount discount = new CampaignDTO.Discount();
                        discount.setType(promotionGrab.getType());
                        discount.setCap(promotionGrab.getCap());
                        discount.setValue(promotionGrab.getValue());

                        CampaignDTO.Discount.Scope scope = new CampaignDTO.Discount.Scope();
                        scope.setType(promotionGrab.getScopeType());

                        List<String> objectIDs = promotionGrab.getPromotionGrabDetails().stream()
                                .map(PromotionGrabDetail::getObjectId)
                                .collect(Collectors.toList());

                        scope.setObjectIDs(objectIDs);
                        discount.setScope(scope);
                        campaignDTO.setDiscount(discount);
                        campaignDTO.setCustomTag(promotionGrab.getCustomTag());

                        CreateCampaignResponseDTO campaignResponseDTO = null;

                        // Insert promotion to grab
                        if(promotionGrab.getProduct().equals(Product.GRAB_MART.name())){
                            campaignResponseDTO = createCampaign(grabMartClient, campaignDTO, grabToken);
                        } else if (promotionGrab.getProduct().equals(Product.GRAB_FOOD.name())) {
                            campaignResponseDTO = createCampaign(grabFoodClient, campaignDTO, grabToken);
                        }

                        if (campaignResponseDTO == null) {
                            LOGGER.warn("Create promotion failed: response is null");
                        } else {
                            createdCampaignIds.add(campaignResponseDTO.getId());
                            promotionGrabLocation.setPromotionIdByGrab(campaignResponseDTO.getId());
                            updatePromotionLocationList.add(promotionGrabLocation);
                        }
                    }
                    if (!updatePromotionLocationList.isEmpty())
                        promotionGrabLocationService.saveAll(updatePromotionLocationList);

                } catch (Exception e) {
                    LOGGER.error("❌ Campaign creation failed! Rolling back Grab…");

                    // COMPENSATION
                    for (String campaignId : createdCampaignIds) {
                        try {
                            if(promotionGrab.getProduct().equals(Product.GRAB_MART.name())){
                                grabMartCampaignService.deleteCampaign(campaignId, grabToken);
                            } else if (promotionGrab.getProduct().equals(Product.GRAB_FOOD.name())) {
                                grabFoodCampaignService.deleteCampaign(campaignId, grabToken);
                            }

                        } catch (Exception deleteEx) {
                            LOGGER.error("⚠️ FAILED TO DELETE CAMPAIGN: {}", campaignId);
                        }
                    }

                    // ROLLBACK DB LOCAL
                    throw e;
                }
            }
        }


        SuccessResponse<String> response = new SuccessResponse<>("success", String.valueOf(promotionGrab.getId()));
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> findByIdForApi(Long id) {
        PromotionGrab promotionGrab = promotionGrabRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("promotion grab not found!"));

        PromotionGrabViewDTO content = new PromotionGrabViewDTO();
        content.setId(String.valueOf(promotionGrab.getId()));
        content.setName(promotionGrab.getName());
        content.setProduct(promotionGrab.getProduct());
        content.setStartTime(promotionGrab.getStartTime());
        content.setEndTime(promotionGrab.getEndTime());
        content.setEaterType(promotionGrab.getEaterType());
        content.setMinBasketAmount(promotionGrab.getMinBasketAmount());
        content.setBundleQuantity(promotionGrab.getBundleQuantity());
        content.setTotalCount(promotionGrab.getTotalCount());
        content.setTotalCountPerUser(promotionGrab.getTotalCountPerUser());
        content.setType(promotionGrab.getType());
        content.setCap(promotionGrab.getCap());
        content.setValue(promotionGrab.getValue());
        content.setScopeType(promotionGrab.getScopeType());
        content.setCustomTag(promotionGrab.getCustomTag());
        content.setCreatedBy(promotionGrab.getCreatedBy());
        content.setStatus(promotionGrab.getStatus().name());

        Set<Long> itemIDs = promotionGrab.getPromotionGrabDetails().stream()
                .map(detail -> Long.valueOf(detail.getObjectId()))
                .collect(Collectors.toSet());
        List<ItemMaster> itemMasters = itemMasterService.findItemMastersByItemMasterIds(new ArrayList<>(itemIDs));
        Map<Long, ItemMaster> itemMasterMap = itemMasters.stream()
                .collect(Collectors.toMap(ItemMaster::getItemMasterId, Function.identity(), (existing, replacement) -> existing));

        Set<Long> itemGroupIDs = promotionGrab.getPromotionGrabDetails().stream()
                .map(detail -> Long.valueOf(detail.getObjectId()))
                .collect(Collectors.toSet());
        List<GrabmartItemGroup> itemGroups = grabmartItemGroupService.findAllGrabMartItemGroupByIds(new ArrayList<>(itemGroupIDs));
        Map<Long, GrabmartItemGroup> itemGroupMap = itemGroups.stream()
                .collect(Collectors.toMap(GrabmartItemGroup::getId, Function.identity()));

        List<PromotionGrabViewDTO.PromotionGrabDetailViewDTO> promotionGrabDetails = promotionGrab.getPromotionGrabDetails().stream()
                .map(detail -> {
                    PromotionGrabViewDTO.PromotionGrabDetailViewDTO promotionGrabDetailViewDTO = new PromotionGrabViewDTO.PromotionGrabDetailViewDTO();
                    promotionGrabDetailViewDTO.setId(String.valueOf(detail.getId()));

                    ItemMaster itemMaster = itemMasterMap.get(Long.valueOf(detail.getObjectId()));
                    GrabmartItemGroup grabmartItemGroup = itemGroupMap.get(Long.valueOf(detail.getObjectId()));

                    if(itemMaster != null){
                        promotionGrabDetailViewDTO.setItemId(String.valueOf(itemMaster.getItemMasterId()));
                        promotionGrabDetailViewDTO.setItemName(itemMaster.getName());
                        promotionGrabDetailViewDTO.setItemBarcode(itemMaster.getBarcode());
                    }if(grabmartItemGroup != null){
                        promotionGrabDetailViewDTO.setGrabMartItemGroupId(String.valueOf(grabmartItemGroup.getId()));
                        promotionGrabDetailViewDTO.setGrabMartItem(grabmartItemGroup.getName());
                    }

                    return promotionGrabDetailViewDTO;
                }).collect(Collectors.toList());

        content.setPromotionGrabDetails(promotionGrabDetails);

        List<LocationGrabForSelectDTO> grabLocations = locationService.getLocationsGrabForSelect(promotionGrab.getProduct());

        List<PromotionGrabViewDTO.PromotionGrabLocationViewDTO> promotionGrabLocations = grabLocations.stream()
                .map(location -> {
                    PromotionGrabViewDTO.PromotionGrabLocationViewDTO promotionGrabLocationViewDTO = new PromotionGrabViewDTO.PromotionGrabLocationViewDTO();
                    promotionGrabLocationViewDTO.setLocationId(String.valueOf(location.getId()));
                    promotionGrabLocationViewDTO.setLocationName(location.getName());

                    promotionGrab.getPromotionGrabLocations().stream()
                            .filter((grabLocation) -> grabLocation.getLocation().getLocationId().equals(Long.valueOf(location.getId())))
                            .findFirst().ifPresent(promotionGrabLocation -> promotionGrabLocationViewDTO.setChecked(true));

                    return promotionGrabLocationViewDTO;
                }).collect(Collectors.toList());

        content.setPromotionGrabLocations(promotionGrabLocations);

        SuccessResponse<PromotionGrabViewDTO> response = new SuccessResponse<>("success",content);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> getPromotionsGrab(Long locationId, String name, String product, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PromotionGrabViewDTO> contentsPage = promotionGrabRepository.getPromotionsGrab(locationId, name, product, pageable);
        PaginationResponse paginationResponse = ResponseUtils.createPaginationResponse(contentsPage);
        SuccessPaginationResponse<List<PromotionGrabViewDTO>> response = new SuccessPaginationResponse<>("success",paginationResponse,contentsPage.getContent());
        return ResponseEntity.ok(response);
    }

    @Override
    @Transactional(value = "db1TransactionManager", rollbackFor = {Throwable.class})
    public ResponseEntity<?> deletePromotionGrabDetail(Long detailId) {
        PromotionGrabDetail promotionGrabDetail = promotionGrabDetailService.findById(detailId).orElseThrow(() ->  new ResourceNotFoundException("promotion grab detail not found!"));
        promotionGrabDetailService.delete(promotionGrabDetail);
        SuccessResponse<String> response = new SuccessResponse<>("success", null);
        return ResponseEntity.ok(response);
    }

    @Override
    @Transactional(value = "db1TransactionManager", rollbackFor = {Throwable.class})
    public ResponseEntity<?> terminatePromotionGrab(Long id) {
        PromotionGrab promotionGrab = promotionGrabRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("promotion grab not found!"));
        promotionGrab.setStatus(DocumentStatus.TERMINATED);
        for(PromotionGrabLocation promotionGrabLocation: promotionGrab.getPromotionGrabLocations()){
            if(promotionGrab.getProduct().equals(Product.GRAB_MART.name())){
                grabMartCampaignService.deleteCampaign(promotionGrabLocation.getPromotionIdByGrab(), null);
            }else if(promotionGrab.getProduct().equals(Product.GRAB_FOOD.name())){
                grabFoodCampaignService.deleteCampaign(promotionGrabLocation.getPromotionIdByGrab(), null);
            }

        }
        promotionGrabRepository.save(promotionGrab);

        SuccessResponse<String> response = new SuccessResponse<>("success", null);
        return ResponseEntity.ok(response);
    }

    private Map<Long, ItemMaster> getItemMasterMap(PromotionGrabDTO promotionGrabDTO){
        Set<Long> itemIDs = promotionGrabDTO.getPromotionGrabDetailDTOS().stream()
                .map(detail -> Long.valueOf(detail.getObjectId()))
                .collect(Collectors.toSet());
        List<ItemMaster> itemMasters = itemMasterService.findItemMastersByItemMasterIds(new ArrayList<>(itemIDs));

        if(itemIDs.size() != itemMasters.size()) throw new ResourceNotFoundException("some items not found!");
        return itemMasters.stream()
                .collect(Collectors.toMap(ItemMaster::getItemMasterId, Function.identity(),(existing, replacement) -> existing));
    }

    private Map<Long, GrabmartItemGroup> getGrabMartItemGroupMap(PromotionGrabDTO promotionGrabDTO){
        Set<Long> itemGroupIds = promotionGrabDTO.getPromotionGrabDetailDTOS().stream()
                .map(detail -> Long.valueOf(detail.getObjectId()))
                .collect(Collectors.toSet());
        List<GrabmartItemGroup> itemGroups = grabmartItemGroupService.findAllGrabMartItemGroupByIds(new ArrayList<>(itemGroupIds));
        if(itemGroupIds.size() != itemGroups.size()) throw new ResourceNotFoundException("some grab item category not found!");
        return itemGroups.stream()
                .collect(Collectors.toMap(GrabmartItemGroup::getId, Function.identity()));
    }

    private Map<Long, Location> getLocationMap(PromotionGrabDTO promotionGrabDTO){
        Set<Long> locationIds = promotionGrabDTO.getPromotionGrabLocationDTOS().stream()
                .map(PromotionGrabDTO.PromotionGrabLocationDTO::getLocationId)
                .collect(Collectors.toSet());
        List<Location> locations = locationService.findAllByIDs(new ArrayList<>(locationIds));
        if(locationIds.size() != locations.size()) throw new ResourceNotFoundException("some location not found!");
        return locations.stream()
                .collect(Collectors.toMap(Location::getLocationId, Function.identity()));
    }

    private CreateCampaignResponseDTO createCampaign(WebClient webClient, CampaignDTO campaignDTO, String token){
        return  webClient.post()
                .uri(endpointCreateCampaign)
                .header("Authorization", "Bearer " + token)
                .bodyValue(campaignDTO)
                .retrieve()
                .onStatus(HttpStatus::isError, clientResponse -> clientResponse.bodyToMono(GrabFailedResponse.class)
                        .switchIfEmpty(Mono.error(new GrabException("Delete promotion is error", "unathorized")))
                        .flatMap(error -> Mono.error(new GrabException(error.getReason(), error.getMessage()))))
                .bodyToMono(CreateCampaignResponseDTO.class)
                .doOnError(err ->
                        LOGGER.error("[Grab] Error before retry: {} - {}",
                                err.getClass().getSimpleName(), err.getMessage(), err)
                )
                .timeout(Duration.ofSeconds(60))
                .block();
    }
}
