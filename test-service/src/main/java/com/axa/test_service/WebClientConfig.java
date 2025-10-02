package com.axa.test_service;


import com.axa.core_lib.http.UserServiceClientLogging;
import com.axa.core_lib.http.WebClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebClientConfig {

    @Bean(name = "testServiceUserClient")
    public UserServiceClientLogging testServiceUserClient(@Value("${user.service.base-url}") String baseUrl) {
        return new UserServiceClientLogging(WebClientFactory.create(baseUrl));
    }

}
