package org.aystudios.Skincare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class CartResponseDTO {
    private List<CartItemResponseDTO> cartItems;
    private BigDecimal totalPrice;
}
