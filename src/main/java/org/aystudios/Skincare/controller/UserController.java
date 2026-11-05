package org.aystudios.Skincare.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.aystudios.Skincare.dto.UserProfileRequestDTO;
import org.aystudios.Skincare.dto.UserProfileResponseDTO;
import org.aystudios.Skincare.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.aystudios.Skincare.security.CustomUserDetails;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user")
@Tag(name = "7. User profile")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<UserProfileResponseDTO> getProfile(@AuthenticationPrincipal CustomUserDetails user){
        return ResponseEntity.ok(userService.getProfile(user.getId()));
    }

    @PutMapping
    public ResponseEntity<UserProfileResponseDTO> updateProfile(@AuthenticationPrincipal CustomUserDetails user, @Valid @RequestBody UserProfileRequestDTO dto){
        UserProfileResponseDTO response = userService.updateProfile(user.getId(), dto);
        return ResponseEntity.ok(response);
    }
}
