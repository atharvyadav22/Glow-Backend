package org.aystudios.Skincare.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtUtil {

    private final SecretKey key;
    private final long accessExpiry;
    private final long refreshExpiry;

    public static final String TYPE = "type";
    public static final String ROLE = "role";
    public static final String USER_ID = "userId";

    public static final String ACCESS  = "access";
    public static final String REFRESH = "refresh";

    public JwtUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-expiry:900000}") long accessExpiry,     // Default 15 minutes
            @Value("${jwt.refresh-expiry:604800000}") long refreshExpiry  // Default 7 days
    ) {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            try {
                keyBytes = MessageDigest.getInstance("SHA-256").digest(keyBytes);
            } catch (NoSuchAlgorithmException e) {
                throw new IllegalStateException("SHA-256 algorithm not available", e);
            }
        }
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessExpiry = accessExpiry;
        this.refreshExpiry = refreshExpiry;
    }

    // ---------- TOKEN GENERATION ----------

    public String generateAccessToken(Long userId, String email, String role) {
        return buildToken(userId, email, role, ACCESS, accessExpiry);
    }

    public String generateRefreshToken(Long userId, String email) {
        return buildToken(userId, email, null, REFRESH, refreshExpiry);
    }

    private String buildToken(
            Long userId,
            String email,
            String role,
            String type,
            long expiry
    ) {
        var builder = Jwts.builder()
                .setSubject(email)
                .claim(USER_ID, userId)
                .claim(TYPE, type)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiry))
                .signWith(key, SignatureAlgorithm.HS256);

        if (role != null) {
            builder.claim(ROLE, role);
        }

        return builder.compact();
    }

    // ---------- VALIDATION & CLAIM EXTRACTION ----------

    public Optional<Claims> validateAccessToken(String token) {
        return extractClaimsIfValid(token, ACCESS);
    }

    public Optional<Claims> validateRefreshToken(String token) {
        return extractClaimsIfValid(token, REFRESH);
    }

    private Optional<Claims> extractClaimsIfValid(String token, String expectedType) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            if (expectedType.equals(claims.get(TYPE, String.class))) {
                return Optional.of(claims);
            }
        } catch (JwtException | IllegalArgumentException ignored) {
            // Expired, invalid signature, or malformed token
        }
        return Optional.empty();
    }

    public Long extractUserId(Claims claims) {
        Object val = claims.get(USER_ID);
        if (val instanceof Number number) {
            return number.longValue();
        }
        return null;
    }

    public String extractEmail(Claims claims) {
        return claims.getSubject();
    }

    public String extractRole(Claims claims) {
        return claims.get(ROLE, String.class);
    }

    public long getAccessTokenExpiry() {
        return accessExpiry;
    }

    public long getRefreshTokenExpiry() {
        return refreshExpiry;
    }
}
