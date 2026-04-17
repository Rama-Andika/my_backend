package com.oxysystem.general.service.grab.partner.grabMart;

import com.oxysystem.general.dto.grab.data.MenuSyncStateRequestDTO;
import com.oxysystem.general.dto.grab.view.*;
import com.oxysystem.general.dto.posmaster.grabMartItemMapping.view.GrabmartItemMappingViewDTO;
import com.oxysystem.general.enums.grab.AvailableStatus;
import com.oxysystem.general.enums.grab.CurrencyCode;
import com.oxysystem.general.enums.grab.CurrencySymbol;
import com.oxysystem.general.enums.grab.Product;
import com.oxysystem.general.exception.ResourceNotFoundException;
import com.oxysystem.general.model.tenant.general.Location;
import com.oxysystem.general.model.tenant.posmaster.ItemMaster;
import com.oxysystem.general.model.tenant.posmaster.ItemMasterImage;
import com.oxysystem.general.model.tenant.posmaster.PriceType;
import com.oxysystem.general.model.tenant.posmaster.StrukKasir;
import com.oxysystem.general.model.tenant.system.SystemMain;
import com.oxysystem.general.model.tenant.transaction.GrabMenuSyncStatus;
import com.oxysystem.general.service.general.CurrencyService;
import com.oxysystem.general.service.general.LocationService;
import com.oxysystem.general.service.grab.partner.common.GrabSellingTimeCommon;
import com.oxysystem.general.service.grab.partner.common.interfaces.GrabMenuSyncPartnerService;
import com.oxysystem.general.service.posmaster.GrabmartItemMappingService;
import com.oxysystem.general.service.posmaster.ItemMasterService;
import com.oxysystem.general.service.posmaster.StrukKasirService;
import com.oxysystem.general.service.system.SystemMainService;
import com.oxysystem.general.service.transaction.GrabMenuSyncStatusService;
import com.oxysystem.general.util.PriceTypeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class GrabMartMenuSyncPartnerServiceImpl implements GrabMenuSyncPartnerService {
    private final StrukKasirService strukKasirService;
    private final LocationService locationService;
    private final CurrencyService currencyService;
    private final GrabmartItemMappingService grabmartItemMappingService;
    private final SystemMainService systemMainService;
    private final ItemMasterService itemMasterService;
    private final GrabMenuSyncStatusService grabMenuSyncStatusService;

    public GrabMartMenuSyncPartnerServiceImpl(StrukKasirService strukKasirService, LocationService locationService, CurrencyService currencyService, GrabmartItemMappingService grabmartItemMappingService, SystemMainService systemMainService, ItemMasterService itemMasterService, GrabMenuSyncStatusService grabMenuSyncStatusService) {
        this.strukKasirService = strukKasirService;
        this.locationService = locationService;
        this.currencyService = currencyService;
        this.grabmartItemMappingService = grabmartItemMappingService;
        this.systemMainService = systemMainService;
        this.itemMasterService = itemMasterService;
        this.grabMenuSyncStatusService = grabMenuSyncStatusService;
    }

    @Override
    public ResponseEntity<?> getMenu(String merchantID, String partnerMerchantID) {
        if(merchantID == null || merchantID.isEmpty()) throw new ResourceNotFoundException("missing merchant ID parameter!");
        if(partnerMerchantID == null || partnerMerchantID.isEmpty()) throw new ResourceNotFoundException("missing partner merchant ID parameter!");

        StrukKasir strukKasir = strukKasirService.findByGrabMerchantId(merchantID).orElseThrow(() -> new ResourceNotFoundException("merchant ID not found"));
        Location location = locationService.findById(strukKasir.getLocationId()).orElseThrow(() -> new ResourceNotFoundException("location not found"));
        currencyService.findIdrCurrency().orElseThrow(() -> new ResourceNotFoundException("currency IDR has not been setup"));

        GrabMenuDTO martMenu = new GrabMenuDTO();
        martMenu.setMerchantID(merchantID);
        martMenu.setPartnerMerchantID(partnerMerchantID);

        CurrencyDTO currency = new CurrencyDTO();
        currency.setCode(CurrencyCode.IDR.name());
        currency.setSymbol(CurrencySymbol.RP.name());
        currency.setExponent(2);

        martMenu.setCurrency(currency);

        List<SellingTimeDTO> sellingTimes = new ArrayList<>();

        SellingTimeDTO sellingTime = GrabSellingTimeCommon.getSellingTimeDTO();
        sellingTimes.add(sellingTime);

        martMenu.setSellingTimes(sellingTimes);

        List<CategoryDTO> categories = getCategoryDTOS(location,currency);

        martMenu.setCategories(categories);
        return ResponseEntity.ok(martMenu);
    }

    @Override
    public ResponseEntity<?> menuSyncState(MenuSyncStateRequestDTO data) {
        GrabMenuSyncStatus grabMenuSyncStatus = grabMenuSyncStatusService.findByMerchantId(data.getMerchantID()).orElse(new GrabMenuSyncStatus());
        grabMenuSyncStatus.setMerchantId(data.getMerchantID());
        grabMenuSyncStatus.setStatus(data.getStatus());
        grabMenuSyncStatus.setProduct(Product.GRAB_MART.name());
        grabMenuSyncStatusService.save(grabMenuSyncStatus);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    private List<CategoryDTO> getCategoryDTOS(Location location, CurrencyDTO currency) {
        List<GrabmartItemMappingViewDTO> grabmartItemMappingViewDTOS = grabmartItemMappingService.getListGrabmartItemMapping(location.getGolPrice(), location.getLocationId());
        Map<Long, List<GrabmartItemMappingViewDTO>> groupedByCategory = grabmartItemMappingViewDTOS.stream()
                .collect(Collectors.groupingBy(GrabmartItemMappingViewDTO::getGrabmartItemGroupId));

        SystemMain systemMainItemIdListDiffZone = systemMainService.findSystemPropertyName("GRABMART_ITEM_ID_LIST_DIFF_ZONE").orElse(null);

        SystemMain systemMainLocationIdListDiffZone = systemMainService.findSystemPropertyName("GRABMART_LOCATION_ID_LIST_DIFF_ZONE").orElse(null);
        SystemMain systemMainImgLink = systemMainService.findSystemPropertyName("IMG_LINK").orElse(null);

        return groupedByCategory.entrySet().stream().map(entry -> {
            GrabmartItemMappingViewDTO categoryRepresentative = entry.getValue().get(0);
            CategoryDTO category = new CategoryDTO();
            category.setId(categoryRepresentative.getGrabmartCategoryId());
            category.setName(categoryRepresentative.getGrabmartCategory());
            category.setAvailableStatus(AvailableStatus.AVAILABLE.name());
            category.setSellingTimeID("SELLING-TIME-01");
            category.setSequence(categoryRepresentative.getGrabmartCategorySequence());

            Map<Long, List<GrabmartItemMappingViewDTO>> groupedSubCategory = entry.getValue().stream().collect(Collectors.groupingBy(GrabmartItemMappingViewDTO::getGrabmartItemCategoryId));

            List<SubCategoryDTO> subCategories = groupedSubCategory.entrySet().stream().map(subEntry -> {
                GrabmartItemMappingViewDTO subCategoryRepresentative = subEntry.getValue().get(0);
                SubCategoryDTO subCategory = new SubCategoryDTO();
                subCategory.setId(subCategoryRepresentative.getGrabmartSubCategoryId());
                subCategory.setName(subCategoryRepresentative.getGrabmartSubCategory());
                subCategory.setAvailableStatus("AVAILABLE");
                subCategory.setSellingTimeID("SELLING-TIME-01");
                subCategory.setSequence(subCategoryRepresentative.getGrabmartSubCategorySequence());

                List<ItemDTO> items = getItemDTOS(subEntry.getValue(), currency, systemMainItemIdListDiffZone, systemMainLocationIdListDiffZone, systemMainImgLink, location);
                subCategory.setItems(items);

                return subCategory;
            }).collect(Collectors.toList());

            category.setSubCategories(subCategories);

            return category;
        })
        .sorted(Comparator.comparing(
                CategoryDTO::getSequence,
                Comparator.nullsLast(Integer::compareTo) // ✅ null paling akhir
        ))
        .collect(Collectors.toList());
    }

    private List<ItemDTO> getItemDTOS(List<GrabmartItemMappingViewDTO> items, CurrencyDTO currency, SystemMain systemMainItemIdListDiffZone, SystemMain systemMainLocationIdListDiffZone, SystemMain systemMainImgLink, Location location) {
        String[] itemIdListDiffZone = systemMainItemIdListDiffZone != null ? systemMainItemIdListDiffZone.getValueprop().split(",") : new String[]{""};
        Set<String> itemIdListDiffZoneSet = new HashSet<>(Arrays.asList(itemIdListDiffZone));

        String[] locationIdListDiffZone = systemMainLocationIdListDiffZone != null ? systemMainLocationIdListDiffZone.getValueprop().split(",") : new String[]{""};
        Set<String> locationIdListDiffZoneSet = new HashSet<>(Arrays.asList(locationIdListDiffZone));

        String imgLink = systemMainImgLink != null ? systemMainImgLink.getValueprop() : "";

        int multipleBy;
        multipleBy = currency.getExponent() != null ? (int) Math.pow(10, currency.getExponent()) : 10;

        List<Long> itemMasterIds = items.stream()
                .map(GrabmartItemMappingViewDTO::getItemMasterId)
                .collect(Collectors.toList());

        List<ItemMaster> itemMasters = itemMasterService.findItemMasterWithPriceTypeByIds(itemMasterIds);

        Map<Long, ItemMaster> itemMasterMap = itemMasters.stream()
                .collect(Collectors.toMap(ItemMaster::getItemMasterId, Function.identity(),(existing, replacement) -> existing));

        return items.stream().map(itemMapping -> {
            ItemMaster itemMaster = itemMasterMap.get(itemMapping.getItemMasterId());
            if(itemMaster != null){
                ItemDTO item = new ItemDTO();
                item.setId(String.valueOf(itemMapping.getItemMasterId()));

                String itemNamePrefix = ( itemMapping.getSpecialType().equalsIgnoreCase("tobacco")
                                    || itemMapping.getSpecialType().equalsIgnoreCase("alcohol")) ? "21+ " : "";
                item.setName(itemNamePrefix + itemMapping.getItemName());
                item.setDescription(itemMapping.getItemName());

                if(locationIdListDiffZoneSet.contains(String.valueOf(location.getLocationId())) && itemIdListDiffZoneSet.contains(item.getId())){
                    PriceType priceType = itemMaster.getPriceTypes().stream().min(Comparator.comparing(PriceType::getConvQty, Comparator.nullsLast(Double::compareTo))
                            .thenComparing(PriceType::getQtyFrom, Comparator.nullsLast(Integer::compareTo))).orElse(null);

                    if(priceType != null){
                        double price = PriceTypeUtils.getPriceByGol(priceType, "gol_12");
                        item.setPrice((long) (price * multipleBy));
                    }else {
                        item.setPrice((long) 0);
                    }
                }else{
                    item.setPrice((long) itemMapping.getPrice() * multipleBy);
                }

                item.setPhotos(null);
                item.setSpecialType(itemMapping.getSpecialType());
                item.setTaxable(false);
                item.setBarcode(itemMapping.getBarcode());

                if(item.getPrice() != null && item.getPrice() <= 0){
                    item.setMaxStock(0);
                    item.setAvailableStatus(AvailableStatus.UNAVAILABLE.name());
                }else{
                    double maxStock = Optional.of(itemMapping.getStock() < 0 ? 0 : itemMapping.getStock()).orElse((double) 0);
                    item.setMaxStock((int) maxStock);
                    item.setAvailableStatus(item.getMaxStock() > 0 ? AvailableStatus.AVAILABLE.name() : AvailableStatus.UNAVAILABLE.name());
                }


                item.setSellingTimeID("SELLING-TIME-01");

                List<String> photos = new ArrayList<>();
                if(itemMaster.getItemMasterImages() != null && !itemMaster.getItemMasterImages().isEmpty()){
                    itemMaster.getItemMasterImages().stream()
                            .sorted(Comparator.comparing(ItemMasterImage::getSequence))
                            .forEach(itemImage -> photos.add(imgLink + "/" + itemImage.getPath()));
                }
                item.setPhotos(photos);

                WeightDTO weight = new WeightDTO();
                weight.setUnit(itemMapping.getGrabmartUnit());
                weight.setValue(itemMapping.getValue());
                weight.setCount(1);

                item.setWeight(weight);
                item.setSequence(1);

                return item;
            }
            return null;
        }).collect(Collectors.toList());
    }
}
