package com.example.projectProduct.sale;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;


public interface SaleRepository extends JpaRepository<Sale, Long> {

    Page<Sale> findByUserId(Long userId, Pageable pageable);

    List<Sale> findBySaleDateBetween(LocalDateTime start, LocalDateTime end);

    @Query("""
    SELECT s FROM Sale s
    WHERE (:userId IS NULL OR s.user.id = :userId)
    AND (:startDate IS NULL OR s.saleDate >= :startDate)
    AND (:endDate IS NULL OR s.saleDate <= :endDate)
    """)
    Page<Sale> findWithFilters(
            @Param("userId") Long userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);
}

