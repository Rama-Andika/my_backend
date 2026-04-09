package com.oxysystem.general.service.grab.client.common;

import com.oxysystem.general.dto.grab.data.ListOrderResponseDTO;
import com.oxysystem.general.exception.GrabException;
import com.oxysystem.general.exception.ResourceNotFoundException;
import com.oxysystem.general.exception.ResourceUnauthorizedException;
import com.oxysystem.general.response.SuccessResponse;
import com.oxysystem.general.response.grab.GrabFailedResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;

@Component
@Slf4j
public class GrabOrderSyncCommon {
    @Value("${grabmart.endpoint.list-order}")
    private String endpointListOrder;

    private ListOrderResponseDTO listOrderApi(WebClient webClient, String queryParams, String token) {
        return webClient.get()
                .uri(endpointListOrder + "?" + queryParams)
                .header("Authorization","Bearer " + token)
                .retrieve()
                .onStatus(HttpStatus::isError, clientResponse ->
                        clientResponse.bodyToMono(GrabFailedResponse.class)
                                .switchIfEmpty(Mono.error(new GrabException("Get list order error","unauthorized")))
                                .flatMap(error -> Mono.error(new GrabException(error.getReason(), error.getMessage())))
                )
                .bodyToMono(ListOrderResponseDTO.class)
                .block();
    }

    private String getQueryParams(String merchantID, String date, Integer page, List<String> orderIDs){
        StringBuilder queryParams = new StringBuilder("merchantID=" + merchantID);

        if (date != null && !date.isEmpty()) {
            queryParams.append("&date=").append(date);
        }
        if (page != null) {
            queryParams.append("&page=").append(page);
        }
        if (orderIDs != null && !orderIDs.isEmpty()) {
            for (String orderID : orderIDs) {
                queryParams.append("&orderIDs=").append(orderID);
            }
        }

        return queryParams.toString();
    }

    public ResponseEntity<?> listOrder(WebClient webClient, String token, String merchantID, String date, Integer page, List<String> orderIds) {
        if(token == null || token.isEmpty()) throw new ResourceUnauthorizedException("authorization failed!");
        if(merchantID == null || merchantID.isEmpty()) throw new ResourceNotFoundException("merchant ID cannot empty!");

        String queryParams = getQueryParams(merchantID, date, page, orderIds);

        ListOrderResponseDTO listOrderResponseDTO = listOrderApi(webClient, queryParams, token);

        SuccessResponse<ListOrderResponseDTO> response = new SuccessResponse<>("success", listOrderResponseDTO);
        return ResponseEntity.ok(response);
    }

    public ListOrderResponseDTO getListOrder(WebClient webClient, String token, String merchantID, String date, Integer page, List<String> orderIDs) {
        if(token == null || token.isEmpty()) throw new ResourceUnauthorizedException("authorization failed!");
        if(merchantID == null || merchantID.isEmpty()) throw new ResourceNotFoundException("merchant ID cannot empty!");

        String queryParams = getQueryParams(merchantID, date, page, orderIDs);

        return listOrderApi(webClient, queryParams, token);
    }

    public Mono<ListOrderResponseDTO> getListOrderReactive(WebClient webClient, String token, String merchantID, String date, Integer page, List<String> orderIDs) {
        if (token == null || token.isEmpty()) {
            return Mono.error(new ResourceUnauthorizedException("authorization failed!"));
        }
        if (merchantID == null || merchantID.isEmpty()) {
            return Mono.error(new ResourceNotFoundException("merchant ID cannot empty!"));
        }

        String queryParams = getQueryParams(merchantID, date, page, orderIDs);

        return webClient.get()
                .uri(endpointListOrder + "?" + queryParams)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .onStatus(HttpStatus::isError, clientResponse ->
                        clientResponse.bodyToMono(GrabFailedResponse.class)
                                .switchIfEmpty(Mono.error(new GrabException("Get list order error", "unauthorized")))
                                .flatMap(error -> Mono.error(new GrabException(error.getReason(), error.getMessage())))
                )
                .bodyToMono(ListOrderResponseDTO.class)
                .timeout(Duration.ofSeconds(15)) // ⏱️ batasi agar tidak menggantung selamanya
                .doOnSuccess(response -> {
                    log.info("[OK] Get list order for merchantId={} page={}", merchantID, page);
                })
                .doOnError(e -> {
                    log.warn("[FAIL] Get list order merchantId={} page={} -> {}", merchantID, page, e.getMessage());
                })
                .retryWhen(
                        Retry.backoff(3, Duration.ofSeconds(1))
                                .maxBackoff(Duration.ofSeconds(10))
                                .jitter(0.5)
                                .filter(throwable ->
                                        throwable instanceof WebClientRequestException ||
                                                throwable instanceof WebClientResponseException ||
                                                throwable instanceof GrabException
                                )
                );
    }
}
