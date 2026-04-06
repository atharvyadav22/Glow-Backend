package org.aystudios.Skincare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.aystudios.Skincare.entity.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class MyOrderResponseDTO {

    private Long orderId;

    private OrderStatus status;

    private PaymentMode paymentMode;

    private BigDecimal totalPrice;

    private LocalDateTime createdAt;

    private List<OrderItemResponseDTO> orderItems;
}
