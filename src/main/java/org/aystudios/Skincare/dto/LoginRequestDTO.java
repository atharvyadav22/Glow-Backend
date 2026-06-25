package org.aystudios.Skincare.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDTO {

    @Schema(description = "For testing use this email:",example = "test@gmail.com")
    @Email(message = "Invalid email address")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @Schema(description = "For testing use this password:",example = "123456")
    @NotBlank(message = "Password cannot be blank")
    private String password;

}
