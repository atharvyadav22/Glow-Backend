package org.aystudios.Skincare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartRequestDTO {
    private Long productId;
    private int quantity;
}
