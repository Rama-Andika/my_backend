package com.oxysystem.general.controller.grab.client.grabMart;

import com.oxysystem.general.service.grab.client.grabMart.GrabMartOnboardingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/grabmart")
public class GrabMartOnboardingController {
    private final GrabMartOnboardingService grabMartOnboardingService;

    public GrabMartOnboardingController(GrabMartOnboardingService grabMartOnboardingService) {
        this.grabMartOnboardingService = grabMartOnboardingService;
    }

    @PostMapping("/self-serve/activation")
    public ResponseEntity<?> createSelfJourney(@RequestHeader("Authorization") String bearer, @RequestParam String merchantID){
        String token = bearer.replace("Bearer","").trim();
        return grabMartOnboardingService.createSelfJourney(token, merchantID);
    }
}
