package com.axa.core_lib.http;

import com.axa.core_lib.util.ApiResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;

public class UserServiceClientLogging {

    private final WebClient webClient;

    public UserServiceClientLogging(WebClient webClient) {
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

    public String getUserName(String authHeader) {
        System.out.println("Fetching user name with authHeader: " + authHeader);
        ApiResponse<String> response = webClient.get()
                .uri("/user-name")
                .header("Authorization", authHeader)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<String>>() {})
                .block();

        if (response != null && "200".equals(response.getHeader()))
        {
            return response.getBody();
        }
        throw new RuntimeException("Invalid token");
    }
}
