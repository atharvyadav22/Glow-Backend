package org.aystudios.Skincare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.aystudios.Skincare.entity.Role;

@Data
@AllArgsConstructor
public class UserProfileResponseDTO {
    private String email;
    private String name;
    private String phoneNo;
    private String address;
    private String profilePicUrl;
}
