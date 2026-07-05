package com.yrsd.medcheck.config;

import com.yrsd.medcheck.exceptions.PaymentGatewayClientException;
import com.yrsd.medcheck.exceptions.PaymentGatewayServerException;
import com.yrsd.medcheck.interceptor.PaystackLoggingInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestClient;

import java.io.IOException;

import java.nio.charset.StandardCharsets;

@Configuration
public class PaystackClientConfig {

    @Bean
    public RestClient paystackRestClient(
            @Value("${paystack.secret-key}") String secretKey,
            @Value("${paystack.base-url}") String baseUrl) {

        return RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "Bearer " + secretKey)
                .defaultHeader("Content-Type", "application/json")
//                .requestInterceptor(new PaystackLoggingInterceptor())
                .defaultStatusHandler(HttpStatusCode::is4xxClientError, this::handleClientError)
                .defaultStatusHandler(HttpStatusCode::is5xxServerError, this::handleServerError)
                .build();
    }

    private void handleClientError(HttpRequest request, ClientHttpResponse response) throws IOException {
        String body = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
        throw new PaymentGatewayClientException(response.getStatusCode().value(), body);
    }

    private void handleServerError(HttpRequest request, ClientHttpResponse response) throws IOException {
        String body = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
        throw new PaymentGatewayServerException(response.getStatusCode().value(), body);
    }
}