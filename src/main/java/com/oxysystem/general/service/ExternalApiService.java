package com.oxysystem.general.service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ExternalApiService {

    //private final WebClient fazpassWebClient;

//    public Mono<ResponseOtpDTO> requestFazpassOtp(RequestOtpDTO request){
//        request.setGatewayKey(whatsappKey);
//
//        return fazpassWebClient.post()
//                .uri("/v1/otp/request")
//                .bodyValue(request)
//                .retrieve()
//                .onStatus(HttpStatus::isError, response ->
//                        response.bodyToMono(FailedResponse.class)
//                                .flatMap(error -> Mono.error(new FazpassException(error.getMessage(), error.getErrors()))))
//                .bodyToMono(ResponseOtpDTO.class);
//
//    }
//
//    public Mono<ResponseOtpDTO> validationFazpassOtp(VerifyOtpRequestDTO request){
//        return fazpassWebClient.post()
//                .uri("/v1/otp/verify")
//                .bodyValue(request)
//                .retrieve()
//                .onStatus(HttpStatus::isError, response ->
//                        response.bodyToMono(FailedResponse.class)
//                                .flatMap(error -> Mono.error(new FazpassException(error.getMessage(), error.getErrors()))))
//                .bodyToMono(ResponseOtpDTO.class)
//                .flatMap(response -> Mono.just(new ResponseOtpDTO(response.isStatus(),response.getMessage(),null)));
//    }
}
