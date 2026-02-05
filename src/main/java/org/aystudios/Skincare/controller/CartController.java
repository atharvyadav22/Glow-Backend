package org.aystudios.Skincare.controller;

import org.aystudios.Skincare.dto.AddToCartRequestDTO;
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
    public ResponseEntity<Void> addToCart(@RequestBody AddToCartRequestDTO dto){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        cartService.addToCart(email, dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<CartResponseDTO>> getCartItems(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        List<CartResponseDTO> response = cartService.getMyCart(email);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Void> removeFromCart(@RequestParam Long productId){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();


        cartService.removeItem(email, productId);
        return ResponseEntity.ok().build();
    }

}
