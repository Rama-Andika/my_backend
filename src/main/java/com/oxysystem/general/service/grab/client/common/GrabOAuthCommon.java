package com.oxysystem.general.service.grab.client.common;


import com.oxysystem.general.dto.grab.data.OAuthRequestDTO;
import com.oxysystem.general.dto.grab.data.OAuthResponseDTO;
import com.oxysystem.general.enums.grab.Product;
import com.oxysystem.general.exception.GrabException;
import com.oxysystem.general.response.grab.GrabMartAccessTokenFailedResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
public class GrabOAuthCommon {
    public static OAuthResponseDTO apiAccessToken(String clientId, String clientSecret, WebClient webClient, String product){
        OAuthRequestDTO oAuthRequestDTO = new OAuthRequestDTO();
        oAuthRequestDTO.setClientId(clientId);
        oAuthRequestDTO.setClientSecret(clientSecret);
        oAuthRequestDTO.setGrantType("client_credentials");

        if(product.equals(Product.GRAB_MART.name())){
            oAuthRequestDTO.setScope("mart.partner_api");
        } else if (product.equals(Product.GRAB_FOOD.name())) {
            oAuthRequestDTO.setScope("food.partner_api");
        }


        return webClient.post()
                .uri("/grabid/v1/oauth2/token")
                .bodyValue(oAuthRequestDTO)
                .retrieve()
                .onStatus(HttpStatus::isError, clientResponse ->
                        clientResponse.bodyToMono(GrabMartAccessTokenFailedResponse.class)
                                .switchIfEmpty(Mono.error(new GrabException("OAuth error", "No error description provided.")))
                                .flatMap(error -> Mono.error(new GrabException("OAuth error", error.getErrorDescription()))))
                .bodyToMono(OAuthResponseDTO.class)
                .block();
    }

    public static Mono<String> getGrabTokenReactive(WebClient webClient, OAuthRequestDTO body){
        return webClient.post()
                .uri("/grabid/v1/oauth2/token")
                .bodyValue(body)
                .retrieve()
                .onStatus(HttpStatus::isError, clientResponse ->
                        clientResponse.bodyToMono(GrabMartAccessTokenFailedResponse.class)
                                .switchIfEmpty(Mono.error(new GrabException("OAuth error", "No error description provided.")))
                                .flatMap(error -> Mono.error(new GrabException("OAuth error", error.getErrorDescription()))))
                .bodyToMono(OAuthResponseDTO.class)
                .map(OAuthResponseDTO::getAccessToken) // ambil token
                .doOnError(e -> log.error("Get grab token failed! {}", e.getMessage()));
    }
}
