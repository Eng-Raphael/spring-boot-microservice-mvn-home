package com.axa.test_service;


import com.axa.core_lib.http.UserServiceClient;
import com.axa.core_lib.http.WebClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebClientConfig {

    @Bean
    public UserServiceClient userServiceClient(@Value("${user.service.base-url}") String baseUrl) {
        return new UserServiceClient(WebClientFactory.create(baseUrl));
    }

}
