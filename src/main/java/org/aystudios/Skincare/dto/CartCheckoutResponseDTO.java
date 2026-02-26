package org.aystudios.Skincare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CartCheckoutResponseDTO {
    private UserProfileResponseDTO userProfile;
    private CartResponseDTO cart;
    private List<PaymentMode> paymentModes;
}
