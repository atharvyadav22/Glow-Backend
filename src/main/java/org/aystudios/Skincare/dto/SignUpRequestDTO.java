package org.aystudios.Skincare.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignUpRequestDTO {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
