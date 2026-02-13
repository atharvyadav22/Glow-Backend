package org.aystudios.Skincare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserProfileResponseDTO {
    private String email;
    private String name;
    private String phoneNo;
    private String address;
    private String profilePicUrl;
}
