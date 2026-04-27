package com.nick.taskapp.auth;
/*
 * Response DTO returned after successful authentication.
 *
 * Contains the JWT used by the client for authenticated requests.
 */
public class AuthResponseDto {
    /*
     * JWT access token issued after login.
     */
    private String token;

    public AuthResponseDto() {
    }

    public AuthResponseDto(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}

/*
AuthResponseDto defines what the API returns after a successful login.
Right now, it returns:
JWT token

1. Response DTO
Controls the shape of auth output.
2. Token response pattern
Returns a JWT to the client so future requests can include it in the Authorization header.


*/