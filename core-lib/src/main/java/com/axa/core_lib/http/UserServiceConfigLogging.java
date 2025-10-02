package com.axa.core_lib.http;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
public class UserServiceConfigLogging {


    private String userServiceBaseUrl = "http://localhost:8080";

    @Bean
    public WebClient userServiceWebClient() {
        return WebClientFactory.create(userServiceBaseUrl);
    }

    @Bean
    public UserServiceClientLogging userServiceClient(WebClient userServiceWebClient) {
        return new UserServiceClientLogging(userServiceWebClient);
    }
}
