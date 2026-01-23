package org.aystudios.Skincare.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class  RefreshRequestDTO {
    @NotBlank(message = "Refresh token cannot be blank")
    String refreshToken;
}
