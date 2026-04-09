package com.oxysystem.general.controller.grab.client.grabFood;

import com.oxysystem.general.service.grab.client.grabFood.GrabFoodOAuthServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/grabfood")
public class GrabFoodOAuthController {
    private final GrabFoodOAuthServiceImpl grabFoodOAuthService;

    public GrabFoodOAuthController(GrabFoodOAuthServiceImpl grabFoodOAuthService) {
        this.grabFoodOAuthService = grabFoodOAuthService;
    }

    @PostMapping("/oauth2/token")
    public ResponseEntity<?> getAccessToken(){
        return grabFoodOAuthService.getAccessTokenForApi();
    }
}
