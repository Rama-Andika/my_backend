package com.oxysystem.general.controller.grab.partner.grabFood;

import com.oxysystem.general.dto.grab.data.PushIntegrationStatusRequestDTO;
import com.oxysystem.general.dto.grab.view.GrabMenuDTO;
import com.oxysystem.general.service.grab.partner.grabFood.GrabFoodOnboardingPartnerServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/api/partner/grabfood")
public class GrabFoodOnboardingPartnerController {
    private final GrabFoodOnboardingPartnerServiceImpl grabFoodOnboardingPartnerService;

    public GrabFoodOnboardingPartnerController(GrabFoodOnboardingPartnerServiceImpl grabFoodOnboardingPartnerService) {
        this.grabFoodOnboardingPartnerService = grabFoodOnboardingPartnerService;
    }

    @PostMapping("/pushIntegrationStatus")
    public ResponseEntity<?> pushIntegrationStatus(@Valid @RequestBody PushIntegrationStatusRequestDTO pushIntegrationStatusRequestDTO){
        return grabFoodOnboardingPartnerService.pushIntegrationStatus(pushIntegrationStatusRequestDTO);
    }

    @PostMapping("/pushGrabMenu")
    public ResponseEntity<?> pushGrabMenu(@RequestBody GrabMenuDTO grabMenuDTO){
        return grabFoodOnboardingPartnerService.pushGrabMenu(grabMenuDTO);
    }
}
