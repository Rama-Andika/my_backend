package com.oxysystem.general.service.grab.client.common;

import com.oxysystem.general.dto.grab.data.CreateSelfServeRequestDTO;
import com.oxysystem.general.dto.grab.data.CreateSelfServeResponseDTO;
import com.oxysystem.general.exception.GrabException;
import com.oxysystem.general.exception.ResourceNotFoundException;
import com.oxysystem.general.response.SuccessResponse;
import com.oxysystem.general.response.grab.GrabFailedResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class GrabOnboardingCommon {
    public static ResponseEntity<?> createSelfJourney(WebClient webClient, String token, String merchantID){
        if(merchantID == null || merchantID.isEmpty()) throw new ResourceNotFoundException("merchant ID cannot be empty!");

        CreateSelfServeRequestDTO request = new CreateSelfServeRequestDTO();
        CreateSelfServeRequestDTO.Partner partner = new CreateSelfServeRequestDTO.Partner();
        partner.setMerchantID(merchantID);

        request.setPartner(partner);

        CreateSelfServeResponseDTO createSelfServeResponseDTO = webClient.post()
                .uri("/partner/v1/self-serve/activation")
                .header("Authorization", "Bearer " + token)
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatus::isError, clientResponse ->
                        clientResponse.bodyToMono(GrabFailedResponse.class)
                                .switchIfEmpty(Mono.error(new GrabException("Create self serve failed", "unauthorized")))
                                .flatMap(error -> Mono.error(new GrabException(error.getReason(), error.getMessage())))
                )
                .bodyToMono(CreateSelfServeResponseDTO.class)
                .switchIfEmpty(Mono.empty())
                .block();

        SuccessResponse<CreateSelfServeResponseDTO> response = new SuccessResponse<>("success",createSelfServeResponseDTO);
        return ResponseEntity.ok(response);
    }
}
