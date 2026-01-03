package org.aystudios.Skincare.service;

import org.apache.catalina.User;
import org.aystudios.Skincare.dto.LoginRequest;
import org.aystudios.Skincare.dto.SignUpRequest;
import org.aystudios.Skincare.entity.Users;
import org.aystudios.Skincare.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void signUp(SignUpRequest signUpRequest){

        if(userRepository.findByEmail(signUpRequest.getEmail()).isPresent()){
            throw new RuntimeException("User with " + signUpRequest.getEmail() + " already exists");
        }

        Users user = new Users();
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setRole("USER");

        userRepository.save(user);

    }

    public void login(LoginRequest loginRequest){

        Users user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));

        boolean isPasswordMatch = passwordEncoder.matches(loginRequest.getPassword(), user.getPassword());

        if(!isPasswordMatch){
            throw new RuntimeException("Invalid password");
        }

    }

}
