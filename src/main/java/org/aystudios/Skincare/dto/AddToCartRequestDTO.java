package org.aystudios.Skincare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddToCartRequestDTO {

    private Long productId;
    private int quantity;
}
