package org.aystudios.Skincare.service;

import org.aystudios.Skincare.dto.UserProfileRequestDTO;
import org.aystudios.Skincare.dto.UserProfileResponseDTO;
import org.aystudios.Skincare.entity.UserEntity;
import org.aystudios.Skincare.exception.auth.UserNotFoundException;
import org.aystudios.Skincare.mapper.UserMapper;
import org.aystudios.Skincare.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ----- Get Profile -----

    public UserProfileResponseDTO getProfile(String email) {
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
        return UserMapper.toDTO(userEntity);
    }

    // ----- Update Profile -----
    public UserProfileResponseDTO updateProfile(String email, UserProfileRequestDTO dto) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));

        user.setName(dto.getName());
        user.setPhoneNo(dto.getPhoneNo());
        user.setAddress(dto.getAddress());
        user.setProfilePicUrl(dto.getProfilePicUrl());

        UserEntity entity = userRepository.save(user);

        return UserMapper.toDTO(entity);
    }
}
