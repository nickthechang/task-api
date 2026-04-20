package com.nick.taskapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.nick.taskapp.dto.ApiResponse;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    //handle validation errors globally
    // @ExceptionHandler(MethodArgumentNotValidException.class)
    // public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
    //     Map<String, String> errors = new HashMap<>();
    //     ex.getBindingResult().getFieldErrors()
    //             .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

    //     return ResponseEntity.badRequest().body(errors);
    // }

    //improved global error format
    // @ExceptionHandler(MethodArgumentNotValidException.class)
    // public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
    //     Map<String, Object> response = new HashMap<>();
    //     Map<String, String> errors = new HashMap<>();

    //     ex.getBindingResult().getFieldErrors()
    //             .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

    //     response.put("message", "Validation failed");
    //     response.put("errors", errors);

    //     return ResponseEntity.badRequest().body(response);
    // }


    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(TaskNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        ApiResponse<Map<String, String>> response =
                new ApiResponse<>(false, errors, "Validation failed");

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidEnum(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest()
                .body(ApiResponse.error("Invalid request body or enum value"));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.badRequest()
                .body(ApiResponse.error("Invalid request parameter: " + ex.getName()));
    }
}