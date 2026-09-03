package com.example.projectProduct.product;

import java.math.BigDecimal;

public record ProductResponseDTO(
        Long id,
        String name,
        BigDecimal price,
        Integer stock,
        String categoryName
) {}