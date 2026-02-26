package org.aystudios.Skincare.controller;

import org.aystudios.Skincare.dto.CartCheckoutResponseDTO;
import org.aystudios.Skincare.dto.CartRequestDTO;
import org.aystudios.Skincare.dto.CartItemResponseDTO;
import org.aystudios.Skincare.dto.CartResponseDTO;
import org.aystudios.Skincare.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    // FIXME: Change Auth to Long Id
    @PostMapping
    public ResponseEntity<CartResponseDTO> addToCart(@RequestBody CartRequestDTO dto){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        CartResponseDTO response = cartService.addToCart(email, dto);
        return ResponseEntity.ok(response);
    }

    // FIXME: Change Auth to Long Id
    // FIXME: Add Paginator
    @GetMapping
    public ResponseEntity<CartResponseDTO> getCartItems(@AuthenticationPrincipal Long userId){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        CartResponseDTO response = cartService.getMyCart(email);
        return ResponseEntity.ok(response);
    }

    // FIXME: Change Auth to Long Id
    @DeleteMapping("/{productId}")
    public ResponseEntity<CartResponseDTO> removeFromCart(@PathVariable Long productId){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        CartResponseDTO response = cartService.removeItem(email, productId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CartItemResponseDTO>> getAllCart(){
        List<CartItemResponseDTO> response = cartService.getAllCartItems();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/checkout")
    public ResponseEntity<CartCheckoutResponseDTO> checkout(@AuthenticationPrincipal String email){
        CartCheckoutResponseDTO response = cartService.checkout(email);
        return ResponseEntity.ok(response);
    }
}
