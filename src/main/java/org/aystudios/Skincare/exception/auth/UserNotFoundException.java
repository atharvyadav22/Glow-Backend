package org.aystudios.Skincare.exception.auth;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String emailOrMessage) {
        super(emailOrMessage.contains(" ") ? emailOrMessage : "User with " + emailOrMessage + " does not exist.");
    }
}
