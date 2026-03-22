package org.aystudios.Skincare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CartItemResponseDTO {

    private Long productId;
    private int quantity;
    private String productName;
    private String image;
    private String brand;
    private String description;
    private BigDecimal originalPrice;
    private BigDecimal discountPrice;

}
