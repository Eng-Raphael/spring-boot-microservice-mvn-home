package com.axa.test_service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
public class HelloController {

    private final WebClient userServiceWebClient;

    @Autowired
    public HelloController(WebClient userServiceWebClient) {
        this.userServiceWebClient = userServiceWebClient;
    }

    @GetMapping("/hello")
    public ResponseEntity<String> hello(@RequestHeader("Authorization") String authHeader) {

        ApiResponse<String> response = userServiceWebClient.get()
                .uri("/validate")
                .header("Authorization", authHeader)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<String>>() {})
                .block();

        if (response != null && "200".equals(response.getHeader())) {
            String username = response.getBody();
            return ResponseEntity.ok("Hello " + username + ", welcome to test-service!");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }
}
