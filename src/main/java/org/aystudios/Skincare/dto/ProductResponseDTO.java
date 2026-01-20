package org.aystudios.Skincare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDTO {

    private Long id;
    private String name;
    private String brand;
    private String category;
    private String description;
    private BigDecimal originalPrice;
    private BigDecimal discountPrice;
    private Boolean productAvailable;
    private Integer quantity;
}
