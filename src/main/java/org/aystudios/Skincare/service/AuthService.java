package org.aystudios.Skincare.service;

import org.aystudios.Skincare.dto.LoginRequestDTO;
import org.aystudios.Skincare.dto.LoginResponseDTO;
import org.aystudios.Skincare.dto.RefreshRequestDTO;
import org.aystudios.Skincare.dto.SignUpRequestDTO;
import org.aystudios.Skincare.entity.Role;
import org.aystudios.Skincare.entity.UserEntity;
import org.aystudios.Skincare.exception.auth.InvalidPasswordException;
import org.aystudios.Skincare.exception.auth.InvalidTokenException;
import org.aystudios.Skincare.exception.auth.UserAlreadyExistedException;
import org.aystudios.Skincare.exception.auth.UserNotFoundException;
import org.aystudios.Skincare.repository.UserRepository;
import org.aystudios.Skincare.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // -------- SIGN UP --------
    public void signUp(SignUpRequestDTO dto) {

        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new UserAlreadyExistedException(dto.getEmail());
        }
        userRepository.findByEmail(dto.getEmail());

        UserEntity user = new UserEntity();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.USER);

        userRepository.save(user);
    }

    // -------- LOGIN --------
    public LoginResponseDTO login(LoginRequestDTO dto) {

        UserEntity user = userRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new UserNotFoundException(dto.getEmail()));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new InvalidPasswordException();
        }

        return new LoginResponseDTO(
                jwtUtil.generateAccessToken(
                        user.getEmail(),
                        user.getRole().name()
                ),
                jwtUtil.generateRefreshToken(
                        user.getEmail()
                ),
                jwtUtil.getAccessTokenExpiry()
        );
    }

    // -------- REFRESH TOKEN --------
    public LoginResponseDTO refreshToken(RefreshRequestDTO dto) {

        if (!jwtUtil.isValidRefreshToken(dto.getRefreshToken())) {
            throw new InvalidTokenException();
        }

        String email = jwtUtil.extractEmail(dto.getRefreshToken());

        return new LoginResponseDTO(
                jwtUtil.generateAccessToken(
                        email,
                        Role.USER.name()
                ),
                jwtUtil.generateRefreshToken(email),
                jwtUtil.getAccessTokenExpiry()
        );
    }

    // -------- USERS --------
    public List<UserEntity> getAllUsers(){
        return userRepository.findAll();
    }
}