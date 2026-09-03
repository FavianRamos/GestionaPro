package com.example.projectProduct.report;

import java.math.BigDecimal;

public record TopProductDTO(
        Long productId,
        String productName,
        Long totalQuantitySold,
        BigDecimal totalRevenue
) {}