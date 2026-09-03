package com.example.projectProduct.report;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RevenueReportDTO(
        LocalDate startDate,
        LocalDate endDate,
        Long totalSales,
        BigDecimal totalRevenue
) {}