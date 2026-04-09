package com.oxysystem.general.service.grab.client.grabFood;

import com.oxysystem.general.service.grab.client.common.GrabOnboardingCommon;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class GrabFoodOnboardingService {
    private final WebClient grabFoodClient;

    public GrabFoodOnboardingService(WebClient grabFoodClient) {
        this.grabFoodClient = grabFoodClient;
    }

    public ResponseEntity<?> createSelfJourney(String token, String merchantID){
        return GrabOnboardingCommon.createSelfJourney(grabFoodClient, token, merchantID);
    }
}
