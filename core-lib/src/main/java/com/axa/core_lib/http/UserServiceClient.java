package com.axa.core_lib.http;

import com.axa.core_lib.util.ApiResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;

public class UserServiceClient {

    private final WebClient webClient;

    public UserServiceClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public String validateToken(String authHeader) {
        ApiResponse<String> response = webClient.get()
                .uri("/validate")
                .header("Authorization", authHeader)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<String>>() {})
                .block();

        if (response != null && "200".equals(response.getHeader())) {
            return response.getBody();
        }
        throw new RuntimeException("Invalid token");
    }
}
