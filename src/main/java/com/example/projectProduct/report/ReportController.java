package com.example.projectProduct.report;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/sales")
    public ResponseEntity<List<SalesReportDTO>> getSalesReport(
            @RequestParam String groupBy,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<SalesReportDTO> report = reportService.getSalesReport(startDate, endDate, groupBy);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/revenue")
    public ResponseEntity<RevenueReportDTO> getTotalRevenue(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        RevenueReportDTO revenue = reportService.getTotalRevenue(startDate, endDate);
        return ResponseEntity.ok(revenue);
    }

    @GetMapping("/top-products")
    public ResponseEntity<List<TopProductDTO>> getTopSellingProducts(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "10") int limit) {

        List<TopProductDTO> topProducts = reportService.getTopSellingProducts(startDate, endDate, limit);
        return ResponseEntity.ok(topProducts);
    }

    @GetMapping("/sales-by-category")
    public ResponseEntity<List<CategorySalesDTO>> getSalesByCategory(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<CategorySalesDTO> report = reportService.getSalesByCategory(startDate, endDate);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<LowStockProductDTO>> getLowStockProducts(
            @RequestParam(defaultValue = "10") Integer threshold) {

        List<LowStockProductDTO> report = reportService.getLowStockProducts(threshold);
        return ResponseEntity.ok(report);
    }
}