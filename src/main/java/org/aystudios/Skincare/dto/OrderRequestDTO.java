package org.aystudios.Skincare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDTO {
    
    @NotNull(message = "Payment mode is required")
    private PaymentMode paymentMode;
}
