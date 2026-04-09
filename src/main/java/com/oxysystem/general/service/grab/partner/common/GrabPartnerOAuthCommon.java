package com.oxysystem.general.service.grab.partner.common;

import com.oxysystem.general.dto.grab.data.OAuthRequestDTO;
import com.oxysystem.general.dto.grab.data.OAuthResponseDTO;
import com.oxysystem.general.exception.ResourceUnauthorizedException;
import com.oxysystem.general.util.JwtUtil;
import org.springframework.stereotype.Component;

@Component
public class GrabPartnerOAuthCommon {
    private final JwtUtil jwtUtil;

    public GrabPartnerOAuthCommon(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public OAuthResponseDTO generateTokens(OAuthRequestDTO oAuthRequestDTO, String clientId, String clientSecret, String username) {
        if(!oAuthRequestDTO.getClientId().equals(clientId) || !oAuthRequestDTO.getClientSecret().equals(clientSecret)){
            throw new ResourceUnauthorizedException("invalid client credentials");
        }
        String accessToken = jwtUtil.generateAccessToken(username);

        return new OAuthResponseDTO(accessToken, "Bearer", 604_800);
    }
}
