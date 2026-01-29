package org.aystudios.Skincare.mapper;

import org.aystudios.Skincare.dto.UserProfileRequestDTO;
import org.aystudios.Skincare.dto.UserProfileResponseDTO;
import org.aystudios.Skincare.entity.UserEntity;

public class UserMapper {

    public static UserEntity toEntity(UserProfileRequestDTO dto) {
        UserEntity entity = new UserEntity();
        entity.setName(dto.getName());
        entity.setPhoneNo(dto.getPhone());
        entity.setAddress(dto.getAddress());
        entity.setProfilePicUrl(dto.getProfilePicUrl());

        return entity;
    }

    public static UserProfileResponseDTO toDTO(UserEntity entity) {
        return new UserProfileResponseDTO(
                entity.getEmail(),
                entity.getName(),
                entity.getPhoneNo(),
                entity.getAddress(),
                entity.getProfilePicUrl()
        );
    }

}
