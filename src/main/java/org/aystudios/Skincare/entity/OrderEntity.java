package org.aystudios.Skincare.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aystudios.Skincare.dto.PaymentMode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

//TODO: Pass Ids Instead of Entity (Learn)
@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Enumerated(EnumType.STRING)
    private PaymentMode paymentMode;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private BigDecimal totalPrice;

    private LocalDateTime createdAt;

}
