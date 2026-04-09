package com.oxysystem.general.service.grab.client.grabMart;

import com.oxysystem.general.service.grab.client.common.GrabOnboardingCommon;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class GrabMartOnboardingService {
    private final WebClient grabMartClient;

    public GrabMartOnboardingService(WebClient grabMartClient) {
        this.grabMartClient = grabMartClient;
    }

    public ResponseEntity<?> createSelfJourney(String token, String merchantID){
        return GrabOnboardingCommon.createSelfJourney(grabMartClient, token, merchantID);
    }
}
