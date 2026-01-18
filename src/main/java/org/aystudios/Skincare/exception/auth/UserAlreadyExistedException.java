package org.aystudios.Skincare.exception.auth;

public class UserAlreadyExistedException extends RuntimeException{
    public UserAlreadyExistedException(String email){
        super("User with "+email+" already existed.");
    }
}
