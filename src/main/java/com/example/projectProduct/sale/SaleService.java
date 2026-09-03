package com.example.projectProduct.sale;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface SaleService {

    SaleResponseDTO createSale(SaleRequestDTO saleRequestDTO, Long authenticatedUserId);
    SaleResponseDTO getSaleById(Long id);
    Page<SaleResponseDTO> getAllSales(Long userId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    Page<SaleResponseDTO> getMySales(Long userId, Pageable pageable);
}