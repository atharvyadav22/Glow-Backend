package org.aystudios.Skincare.exception.auth;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException() {
        super("Invalid email or password");
    }
}
