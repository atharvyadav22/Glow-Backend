package org.aystudios.Skincare.service;

import org.aystudios.Skincare.dto.*;
import org.aystudios.Skincare.entity.OrderEntity;
import org.aystudios.Skincare.entity.OrderStatus;
import org.aystudios.Skincare.entity.PaymentStatus;
import org.aystudios.Skincare.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


// TODO: Implement Proper Payment Gateway
@Service
public class PaymentService {

    private final OrderRepository orderRepository;

    public PaymentService(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderResponseDTO simulatePayment(Long orderId) {

        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getPaymentMode() == PaymentMode.COD) {
            throw new RuntimeException("COD payment does not require any payment gateway");
        }

        try {
            // Simulate payment delay (2 seconds)
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // After delay mark success
        order.setPaymentStatus(PaymentStatus.SUCCESS);
        order.setStatus(OrderStatus.CONFIRMED);

        orderRepository.save(order);

        return new OrderResponseDTO(
                order.getOrderId(),
                order.getStatus(),
                order.getPaymentMode(),
                order.getTotalPrice()
        );
    }
}
