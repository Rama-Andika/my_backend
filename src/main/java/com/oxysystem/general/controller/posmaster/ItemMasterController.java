package com.oxysystem.general.controller.posmaster;

import com.oxysystem.general.service.posmaster.ItemMasterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ItemMasterController {
    private final ItemMasterService itemMasterService;

    public ItemMasterController(ItemMasterService itemMasterService) {
        this.itemMasterService = itemMasterService;
    }

    @GetMapping("/item-masters")
    public ResponseEntity<?> getItemMasters(String name, String barcode, String code, Long itemGroupId, Long itemCategoryId,
                                            @RequestParam(required = false, defaultValue = "0") int page,
                                            @RequestParam(required = false, defaultValue = "20") int size){
        return itemMasterService.getItemMastersForApi(name, barcode, code, itemGroupId, itemCategoryId, page, size);
    }

    @GetMapping("/item-masters/with-price")
    public ResponseEntity<?> getItemMasters(String name, String barcode, String code, Long itemGroupId, Long itemCategoryId, Long locationId,
                                            @RequestParam(required = false, defaultValue = "0") int page,
                                            @RequestParam(required = false, defaultValue = "20") int size){
        return itemMasterService.getItemMastersWithPrice(name, barcode, code, itemGroupId, itemCategoryId, locationId, page, size);
    }

    @GetMapping("/item-masters/by-location/{locationId}")
    public ResponseEntity<?> getItemMastersWithPrice(String name, String barcode, String code, String category, @PathVariable Long locationId,
                                            @RequestParam(required = false, defaultValue = "true") boolean availability,
                                            @RequestParam(required = false, defaultValue = "0") int page,
                                            @RequestParam(required = false, defaultValue = "25") int size){
        return itemMasterService.getItemMastersByLocation(name, barcode, code, locationId, category, availability, page, size);
    }

    @GetMapping("/playground/item-masters")
    public ResponseEntity<?> getItemMastersPlayground(){
        return itemMasterService.getItemMastersPlayground();
    }
}
