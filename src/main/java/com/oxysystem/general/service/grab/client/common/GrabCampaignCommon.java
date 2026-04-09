package com.oxysystem.general.service.grab.client.common;

import com.oxysystem.general.dto.grab.view.CampaignViewDTO;
import com.oxysystem.general.exception.GrabException;
import com.oxysystem.general.response.SuccessResponse;
import com.oxysystem.general.response.grab.GrabFailedResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class GrabCampaignCommon {
    @Value("${grabmart.endpoint.list-campaigns}")
    private String endpointListCampaign;

    @Value("${grabmart.endpoint-delete-campaign}")
    private String endpointDeleteCampaign;

    public ResponseEntity<?> listCampaign(WebClient webClient, String merchantID, String token) {
        CampaignViewDTO content = webClient.get()
                .uri(endpointListCampaign + "?=merchantID=" + merchantID)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .onStatus(HttpStatus::isError, clientResponse -> clientResponse.bodyToMono(GrabFailedResponse.class)
                        .switchIfEmpty(Mono.error(new GrabException("Get list campaign error", "unathorized")))
                        .flatMap(error -> Mono.error(new GrabException(error.getReason(), error.getMessage()))))
                .bodyToMono(CampaignViewDTO.class)
                .block();

        SuccessResponse<?> response = new SuccessResponse<>("success", content);
        return ResponseEntity.ok(response);
    }

    public void deleteCampaign(WebClient webClient, String campaignId, String token) {
        webClient.delete()
                .uri(endpointDeleteCampaign + "/" + campaignId)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .onStatus(HttpStatus::isError, clientResponse -> clientResponse.bodyToMono(GrabFailedResponse.class)
                        .switchIfEmpty(Mono.error(new GrabException("Delete promotion is error", "unathorized")))
                        .flatMap(error -> Mono.error(new GrabException(error.getReason(), error.getMessage()))))
                .bodyToMono(String.class)
                .block();
    }
}
