package com.nick.taskapp.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
/*
 * Service for creating and reading JWT tokens.
 *
 * Handles token signing, expiration, and username extraction
 * for authenticated requests.
 */
@Service
public class JwtService {
    /*
     * Secret key used to sign and verify JWTs.
     * Loaded from application configuration.
     */
    @Value("${jwt.secret}")
    private String secret;
    
    /*
     * Token lifetime in milliseconds.
     * Loaded from application configuration.
     */
    @Value("${jwt.expiration}")
    private long expiration;

    /*
     * Builds the signing key used by the JWT library.
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /*
     * Generates a signed JWT for the authenticated username.
     */
    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }
    /*
     * Extracts the username stored in the token subject.
     */
    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
    /*
     * Checks whether the token belongs to the expected username.
     */
    public boolean isTokenValid(String token, String username) {
        return username.equals(extractUsername(token));
    }
}


/*
JwtService creates and reads JWT tokens.
It handles:
generating a token after login
signing the token with a secret key
setting token expiration
extracting the username from a token
checking whether a token belongs to a username
This is the “token utility” part of your security system.

1. JWT generation
Creates a signed token containing the username.
2. Secret-key signing
Uses jwt.secret from application properties to sign and verify tokens.
3. Expiration time
Uses jwt.expiration to control how long tokens stay valid.
4. Token parsing
Reads the token later to extract the username.
5. Environment-based config
The secret and expiration are not hardcoded. They come from config values.
*/