package org.aystudios.Skincare.repository;

import org.aystudios.Skincare.entity.FavouritesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavouritesRepository extends JpaRepository<FavouritesEntity, Long> {

    boolean existsByUserIdAndProductId(Long userId, Long productId);

    List<FavouritesEntity> findByUserId(Long userId);

    void deleteByUserIdAndProductId(Long userId, Long productId);

}
