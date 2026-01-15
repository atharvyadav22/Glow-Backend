package org.aystudios.Skincare.service;

import org.aystudios.Skincare.dto.LoginRequestDTO;
import org.aystudios.Skincare.dto.LoginResponseDTO;
import org.aystudios.Skincare.dto.RefreshRequestDTO;
import org.aystudios.Skincare.dto.SignUpRequestDTO;
import org.aystudios.Skincare.entity.UserEntity;
import org.aystudios.Skincare.exception.auth.InvalidPasswordException;
import org.aystudios.Skincare.exception.auth.UserAlreadyExistedException;
import org.aystudios.Skincare.exception.auth.UserNotFoundException;
import org.aystudios.Skincare.repository.UserRepository;
import org.aystudios.Skincare.util.JwtUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Qualifier("authService")
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
            throw new UserAlreadyExistedException(signUpRequestDTO.getEmail());
        }

        UserEntity user = new UserEntity();
        user.setEmail(signUpRequestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequestDTO.getPassword()));
        user.setRole("USER");

        userRepository.save(user);
    }

    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        UserEntity user = userRepository.findByEmail(loginRequestDTO.getEmail()).orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
            throw new InvalidPasswordException();
        }

        return new LoginResponseDTO(
                jwtUtil.generateAccessToken(user.getEmail()),
                jwtUtil.generateRefreshToken(user.getEmail()),
                "Bearer",
                jwtUtil.getAccessTokenExpiry()
        );
    }

    public LoginResponseDTO refreshToken(RefreshRequestDTO refreshRequestDTO) {
        if (!jwtUtil.isTokenValid(refreshRequestDTO.getRefreshToken())) {
            throw new RuntimeException("Invalid refresh token");
        }
        String email = jwtUtil.extractEmail(refreshRequestDTO.getRefreshToken());
        return new LoginResponseDTO(jwtUtil.generateAccessToken(email), jwtUtil.generateRefreshToken(email), "Bearer", jwtUtil.getAccessTokenExpiry());
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

}
