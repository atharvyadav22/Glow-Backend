package org.aystudios.Skincare.service;

import org.aystudios.Skincare.dto.LoginRequestDTO;
import org.aystudios.Skincare.dto.LoginResponseDTO;
import org.aystudios.Skincare.dto.RefreshRequestDTO;
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

    public void signUp(SignUpRequestDTO signUpRequestDTO) {

        if (userRepository.findByEmail(signUpRequestDTO.getEmail()).isPresent()) {
            throw new RuntimeException("User with " + signUpRequestDTO.getEmail() + " already exists");
        }

        UserEntity user = new UserEntity();
        user.setEmail(signUpRequestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequestDTO.getPassword()));
        user.setRole("USER");

        userRepository.save(user);

    }

    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {

        UserEntity user = userRepository.findByEmail(loginRequestDTO.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));

        boolean isPasswordMatch = passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword());

        if (!isPasswordMatch) {
            throw new RuntimeException("Invalid credentials");
        }

        return new LoginResponseDTO(jwtUtil.generateAccessToken(user.getEmail()), jwtUtil.generateRefreshToken(user.getEmail()), "Bearer", jwtUtil.getAccessTokenExpiry());

    }

    public LoginResponseDTO refreshToken(RefreshRequestDTO refreshRequestDTO) {

        if (!jwtUtil.isTokenValid(refreshRequestDTO.getRefreshToken())) {
            throw new RuntimeException("Invalid refresh token");
        }

        return new LoginResponseDTO(jwtUtil.generateAccessToken(jwtUtil.extractEmail(refreshRequestDTO.getRefreshToken())), jwtUtil.generateRefreshToken(jwtUtil.extractEmail(refreshRequestDTO.getRefreshToken())), "Bearer", jwtUtil.getAccessTokenExpiry());
    }

    public List<UserEntity> getAllUsers() {

        return userRepository.findAll();
    }

}
