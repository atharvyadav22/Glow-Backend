package org.aystudios.Skincare.mapper;

import org.aystudios.Skincare.dto.CartItemResponseDTO;
import org.aystudios.Skincare.entity.CartItemEntity;

public class CartMapper {

    public static CartItemResponseDTO toResponse(CartItemEntity entity){
        return new CartItemResponseDTO(
                entity.getProduct().getId(),
                entity.getQuantity(),
                entity.getProduct().getName(),
                entity.getProduct().getImage(),
                entity.getProduct().getBrand(),
                entity.getProduct().getDescription(),
                entity.getProduct().getOriginalPrice(),
                entity.getProduct().getDiscountPrice()
        );
    }
}
