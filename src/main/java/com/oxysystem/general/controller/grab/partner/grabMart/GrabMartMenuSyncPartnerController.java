package com.oxysystem.general.controller.grab.partner.grabMart;


import com.oxysystem.general.dto.grab.data.MenuSyncStateRequestDTO;
import com.oxysystem.general.service.grab.partner.grabMart.GrabMartMenuSyncPartnerServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/partner/grabmart")
public class GrabMartMenuSyncPartnerController {
    private final GrabMartMenuSyncPartnerServiceImpl grabMartMenuSyncPartnerService;

    public GrabMartMenuSyncPartnerController(GrabMartMenuSyncPartnerServiceImpl grabMartMenuSyncPartnerService) {
        this.grabMartMenuSyncPartnerService = grabMartMenuSyncPartnerService;
    }

    @GetMapping("/merchant/menu")
    public ResponseEntity<?> getMartMenu(@RequestParam(defaultValue = "") String merchantID,
                                         @RequestParam(defaultValue = "") String partnerMerchantID){
        return grabMartMenuSyncPartnerService.getMenu(merchantID, partnerMerchantID);
    }

    @PostMapping("/menuSyncState")
    public ResponseEntity<?> menuSyncState(@RequestBody MenuSyncStateRequestDTO menuSyncStateRequestDTO){
        return grabMartMenuSyncPartnerService.menuSyncState(menuSyncStateRequestDTO);
    }
}
