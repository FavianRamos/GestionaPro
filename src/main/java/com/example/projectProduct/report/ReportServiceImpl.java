package com.example.projectProduct.report;

import com.example.projectProduct.product.ProductRepository;
import com.example.projectProduct.sale.Sale;
import com.example.projectProduct.sale.SaleDetailRepository;
import com.example.projectProduct.sale.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {


    private final SaleRepository saleRepository;

    @Override
    public List<SalesReportDTO> getSalesReport(LocalDate startDate, LocalDate endDate, String groupBy) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(23, 59, 59);

        DateTimeFormatter formatter = resolveFormatter(groupBy);

        List<Sale> sales = saleRepository.findBySaleDateBetween(start, end);

        Map<String, List<Sale>> grouped = sales.stream()
                .collect(Collectors.groupingBy(s -> s.getSaleDate().format(formatter)));

        return grouped.entrySet().stream()
                .map(entry -> new SalesReportDTO(
                        entry.getKey(),
                        (long) entry.getValue().size(),
                        entry.getValue().stream()
                                .map(Sale::getTotal)
                                .reduce(BigDecimal.ZERO, BigDecimal::add)
                ))
                .sorted(Comparator.comparing(SalesReportDTO::period))
                .toList();
    }

    private final SaleDetailRepository saleDetailRepository;

    @Override
    public List<TopProductDTO> getTopSellingProducts(LocalDate startDate, LocalDate endDate, int limit) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(23, 59, 59);

        return saleDetailRepository.findTopSellingProducts(start, end)
                .stream()
                .limit(limit)
                .toList();
    }

    private DateTimeFormatter resolveFormatter(String groupBy) {
        return switch (groupBy.toLowerCase()) {
            case "day" -> DateTimeFormatter.ofPattern("yyyy-MM-dd");
            case "month" -> DateTimeFormatter.ofPattern("yyyy-MM");
            case "year" -> DateTimeFormatter.ofPattern("yyyy");
            default -> throw new IllegalArgumentException("Invalid groupBy value: " + groupBy + ". Use day, month or year");
        };


    }

    @Override
    public RevenueReportDTO getTotalRevenue(LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(23, 59, 59);

        List<Sale> sales = saleRepository.findBySaleDateBetween(start, end);

        BigDecimal total = sales.stream()
                .map(Sale::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new RevenueReportDTO(startDate, endDate, (long) sales.size(), total);
    }

    @Override
    public List<CategorySalesDTO> getSalesByCategory(LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(23, 59, 59);

        return saleDetailRepository.findSalesByCategory(start, end);
    }

    private final ProductRepository productRepository;

    @Override
    public List<LowStockProductDTO> getLowStockProducts(Integer threshold) {
        return productRepository.findLowStockProducts(threshold);
    }
}