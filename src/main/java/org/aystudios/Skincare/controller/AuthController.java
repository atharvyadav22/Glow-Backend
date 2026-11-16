package org.aystudios.Skincare.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.aystudios.Skincare.dto.LoginRequestDTO;
import org.aystudios.Skincare.dto.LoginResponseDTO;
import org.aystudios.Skincare.dto.RefreshRequestDTO;
import org.aystudios.Skincare.dto.SignUpRequestDTO;
import org.aystudios.Skincare.dto.UserProfileResponseDTO;
import org.aystudios.Skincare.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "1. Authentication")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> testConnection() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "message", "Connection successful! Backend is running.",
                "timestamp", LocalDateTime.now()
        ));
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@Valid @RequestBody SignUpRequestDTO signUpRequestDTO) {
        authService.signUp(signUpRequestDTO);
        return ResponseEntity.ok("User created successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        return ResponseEntity.ok(authService.login(loginRequestDTO));
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDTO> refreshToken(@Valid @RequestBody RefreshRequestDTO refreshRequestDTO) {
        return ResponseEntity.ok(authService.refreshToken(refreshRequestDTO));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/allusers")
    public ResponseEntity<List<UserProfileResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(authService.getAllUsers());
    }
}
