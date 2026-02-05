package org.aystudios.Skincare.service;

import org.aystudios.Skincare.dto.AddToCartRequestDTO;
import org.aystudios.Skincare.dto.CartResponseDTO;
import org.aystudios.Skincare.entity.CartItemEntity;
import org.aystudios.Skincare.entity.ProductEntity;
import org.aystudios.Skincare.entity.UserEntity;
import org.aystudios.Skincare.exception.auth.UserNotFoundException;
import org.aystudios.Skincare.exception.product.ProductNotFoundException;
import org.aystudios.Skincare.repository.CartRepository;
import org.aystudios.Skincare.repository.ProductRepository;
import org.aystudios.Skincare.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    // ----- Add to Cart -----
    public void addToCart(String email, AddToCartRequestDTO dto) {

        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
        ProductEntity productEntity = productRepository.findById(dto.getProductId()).orElseThrow(() -> new ProductNotFoundException());

        CartItemEntity cartItemEntity = cartRepository.findByUserAndProduct(userEntity, productEntity)
                .orElse((null));

        if(cartItemEntity == null){
            cartItemEntity = new CartItemEntity(null, userEntity, productEntity, dto.getQuantity());
        }
        else {
            cartItemEntity.setQuantity(cartItemEntity.getQuantity() + dto.getQuantity());
        }

        cartRepository.save(cartItemEntity);
    }

    // ----- Get Cart Items -----
    public List<CartResponseDTO> getMyCart(String email) {

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        List<CartItemEntity> cartItems = cartRepository.findByUser(user);

        List<CartResponseDTO> response = new ArrayList<>();

        for (CartItemEntity item : cartItems) {

            CartResponseDTO dto = new CartResponseDTO(
                    item.getProduct().getId(),
                    item.getQuantity(),
                    item.getProduct().getName(),
                    item.getProduct().getDescription(),
                    item.getProduct().getOriginalPrice(),
                    item.getProduct().getDiscountPrice()
            );
            response.add(dto);

        }

        return response;
    }


    // ----- Remove Item -----
    public void removeItem(String email, Long productId) {

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException());

        cartRepository.deleteByUserAndProduct(user, product);
    }


}
