package org.aystudios.Skincare.controller;

import org.aystudios.Skincare.dto.CartRequestDTO;
import org.aystudios.Skincare.dto.CartResponseDTO;
import org.aystudios.Skincare.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    public ResponseEntity<CartResponseDTO> addToCart(@RequestBody CartRequestDTO dto){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        CartResponseDTO response = cartService.addToCart(email, dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<CartResponseDTO>> getCartItems(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        List<CartResponseDTO> response = cartService.getMyCart(email);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> removeFromCart(@PathVariable Long productId){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        cartService.removeItem(email, productId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<CartResponseDTO>> getAllCart(){
        List<CartResponseDTO> response = cartService.getAllCartItems();
        return ResponseEntity.ok(response);
    }
}
