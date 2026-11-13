package com.nick.taskapp.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.WeakKeyException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {
    // Synthetic keys for tests only. Do not use these in deployments.
    private static final String KEY_A = "TEST-ONLY-signing-key-A-0123456789abcdef0123456789abcdef";
    private static final String KEY_B = "TEST-ONLY-signing-key-B-0123456789abcdef0123456789abcdef";

    @Test
    void rejectsMissingOrWeakSecretsImmediately() {
        assertThrows(IllegalArgumentException.class, () -> new JwtService(null, 60000));
        assertThrows(IllegalArgumentException.class, () -> new JwtService("  ", 60000));
        assertThrows(WeakKeyException.class, () -> new JwtService("weak", 60000));
    }

    @Test
    void rejectsInvalidExpiration() {
        assertThrows(IllegalArgumentException.class, () -> new JwtService(KEY_A, 0));
        assertThrows(IllegalArgumentException.class, () -> new JwtService(KEY_A, -1));
    }

    @Test
    void signsAndChecksTokenSubject() {
        JwtService service = new JwtService(KEY_A, 60000);
        String token = service.generateToken("alice");
        assertEquals("alice", service.extractUsername(token));
        assertTrue(service.isTokenValid(token, "alice"));
        assertFalse(service.isTokenValid(token, "bob"));
    }

    @Test
    void keyRotationInvalidatesOldTokens() {
        String token = new JwtService(KEY_A, 60000).generateToken("alice");
        assertThrows(JwtException.class, () -> new JwtService(KEY_B, 60000).extractUsername(token));
    }
}
