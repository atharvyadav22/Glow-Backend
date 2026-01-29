package org.aystudios.Skincare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
public class UserProfileRequestDTO {
    @Length(min = 3, max = 20, message = "Name must be between 3 and 20 characters")
    private String name;
    @Length(min = 10, max = 10, message = "Phone number must be 10 digits")
    private String phone;
    private String address;
    private String profilePicUrl;

}
