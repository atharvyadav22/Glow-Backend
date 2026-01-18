package org.aystudios.Skincare.exception.auth;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException() {
        super("Invalid Token");
    }
}
