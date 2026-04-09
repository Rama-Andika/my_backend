package com.oxysystem.general.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GrabClientConfig {
    @Value("${grabmart.api-environments}")
    private String baseMartUrl;

    @Value("${grabfood.api-environments}")
    private String baseFoodUrl;

    @Bean
    public WebClient grabAccessTokenClient(WebClient.Builder builder){
        return builder.baseUrl("https://api.grab.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .build();
    }

    @Bean
    WebClient grabMartClient(WebClient.Builder builder){
        return builder.baseUrl(baseMartUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .build();
    }

    @Bean
    WebClient grabFoodClient(WebClient.Builder builder){
        return builder.baseUrl(baseFoodUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .build();
    }
}
