package org.aystudios.Skincare.service;

import org.aystudios.Skincare.dto.ProductRequestDTO;
import org.aystudios.Skincare.dto.ProductResponseDTO;
import org.aystudios.Skincare.entity.ProductEntity;
import org.aystudios.Skincare.exception.general.ResourceNotFoundException;
import org.aystudios.Skincare.mapper.ProductMapper;
import org.aystudios.Skincare.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class
ProductService {

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


    public ProductResponseDTO getProductById(Long id) {
        ProductEntity entity = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));

        return ProductMapper.toResponse(entity);
    }

    public Page<ProductResponseDTO> searchProducts(String keyword, int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        if (keyword == null || keyword.isBlank()) return null;
        else return productRepository.searchProducts(keyword, pageable).map(ProductMapper::toResponse);
    }


    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO dto) {

        ProductEntity existing = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));

        existing.setName(dto.getName());
        existing.setBrand(dto.getBrand());
        existing.setCategory(dto.getCategory());
        existing.setDescription(dto.getDescription());
        existing.setOriginalPrice(dto.getOriginalPrice());
        existing.setProductAvailable(dto.getProductAvailable());
        existing.setQuantity(dto.getQuantity());

        ProductEntity productEntity = productRepository.save(existing);
        return ProductMapper.toResponse(productEntity);
    }


    public void deleteProduct(Long id) {

        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id " + id);
        }

        productRepository.deleteById(id);
    }

    public List<String> getCategories(){
        return productRepository.findAllCategories();
    }

    public Page<ProductResponseDTO> getProductsByCategory(String category, int page, int size, String sortBy, String direction){
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ProductEntity> productEntities = productRepository.findByCategoryIgnoreCase(category, pageable);

        if (productEntities.isEmpty()) throw new ResourceNotFoundException("No products found for category " + category);

        return productEntities.map(ProductMapper::toResponse);
    }

}
