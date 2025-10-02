package com.axa.test_service;


import com.axa.core_lib.http.UserServiceClientLogging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    private final UserServiceClientLogging testServiceUserClient;

    @Autowired
    public HelloController(@Qualifier("testServiceUserClient") UserServiceClientLogging client) {
        this.testServiceUserClient = client;
    }

    @GetMapping("/hello")
    public ResponseEntity<String> hello(@RequestHeader("Authorization") String authHeader) {
        String username = testServiceUserClient.validateToken(authHeader);
        return ResponseEntity.ok("Hello " + username + ", welcome to test-service!");
    }


}
