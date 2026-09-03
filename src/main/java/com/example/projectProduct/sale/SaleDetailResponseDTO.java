package com.example.projectProduct.sale;

import java.math.BigDecimal;

public record SaleDetailResponseDTO(
        Long productId,
        String productName,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal subtotal
) {}