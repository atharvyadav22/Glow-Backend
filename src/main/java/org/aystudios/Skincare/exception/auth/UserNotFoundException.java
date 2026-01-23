package org.aystudios.Skincare.exception.auth;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String email){
        super(("User with " + email + " does not exist."));
    }
}
