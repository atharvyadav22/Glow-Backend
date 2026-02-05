package org.aystudios.Skincare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CartResponseDTO {

    private Long productId;
    private int quantity;
    private String productName;
    private String description;
    private BigDecimal originalPrice;
    private BigDecimal discountPrice;

}
