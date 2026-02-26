package org.aystudios.Skincare.service;

import org.aystudios.Skincare.dto.*;
import org.aystudios.Skincare.entity.CartItemEntity;
import org.aystudios.Skincare.entity.ProductEntity;
import org.aystudios.Skincare.entity.UserEntity;
import org.aystudios.Skincare.exception.auth.UserNotFoundException;
import org.aystudios.Skincare.exception.product.ProductNotFoundException;
import org.aystudios.Skincare.mapper.CartMapper;
import org.aystudios.Skincare.mapper.UserMapper;
import org.aystudios.Skincare.repository.CartRepository;
import org.aystudios.Skincare.repository.ProductRepository;
import org.aystudios.Skincare.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
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

    // ----- Cart Response Builder -----
    @Transactional(readOnly = true)
    private CartResponseDTO buildCartResponse(Long userId){

        List<CartItemEntity> cartItems = cartRepository.findByUserId(userId);
        List<CartItemResponseDTO> response = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;

        for(CartItemEntity item: cartItems){
            BigDecimal itemTotal = item.getProduct().getDiscountPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            totalPrice = totalPrice.add(itemTotal);
            response.add(CartMapper.toResponse(item));
        }

        return new CartResponseDTO(response, totalPrice);
    }

    // ----- Add to Cart -----
    @Transactional
    public CartResponseDTO addToCart(String email, CartRequestDTO dto) {

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
        return buildCartResponse(userEntity.getId());
    }

    // ----- Get Cart Items -----
    public CartResponseDTO getMyCart(String email) {

        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
        return buildCartResponse(userEntity.getId());
    }


    // ----- Remove Item -----
    @Transactional
    public CartResponseDTO removeItem(String email, Long productId) {

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException());

        cartRepository.deleteByUserAndProduct(user, product);

        return buildCartResponse(user.getId());
    }

    // TODO: Fix N+1 Query by using Joins
    // ----- Get All Cart Items -----
    public List<CartItemResponseDTO> getAllCartItems(){

        List<CartItemEntity> entity = cartRepository.findAll();

        return entity.stream().map(CartMapper::toResponse).toList();
    }

    // ----- Cart Checkout -----
    public CartCheckoutResponseDTO checkout(String email){

        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
        UserProfileResponseDTO userProfile = UserMapper.toDTO(userEntity);

        List<PaymentMode> paymentModes = new ArrayList<>();
        for (PaymentMode mode: PaymentMode.values()){
            paymentModes.add(mode);
        }

        return new CartCheckoutResponseDTO(userProfile, buildCartResponse(userEntity.getId()), paymentModes);

    }


}
