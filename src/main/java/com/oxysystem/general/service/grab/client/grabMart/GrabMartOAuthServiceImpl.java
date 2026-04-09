package com.oxysystem.general.service.grab.client.grabMart;

import com.oxysystem.general.dto.grab.data.OAuthRequestDTO;
import com.oxysystem.general.dto.grab.data.OAuthResponseDTO;
import com.oxysystem.general.enums.grab.Product;
import com.oxysystem.general.response.FailedResponse;
import com.oxysystem.general.response.SuccessResponse;
import com.oxysystem.general.service.grab.client.common.GrabOAuthCommon;
import com.oxysystem.general.service.grab.client.common.interfaces.GrabOAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class GrabMartOAuthServiceImpl implements GrabOAuthService {
    @Value("${grabmart.client-id}")
    private String clientId;

    @Value("${grabmart.client-secret}")
    private String clientSecret;

    private final WebClient grabAccessTokenClient;

    public GrabMartOAuthServiceImpl(WebClient grabAccessTokenClient) {
        this.grabAccessTokenClient = grabAccessTokenClient;
    }

    @Override
    public String getGrabToken() {
        try{
            OAuthResponseDTO response = getAccessToken();
            return response.getAccessToken();
        }catch (Exception e){
            log.error("Get grab mart token failed! {}",e.getMessage());
            return "";
        }
    }

    @Override
    public Mono<String> getGrabTokenReactive() {
        OAuthRequestDTO oAuthRequestDTO = new OAuthRequestDTO();
        oAuthRequestDTO.setClientId(clientId);
        oAuthRequestDTO.setClientSecret(clientSecret);
        oAuthRequestDTO.setGrantType("client_credentials");
        oAuthRequestDTO.setScope("mart.partner_api");

        return GrabOAuthCommon.getGrabTokenReactive(grabAccessTokenClient, oAuthRequestDTO);
    }

    @Override
    public ResponseEntity<?> getAccessTokenForApi() {
        OAuthResponseDTO response = getAccessToken();

        if(response != null){
            SuccessResponse<String> successResponse = new SuccessResponse<>("success", response.getAccessToken());
            return ResponseEntity.ok(successResponse);
        }else{
            FailedResponse<String> failedResponse = new FailedResponse<>("OAuth Error", "No error description provided.");
            return ResponseEntity.badRequest().body(failedResponse);
        }
    }

    @Override
    public OAuthResponseDTO getAccessToken() {
        return GrabOAuthCommon.apiAccessToken(clientId, clientSecret, grabAccessTokenClient, Product.GRAB_MART.name());
    }
}
