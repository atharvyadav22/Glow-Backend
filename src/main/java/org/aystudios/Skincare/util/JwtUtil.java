package org.aystudios.Skincare.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey key;

    private static final String TYPE = "type";
    private static final String ROLE = "role";
    private static final String USER_ID = "userId";

    private static final String ACCESS  = "access";
    private static final String REFRESH = "refresh";

//    private static final long ACCESS_EXPIRY = 15 * 60 * 1000;
    private static final long ACCESS_EXPIRY  = 7 * 24 * 60 * 60 * 1000;
    private static final long REFRESH_EXPIRY = 7 * 24 * 60 * 60 * 1000;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    // ---------- TOKEN GENERATION ----------

    public String generateAccessToken(Long userId, String email, String role) {
        return buildToken(userId, email, role, ACCESS, ACCESS_EXPIRY);
    }

    public String generateRefreshToken(Long userId, String email) {
        return buildToken(userId, email, null, REFRESH, REFRESH_EXPIRY);
    }

    private String buildToken(
            Long userId,
            String email,
            String role,
            String type,
            long expiry
    ) {
        return Jwts.builder()
                .setSubject(email)
                .claim(USER_ID, userId)
                .claim(TYPE, type)
                .claim(ROLE, role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiry))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // ---------- VALIDATION ----------

    public boolean isValidAccessToken(String token) {
        return isValid(token, ACCESS);
    }

    public boolean isValidRefreshToken(String token) {
        return isValid(token, REFRESH);
    }

    private boolean isValid(String token, String expectedType) {
        try {
            Claims claims = extractAllClaims(token);
            return expectedType.equals(claims.get(TYPE));
        } catch (Exception e) {
            return false;
        }
    }

    // ---------- CLAIM EXTRACTION ----------

    public Long extractUserId(String token) {
        return extractAllClaims(token).get(USER_ID, Long.class);
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return extractAllClaims(token).get(ROLE, String.class);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public long getAccessTokenExpiry() {
        return ACCESS_EXPIRY;
    }
}



