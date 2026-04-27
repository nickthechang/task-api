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

/*
 * Handles application exceptions globally.
 *
 * Converts known exceptions into consistent API error responses
 * so controllers do not need repetitive try/catch blocks.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /*
     * Handles missing task errors and returns 404 Not Found.
     */
    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(TaskNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage()));
    }

    /*
     * Handles validation errors from @Valid request DTOs.
     *
     * Returns field-specific validation messages.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        ApiResponse<Map<String, String>> response =
                new ApiResponse<>(false, errors, "Validation failed");

        return ResponseEntity.badRequest().body(response);
    }

    /*
     * Handles invalid JSON request bodies or invalid enum values.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidEnum(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest()
                .body(ApiResponse.error("Invalid request body or enum value"));
    }

    /*
     * Handles invalid query/path parameter types.
     *
     * Example: /tasks/priority?level=INVALID
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.badRequest()
                .body(ApiResponse.error("Invalid request parameter: " + ex.getName()));
    }
}

/*
GlobalExceptionHandler is your centralized error handler.
Instead of writing try/catch in every controller method, this class catches known exceptions and turns them into clean API responses.

1. Error happens anywhere in controller/service flow
2. Exception is thrown
3. GlobalExceptionHandler catches it
4. Builds ApiResponse error body
5. Returns correct HTTP status

1. Global exception handling
@RestControllerAdvice applies exception handling across controllers.
2. Exception-specific handlers
@ExceptionHandler(...) catches specific exception types.
3. Validation error mapping
Validation field errors are converted into a map like:
{
  "title": "Title is required"
}
4. Standardized error responses
Errors are wrapped in ApiResponse, matching your normal response style.
*/