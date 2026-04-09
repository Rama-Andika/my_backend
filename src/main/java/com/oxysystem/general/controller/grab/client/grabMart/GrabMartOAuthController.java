package com.oxysystem.general.controller.grab.client.grabMart;

import com.oxysystem.general.service.grab.client.grabMart.GrabMartOAuthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/grabmart")
public class GrabMartOAuthController {
    private final GrabMartOAuthServiceImpl grabMartOAuthService;

    public GrabMartOAuthController(GrabMartOAuthServiceImpl grabMartOAuthService) {
        this.grabMartOAuthService = grabMartOAuthService;
    }

    @PostMapping("/oauth2/token")
    public ResponseEntity<?> getAccessToken(){
        return grabMartOAuthService.getAccessTokenForApi();
    }
}
