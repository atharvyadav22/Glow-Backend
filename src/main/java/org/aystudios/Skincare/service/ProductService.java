package org.aystudios.Skincare.service;

import org.aystudios.Skincare.dto.ProductRequestDTO;
import org.aystudios.Skincare.dto.ProductResponseDTO;
import org.aystudios.Skincare.entity.ProductEntity;
import org.aystudios.Skincare.exception.ResourceNotFoundException;
import org.aystudios.Skincare.mapper.ProductMapper;
import org.aystudios.Skincare.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Qualifier("productService")
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository repository) {
        this.productRepository = repository;
    }

    public ProductResponseDTO addProduct(ProductRequestDTO dto) {
        ProductEntity productEntity = ProductMapper.toEntity(dto);
        ProductEntity saved = productRepository.save(productEntity);

        return ProductMapper.toResponse(saved);
    }


    public Page<ProductResponseDTO> getAllProducts(int page, int size, String category, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ProductEntity> productEntities;
        if (category != null && !category.isBlank())
            productEntities = productRepository.findByCategoryIgnoreCase(category, pageable);
        else
            productEntities = productRepository.findAll(pageable);

        return productEntities.map(ProductMapper::toResponse);
    }


    public ProductEntity getProductById(Long id) {
        return productRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
    }

    public Page<ProductResponseDTO> searchProducts(String keyword, int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return productRepository.searchProducts(keyword, pageable).map(ProductMapper::toResponse);
    }


    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO dto) {

        ProductEntity existing = getProductById(id);

        existing.setName(dto.getName());
        existing.setBrand(dto.getBrand());
        existing.setCategory(dto.getCategory());
        existing.setDescription(dto.getDescription());
        existing.setPrice(dto.getPrice());
        existing.setProductAvailable(dto.getProductAvailable());
        existing.setQuantity(dto.getQuantity());

        return ProductMapper.toResponse(productRepository.save(existing));
    }


    public void deleteProduct(Long id) {

        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id " + id);
        }

        productRepository.deleteById(id);
    }

}
