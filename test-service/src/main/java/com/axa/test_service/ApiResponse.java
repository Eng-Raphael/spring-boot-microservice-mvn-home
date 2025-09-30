package com.axa.test_service;

import lombok.Data;

@Data
public class ApiResponse<T>{


    private String header;

    private T body;

    private String code;

    public ApiResponse(String code, T body, String header) {
        this.code = code;
        this.body = body;
        this.header = header;
    }
}