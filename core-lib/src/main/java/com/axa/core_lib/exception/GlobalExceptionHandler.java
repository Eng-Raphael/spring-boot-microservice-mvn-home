package com.axa.core_lib.exception;

import com.axa.core_lib.util.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiResponse<String>> handleInvalidCredentials(InvalidCredentialsException ex) {
        ApiResponse<String> response = new ApiResponse<>(
                "Authentication Error",
                ex.getMessage(),
                "401"
        );
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<String>> handleUserExists(UserAlreadyExistsException ex) {
        ApiResponse<String> response = new ApiResponse<>(
                "User Registration Error",
                ex.getMessage(),
                "409"
        );
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PaginationException.class)
    public ResponseEntity<ApiResponse<String>> handlePaginationException(PaginationException ex) {
        ApiResponse<String> response = new ApiResponse<>(
                "Pagination Error",
                ex.getMessage(),
                "400"
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoDataFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleNoDataFound(NoDataFoundException ex) {
        ApiResponse<String> response = new ApiResponse<>(
                "No Data Found",
                ex.getMessage(),
                "204"
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGeneric(Exception ex) {
        ApiResponse<String> response = new ApiResponse<>(
                "Server Error",
                ex.getMessage(),
                "500"
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
