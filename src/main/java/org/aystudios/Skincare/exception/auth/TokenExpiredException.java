package org.aystudios.Skincare.exception.auth;

public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException(String message) {
        super("Token Expired");
    }
}
