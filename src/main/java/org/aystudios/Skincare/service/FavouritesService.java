package org.aystudios.Skincare.service;

import org.aystudios.Skincare.dto.FavouriteResponseDTO;
import org.aystudios.Skincare.dto.ProductResponseDTO;
import org.aystudios.Skincare.entity.FavouritesEntity;
import org.aystudios.Skincare.entity.ProductEntity;
import org.aystudios.Skincare.entity.UserEntity;
import org.aystudios.Skincare.exception.auth.UserNotFoundException;
import org.aystudios.Skincare.exception.product.ProductNotFoundException;
import org.aystudios.Skincare.mapper.ProductMapper;
import org.aystudios.Skincare.repository.FavouritesRepository;
import org.aystudios.Skincare.repository.ProductRepository;
import org.aystudios.Skincare.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class FavouritesService {

    private final FavouritesRepository favouritesRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public FavouritesService(FavouritesRepository favouritesRepository, ProductRepository productRepository, UserRepository userRepository){
        this.favouritesRepository = favouritesRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    // ----- Add to Favourites -----
    @Transactional
    public FavouriteResponseDTO toggleFavourite(Long userId, Long productId){

        if (favouritesRepository.existsByUserIdAndProductId(userId, productId)) {
            favouritesRepository.deleteByUserIdAndProductId(userId, productId);
            return new FavouriteResponseDTO(false);
        }
        else {
            UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
            ProductEntity productEntity = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException());
            FavouritesEntity entity = new FavouritesEntity(null, userEntity,productEntity);
            favouritesRepository.save(entity);
            return new FavouriteResponseDTO(true);
        }

    }

    // ----- Checks if Product is Favourite -----
    public FavouriteResponseDTO isFavourite(Long userId, Long productId){

        Boolean isFavourite = favouritesRepository.existsByUserIdAndProductId(userId, productId);

        return new FavouriteResponseDTO(isFavourite);
    }

    // TODO: Fix N+1 Query by using Joins
    // ----- Get Favourites List -----
    @Transactional
    public List<ProductResponseDTO> getFavourites(Long userId) {

        List<FavouritesEntity> list =
                favouritesRepository.findByUserId(userId);

        List<ProductResponseDTO> response = new ArrayList<>();

        for (FavouritesEntity entity : list) {
            ProductEntity product = entity.getProduct();
            ProductResponseDTO dto = ProductMapper.toResponse(product);
            response.add(dto);
        }

        return response;
    }


}
