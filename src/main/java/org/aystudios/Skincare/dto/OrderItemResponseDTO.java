package org.aystudios.Skincare.dto;

import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.aystudios.Skincare.entity.OrderEntity;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class OrderItemResponseDTO {
    private Long productId;
    private String productName;
    private String brand;
    private String image;
    private int quantity;
    private BigDecimal priceAtPurchase;
}
