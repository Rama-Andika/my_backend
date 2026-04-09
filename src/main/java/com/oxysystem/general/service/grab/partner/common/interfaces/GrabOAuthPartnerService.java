package com.oxysystem.general.service.grab.partner.common.interfaces;

import com.oxysystem.general.dto.grab.data.OAuthRequestDTO;
import org.springframework.http.ResponseEntity;

public interface GrabOAuthPartnerService {
    ResponseEntity<?> generateTokens(OAuthRequestDTO oAuthRequestDTO);
}
