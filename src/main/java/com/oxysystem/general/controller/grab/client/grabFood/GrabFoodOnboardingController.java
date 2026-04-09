package com.oxysystem.general.controller.grab.client.grabFood;

import com.oxysystem.general.service.grab.client.grabFood.GrabFoodOnboardingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/grabfood")
public class GrabFoodOnboardingController {
    private final GrabFoodOnboardingService grabFoodOnboardingService;

    public GrabFoodOnboardingController(GrabFoodOnboardingService grabFoodOnboardingService) {
        this.grabFoodOnboardingService = grabFoodOnboardingService;
    }

    @PostMapping("/self-serve/activation")
    public ResponseEntity<?> createSelfJourney(@RequestHeader("Authorization") String bearer, @RequestParam String merchantID){
        String token = bearer.replace("Bearer","").trim();
        return grabFoodOnboardingService.createSelfJourney(token, merchantID);
    }
}
