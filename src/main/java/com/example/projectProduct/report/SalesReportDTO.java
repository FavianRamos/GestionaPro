package com.example.projectProduct.report;

import java.math.BigDecimal;

public record SalesReportDTO(
        String period,
        Long totalSales,
        BigDecimal totalRevenue
) {}