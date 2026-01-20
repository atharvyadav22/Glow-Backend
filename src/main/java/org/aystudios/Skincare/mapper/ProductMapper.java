package org.aystudios.Skincare.mapper;

import org.aystudios.Skincare.dto.ProductRequestDTO;
import org.aystudios.Skincare.dto.ProductResponseDTO;
import org.aystudios.Skincare.entity.ProductEntity;

public class ProductMapper {

    public static ProductEntity toEntity(ProductRequestDTO dto){

        ProductEntity p = new ProductEntity();

        p.setName(dto.getName());
        p.setBrand(dto.getBrand());
        p.setCategory(dto.getCategory());
        p.setDescription(dto.getDescription());
        p.setOriginalPrice(dto.getOriginalPrice());
        p.setDiscountPrice(dto.getDiscountPrice());
        p.setProductAvailable(dto.getProductAvailable());
        p.setQuantity(dto.getQuantity());
        return p;
    }

    public static ProductResponseDTO toResponse(ProductEntity p) {
        return new ProductResponseDTO(
                p.getId(),
                p.getName(),
                p.getBrand(),
                p.getCategory(),
                p.getDescription(),
                p.getOriginalPrice(),
                p.getDiscountPrice(),
                p.getProductAvailable(),
                p.getQuantity()
        );
    }
}
