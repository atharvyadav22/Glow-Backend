package org.aystudios.Skincare.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
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

import org.aystudios.Skincare.security.CustomUserDetails;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cart")
@Tag(name = "3. Cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    public ResponseEntity<CartResponseDTO> addToCart(@AuthenticationPrincipal CustomUserDetails user, @Valid @RequestBody CartRequestDTO dto){
        CartResponseDTO response = cartService.addToCart(user.getId(), dto);
        return ResponseEntity.ok(response);
    }

    // FIXME: Add Paginator
    @GetMapping
    public ResponseEntity<CartResponseDTO> getCartItems(@AuthenticationPrincipal CustomUserDetails user){
        CartResponseDTO response = cartService.getMyCart(user.getId());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<CartResponseDTO> removeFromCart(@AuthenticationPrincipal CustomUserDetails user, @PathVariable Long productId){
        CartResponseDTO response = cartService.removeItem(user.getId(), productId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CartItemResponseDTO>> getAllCart(){
        List<CartItemResponseDTO> response = cartService.getAllCartItems();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/checkout")
    public ResponseEntity<CartCheckoutResponseDTO> checkout(@AuthenticationPrincipal CustomUserDetails user){
        CartCheckoutResponseDTO response = cartService.checkout(user.getId());
        return ResponseEntity.ok(response);
    }
}
