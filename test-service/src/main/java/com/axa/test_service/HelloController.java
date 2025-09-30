package com.axa.test_service;


import com.axa.core_lib.http.UserServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    private final UserServiceClient userServiceClient;

    @Autowired
    public HelloController(UserServiceClient userServiceClient) {
        this.userServiceClient = userServiceClient;
    }

    @GetMapping("/hello")
    public ResponseEntity<String> hello(@RequestHeader("Authorization") String authHeader) {
        String username = userServiceClient.validateToken(authHeader);
        return ResponseEntity.ok("Hello " + username + ", welcome to test-service!");
    }
}
