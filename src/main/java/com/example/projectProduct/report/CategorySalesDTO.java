package com.example.projectProduct.report;

import java.math.BigDecimal;

public record CategorySalesDTO(
        Long categoryId,
        String categoryName,
        Long totalQuantitySold,
        BigDecimal totalRevenue
) {}