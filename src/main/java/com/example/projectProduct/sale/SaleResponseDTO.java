package com.example.projectProduct.sale;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record SaleResponseDTO(
        Long id,
        Long userId,
        String userName,
        LocalDateTime saleDate,
        BigDecimal subtotal,
        BigDecimal igv,
        BigDecimal total,
        List<SaleDetailResponseDTO> details
) {}