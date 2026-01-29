package org.aystudios.Skincare.controller;

import org.aystudios.Skincare.dto.UserProfileRequestDTO;
import org.aystudios.Skincare.dto.UserProfileResponseDTO;
import org.aystudios.Skincare.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<UserProfileResponseDTO> getProfile(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(userService.getProfile(email));
    }

    @PostMapping
    public ResponseEntity<UserProfileResponseDTO> updateProfile(@RequestBody UserProfileRequestDTO dto){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserProfileResponseDTO response = userService.updateProfile(email, dto);
        return ResponseEntity.ok(response);
    }
}
