package com.oxysystem.general.controller.grab.partner.grabFood;

import com.oxysystem.general.dto.grab.data.OAuthRequestDTO;
import com.oxysystem.general.service.grab.partner.grabFood.GrabFoodOAuthPartnerServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/partner/grabfood/oauth")
public class GrabFoodOAuthPartnerController {
    private final GrabFoodOAuthPartnerServiceImpl oAuthService;

    public GrabFoodOAuthPartnerController(GrabFoodOAuthPartnerServiceImpl oAuthService) {
        this.oAuthService = oAuthService;
    }


    @PostMapping("/token")
    public ResponseEntity<?> getAccessToken(@RequestBody OAuthRequestDTO oAuthRequestDTO){
        return oAuthService.generateTokens(oAuthRequestDTO);
    }
}
