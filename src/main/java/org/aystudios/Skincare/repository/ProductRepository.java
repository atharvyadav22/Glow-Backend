package org.aystudios.Skincare.repository;

import org.aystudios.Skincare.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    // 🔍 Search with pagination
    @Query("""
        SELECT p FROM ProductEntity p
        WHERE
        LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(p.brand) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(p.category) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    Page<ProductEntity> searchProducts(@Param("keyword") String keyword, Pageable pageable);

    Page<ProductEntity> findByCategoryIgnoreCase(String category, Pageable pageable);

    @Query("SELECT DISTINCT p.category FROM ProductEntity p")
    List<String> findAllCategories();
}
