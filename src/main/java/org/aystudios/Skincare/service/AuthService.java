package org.aystudios.Skincare.service;

import org.aystudios.Skincare.dto.LoginRequestDTO;
import org.aystudios.Skincare.dto.SignUpRequestDTO;
import org.aystudios.Skincare.entity.UserEntity;
import org.aystudios.Skincare.repository.UserRepository;
import org.aystudios.Skincare.util.JwtUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Qualifier("authService")
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public void signUp(SignUpRequestDTO signUpRequestDTO){

        if(userRepository.findByEmail(signUpRequestDTO.getEmail()).isPresent()){
            throw new RuntimeException("User with " + signUpRequestDTO.getEmail() + " already exists");
        }

        UserEntity user = new UserEntity();
        user.setEmail(signUpRequestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequestDTO.getPassword()));
        user.setRole("USER");

        userRepository.save(user);

    }

    public String login(LoginRequestDTO loginRequestDTO){

        UserEntity user = userRepository.findByEmail(loginRequestDTO.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));

        boolean isPasswordMatch = passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword());

        if(!isPasswordMatch){
            throw new RuntimeException("Invalid password");
        }

        return jwtUtil.generateToken(user.getEmail());

    }

    public List<UserEntity> getAllUsers(){

        return userRepository.findAll();
    }

}
