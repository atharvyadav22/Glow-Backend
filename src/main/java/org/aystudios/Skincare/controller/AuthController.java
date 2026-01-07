package org.aystudios.Skincare.controller;

import jakarta.validation.Valid;
import org.aystudios.Skincare.dto.LoginRequestDTO;
import org.aystudios.Skincare.dto.SignUpRequestDTO;
import org.aystudios.Skincare.entity.UserEntity;
import org.aystudios.Skincare.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    public final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@Valid @RequestBody SignUpRequestDTO signUpRequestDTO) {
        authService.signUp(signUpRequestDTO);
        return ResponseEntity.ok("User created successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        String token = authService.login(loginRequestDTO);
//        return ResponseEntity.ok("Logged in successfully");
        return ResponseEntity.ok(token);
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserEntity>> getAllUsers(){
        return ResponseEntity.ok(authService.getAllUsers());
    }
}
