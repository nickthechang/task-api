package com.nick.taskapp.dto;
/*
 * Standard wrapper for API responses.
 *
 * Provides a consistent response format with success status,
 * response data, and an optional message.
 */
public class ApiResponse<T> {
     /*
     * Indicates whether the request was successful.
     */
    private boolean success;
    /*
     * Response payload returned to the client.
     */
    private T data;
    /*
     * Optional message describing the result.
     */
    private String message;

    public ApiResponse() {}

    public ApiResponse(boolean success, T data, String message) {
        this.success = success;
        this.data = data;
        this.message = message;
    }
    /*
     * Creates a successful response with data only.
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null);
    }
    /*
     * Creates a successful response with a message and data.
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, data, message);
    }
    /*
     * Creates an error response with a message.
     */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, null, message);
    }

    public boolean isSuccess() { return success; }
    public T getData() { return data; }
    public String getMessage() { return message; }
}

/*
ApiResponse<T> is a generic wrapper for your API responses. It standardizes responses with three fields:
success, data, message

1. Response wrapper pattern
Creates one consistent response format for successful and error responses.
2. Generics
ApiResponse<T> can wrap different types, such as:
TaskResponseDto
PaginatedResponseDto<TaskResponseDto>
Void
AuthResponseDto
Map<String, String> for validation errors.
3. Static factory methods
The success(...) and error(...) methods make response creation cleaner and more readable.


It is your response envelope — the outer JSON shell your API uses to stay consistent.
*/