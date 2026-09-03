package com.example.projectProduct.report;

import java.time.LocalDate;
import java.util.List;

public interface ReportService {
    List<SalesReportDTO> getSalesReport(LocalDate startDate, LocalDate endDate, String groupBy);

    RevenueReportDTO getTotalRevenue(LocalDate startDate, LocalDate endDate);

    List<TopProductDTO> getTopSellingProducts(LocalDate startDate, LocalDate endDate, int limit);

    List<CategorySalesDTO> getSalesByCategory(LocalDate startDate, LocalDate endDate);

    List<LowStockProductDTO> getLowStockProducts(Integer threshold);
}