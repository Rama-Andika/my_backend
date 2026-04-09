package com.oxysystem.general.controller.grab.partner.grabMart;

import com.oxysystem.general.dto.grab.data.OAuthRequestDTO;
import com.oxysystem.general.service.grab.partner.grabMart.GrabMartOAuthPartnerServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/partner/grabmart/oauth")
public class GrabMartOAuthPartnerController {
    private final GrabMartOAuthPartnerServiceImpl oAuthService;

    public GrabMartOAuthPartnerController(GrabMartOAuthPartnerServiceImpl oAuthService) {
        this.oAuthService = oAuthService;
    }

    @PostMapping("/token")
    public ResponseEntity<?> getAccessToken(@RequestBody OAuthRequestDTO oAuthRequestDTO){
        return oAuthService.generateTokens(oAuthRequestDTO);
    }
}
