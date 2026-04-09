package com.oxysystem.general.service.grab.partner.grabFood;

import com.oxysystem.general.dto.grab.data.OAuthRequestDTO;
import com.oxysystem.general.dto.grab.data.OAuthResponseDTO;
import com.oxysystem.general.service.grab.partner.common.GrabPartnerOAuthCommon;
import com.oxysystem.general.service.grab.partner.common.interfaces.GrabOAuthPartnerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class GrabFoodOAuthPartnerServiceImpl implements GrabOAuthPartnerService {
    @Value("${grabfood.client-id}")
    private String clientId;

    @Value("${grabfood.client-secret}")
    private String clientSecret;

    private final GrabPartnerOAuthCommon grabPartnerOAuthCommon;

    public GrabFoodOAuthPartnerServiceImpl(GrabPartnerOAuthCommon grabPartnerOAuthCommon) {
        this.grabPartnerOAuthCommon = grabPartnerOAuthCommon;
    }

    @Override
    public ResponseEntity<?> generateTokens(OAuthRequestDTO oAuthRequestDTO) {
        OAuthResponseDTO response = grabPartnerOAuthCommon.generateTokens(oAuthRequestDTO, clientId, clientSecret, "grabfood-partner");
        return ResponseEntity.ok(response);
    }
}
