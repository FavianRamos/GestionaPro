package com.example.projectProduct.product;

import com.example.projectProduct.report.LowStockProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("""
    SELECT new com.example.projectProduct.report.LowStockProductDTO(
        p.id, p.name, p.stock, p.category.name)
    FROM Product p
    WHERE p.stock <= :threshold
    ORDER BY p.stock ASC
    """)
    List<LowStockProductDTO> findLowStockProducts(@Param("threshold") Integer threshold);
}