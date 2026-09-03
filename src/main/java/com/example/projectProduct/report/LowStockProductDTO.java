package com.example.projectProduct.report;

public record LowStockProductDTO(
        Long productId,
        String productName,
        Integer stock,
        String categoryName
) {}