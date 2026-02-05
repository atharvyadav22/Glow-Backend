package org.aystudios.Skincare.repository;

import org.aystudios.Skincare.entity.CartItemEntity;
import org.aystudios.Skincare.entity.ProductEntity;
import org.aystudios.Skincare.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartItemEntity, Long> {

    @Query("""
            SELECT c FROM CartItemEntity c
            JOIN FETCH c.product
            WHERE c.user = :user
            """)
    Optional<CartItemEntity> findByUserAndProduct(UserEntity user, ProductEntity product);

    List<CartItemEntity> findByUser(UserEntity user);

    void deleteByUserAndProduct(UserEntity user, ProductEntity product);
}
