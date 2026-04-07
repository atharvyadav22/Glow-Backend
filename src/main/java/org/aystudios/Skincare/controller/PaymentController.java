package org.aystudios.Skincare.controller;

import org.aystudios.Skincare.dto.OrderResponseDTO;
 import org.aystudios.Skincare.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService){
        this.paymentService = paymentService;

    }
    @PostMapping("/{orderId}/pay")
    public ResponseEntity<OrderResponseDTO> pay(@PathVariable Long orderId) {

        return ResponseEntity.ok(paymentService.simulatePayment(orderId)
        );
    }
}
