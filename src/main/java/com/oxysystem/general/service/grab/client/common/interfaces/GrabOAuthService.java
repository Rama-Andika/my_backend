package com.oxysystem.general.service.grab.client.common.interfaces;

import com.oxysystem.general.dto.grab.data.OAuthResponseDTO;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface GrabOAuthService {
    ResponseEntity<?> getAccessTokenForApi();
    OAuthResponseDTO getAccessToken();
    String getGrabToken();
    Mono<String> getGrabTokenReactive();
}
