package com.axa.user_service.controller;


import com.axa.core_lib.exception.UserAlreadyExistsException;
import com.axa.core_lib.exception.InvalidCredentialsException;
import com.axa.user_service.entity.User;
import com.axa.user_service.service.JwtService;
import com.axa.user_service.service.UserService;
import com.axa.core_lib.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping("register")
    public ApiResponse<User> register(@RequestBody User user) {
        if(userService.checkIfUserExsists(user.getUsername())){
            throw new UserAlreadyExistsException("User Already Exists");
        }
        return new ApiResponse<>("register success", userService.saveUser(user), "200");
    }

    @PostMapping("login")
    public ApiResponse<String> login(@RequestBody User user){

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        if(authentication.isAuthenticated()) {
            String token = jwtService.generateToken(user.getUsername());
            return new ApiResponse<>("Login success", token, "200");
        }
        else {
            throw new InvalidCredentialsException("Invalid username or password");
        }
    }

    @GetMapping("validate")
    public ApiResponse<String> validateToken(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String username = jwtService.extractUserName(token);
        if (jwtService.validateToken(token, username)) {
            return new ApiResponse<>("Valid token", username, "200");
        } else {
            throw new InvalidCredentialsException("Invalid token");
        }
    }

    @GetMapping("get-userid")
    public ApiResponse<Integer> getUserId(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String username = jwtService.extractUserName(token);
        if (jwtService.validateToken(token, username)) {
            int userId = userService.fetchUserId(username);
            return new ApiResponse<>("Valid token", userId, "200");
        } else {
            throw new InvalidCredentialsException("Invalid token");
        }
    }



}
