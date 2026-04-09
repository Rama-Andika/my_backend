package com.oxysystem.general.controller.grab.partner.grabFood;

import com.oxysystem.general.dto.grab.data.MenuSyncStateRequestDTO;
import com.oxysystem.general.service.grab.partner.grabFood.GrabFoodMenuSyncPartnerServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/partner/grabfood")
public class GrabFoodMenuSyncPartnerController {
    private final GrabFoodMenuSyncPartnerServiceImpl grabFoodMenuSyncPartnerService;

    public GrabFoodMenuSyncPartnerController(GrabFoodMenuSyncPartnerServiceImpl grabMenuSyncPartnerService) {
        this.grabFoodMenuSyncPartnerService = grabMenuSyncPartnerService;
    }

    @PostMapping("/menuSyncState")
    public ResponseEntity<?> menuSyncState(@RequestBody MenuSyncStateRequestDTO menuSyncStateRequestDTO){
        return grabFoodMenuSyncPartnerService.menuSyncState(menuSyncStateRequestDTO);
    }

    @GetMapping("/merchant/menu")
    public ResponseEntity<?> getMartMenu(@RequestParam(defaultValue = "") String merchantID,
                                         @RequestParam(defaultValue = "") String partnerMerchantID){
        return grabFoodMenuSyncPartnerService.getMenu(merchantID, partnerMerchantID);
    }
}
