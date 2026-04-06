package org.aystudios.Skincare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.aystudios.Skincare.entity.OrderStatus;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class OrderResponseDTO {
    private Long orderId;
    private OrderStatus status;
    private PaymentMode paymentMode;
    private BigDecimal price;
}
