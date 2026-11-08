package org.aystudios.Skincare.util;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        // 32+ bytes secret key
        String secret = "test-secret-key-that-is-at-least-32-bytes-long-for-hs256";
        jwtUtil = new JwtUtil(secret, 60000, 3600000); // 1 min access, 1 hr refresh
    }

    @Test
    void testGenerateAndValidateAccessToken() {
        String token = jwtUtil.generateAccessToken(10L, "user@example.com", "USER");
        assertNotNull(token);

        Optional<Claims> claimsOpt = jwtUtil.validateAccessToken(token);
        assertTrue(claimsOpt.isPresent());

        Claims claims = claimsOpt.get();
        assertEquals(10L, jwtUtil.extractUserId(claims));
        assertEquals("user@example.com", jwtUtil.extractEmail(claims));
        assertEquals("USER", jwtUtil.extractRole(claims));
    }

    @Test
    void testRefreshTokenCannotBeUsedAsAccessToken() {
        String refreshToken = jwtUtil.generateRefreshToken(10L, "user@example.com");
        assertNotNull(refreshToken);

        // Refresh token must NOT validate as access token
        Optional<Claims> accessClaims = jwtUtil.validateAccessToken(refreshToken);
        assertTrue(accessClaims.isEmpty());

        // Refresh token must validate as refresh token
        Optional<Claims> refreshClaims = jwtUtil.validateRefreshToken(refreshToken);
        assertTrue(refreshClaims.isPresent());
        assertEquals(10L, jwtUtil.extractUserId(refreshClaims.get()));
        assertEquals("user@example.com", jwtUtil.extractEmail(refreshClaims.get()));
    }

    @Test
    void testShortSecretDoesNotThrowExceptionDueToSha256Padding() {
        // Passphrase shorter than 32 bytes
        JwtUtil utilWithShortSecret = new JwtUtil("short-secret", 60000, 3600000);
        String token = utilWithShortSecret.generateAccessToken(1L, "admin@example.com", "ADMIN");
        assertNotNull(token);
        assertTrue(utilWithShortSecret.validateAccessToken(token).isPresent());
    }

    @Test
    void testExpiredTokenValidation() throws InterruptedException {
        // Token expiring in 1 ms
        JwtUtil expiringUtil = new JwtUtil("test-secret-key-that-is-at-least-32-bytes-long-for-hs256", 1, 1);
        String token = expiringUtil.generateAccessToken(1L, "user@example.com", "USER");

        Thread.sleep(10);
        assertFalse(expiringUtil.validateAccessToken(token).isPresent());
    }
}

