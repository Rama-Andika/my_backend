package com.oxysystem.general.controller.grab.partner.grabMart;

import com.oxysystem.general.dto.grab.data.PushIntegrationStatusRequestDTO;
import com.oxysystem.general.dto.grab.view.GrabMenuDTO;
import com.oxysystem.general.service.grab.partner.grabMart.GrabMartOnboardingPartnerServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/api/partner/grabmart")
public class GrabMartOnboardingPartnerController {
    private final GrabMartOnboardingPartnerServiceImpl grabMartOnboardingPartnerService;

    public GrabMartOnboardingPartnerController(GrabMartOnboardingPartnerServiceImpl grabMartOnboardingPartnerService) {
        this.grabMartOnboardingPartnerService = grabMartOnboardingPartnerService;
    }

    @PostMapping("/pushGrabMenu")
    public ResponseEntity<?> pushGrabMenu(@RequestBody GrabMenuDTO grabMenuDTO){
        return grabMartOnboardingPartnerService.pushGrabMenu(grabMenuDTO);
    }

    @PostMapping("/pushIntegrationStatus")
    public ResponseEntity<?> pushIntegrationStatus(@Valid @RequestBody PushIntegrationStatusRequestDTO pushIntegrationStatusRequestDTO){
        return grabMartOnboardingPartnerService.pushIntegrationStatus(pushIntegrationStatusRequestDTO);
    }
}
