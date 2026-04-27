package com.nick.taskapp.auth;
import jakarta.validation.constraints.NotBlank;
/*
 * Request DTO for authentication endpoints.
 *
 * Carries username and password data from the client
 * for registration and login requests.
 */
public class AuthRequestDto {

    @NotBlank(message = "Username is required")
    private String username;
    /*
     * Raw password submitted by the client.
     *
     * This should only be used for authentication logic
     * and must never be stored directly.
     */
    @NotBlank(message = "Password is required")
    private String password;    

    public AuthRequestDto() {
    }

    public AuthRequestDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

/*
AuthRequestDto defines what the client sends for authentication requests.
Used for:
POST /auth/register
POST /auth/login

1. Request DTO
Defines the shape of login/register request data.
2. Request body binding
Spring converts incoming JSON into this object through @RequestBody.
3. Input separation
Keeps raw client input separate from the User entity.
*/