package org.aystudios.Skincare.controller;

import org.aystudios.Skincare.dto.MyOrderResponseDTO;
import org.aystudios.Skincare.dto.OrderRequestDTO;
import org.aystudios.Skincare.dto.OrderResponseDTO;
import org.aystudios.Skincare.entity.OrderStatus;
import org.aystudios.Skincare.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {


    private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    // TODO: Fix Auth
    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@AuthenticationPrincipal String email, @RequestBody OrderRequestDTO dto){

        OrderResponseDTO response = orderService.createOrder(email, dto);

        return ResponseEntity.ok(response);

    }

    @GetMapping
    public ResponseEntity<List<MyOrderResponseDTO>> getOrders(@AuthenticationPrincipal String email){

        List<MyOrderResponseDTO> response = orderService.getMyOrders(email);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(@PathVariable Long orderId, @RequestParam OrderStatus orderStatus){

        OrderResponseDTO response = orderService.updateOrderStatus(orderId, orderStatus);

        return ResponseEntity.ok(response);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long orderId){
        String response = orderService.deleteOrder(orderId);
        return ResponseEntity.ok(response);
    }
}
