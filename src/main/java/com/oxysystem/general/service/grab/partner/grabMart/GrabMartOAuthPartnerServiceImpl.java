package com.oxysystem.general.service.grab.partner.grabMart;

import com.oxysystem.general.dto.grab.data.OAuthRequestDTO;
import com.oxysystem.general.dto.grab.data.OAuthResponseDTO;
import com.oxysystem.general.service.grab.partner.common.GrabPartnerOAuthCommon;
import com.oxysystem.general.service.grab.partner.common.interfaces.GrabOAuthPartnerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class GrabMartOAuthPartnerServiceImpl implements GrabOAuthPartnerService {
    @Value("${grabmart.client-id}")
    private String clientId;

    @Value("${grabmart.client-secret}")
    private String clientSecret;

    private final GrabPartnerOAuthCommon grabPartnerOAuthCommon;

    public GrabMartOAuthPartnerServiceImpl(GrabPartnerOAuthCommon grabPartnerOAuthCommon) {
        this.grabPartnerOAuthCommon = grabPartnerOAuthCommon;
    }

    @Override
    public ResponseEntity<?> generateTokens(OAuthRequestDTO oAuthRequestDTO) {
        OAuthResponseDTO response = grabPartnerOAuthCommon.generateTokens(oAuthRequestDTO, clientId, clientSecret, "grabmart-partner");
        return ResponseEntity.ok(response);
    }


}
