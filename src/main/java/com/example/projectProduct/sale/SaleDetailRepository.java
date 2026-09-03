package com.example.projectProduct.sale;

import com.example.projectProduct.report.CategorySalesDTO;
import com.example.projectProduct.report.TopProductDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SaleDetailRepository extends JpaRepository<SaleDetail, Long> {

    @Query("""
        SELECT new com.example.projectProduct.report.TopProductDTO(
            sd.product.id, sd.product.name, SUM(sd.quantity), SUM(sd.subtotal))
        FROM SaleDetail sd
        WHERE sd.sale.saleDate BETWEEN :start AND :end
        GROUP BY sd.product.id, sd.product.name
        ORDER BY SUM(sd.quantity) DESC
        """)
    List<TopProductDTO> findTopSellingProducts(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    @Query("""
    SELECT new com.example.projectProduct.report.CategorySalesDTO(
        sd.product.category.id, sd.product.category.name, SUM(sd.quantity), SUM(sd.subtotal))
    FROM SaleDetail sd
    WHERE sd.sale.saleDate BETWEEN :start AND :end
    GROUP BY sd.product.category.id, sd.product.category.name
    ORDER BY SUM(sd.subtotal) DESC
    """)
    List<CategorySalesDTO> findSalesByCategory(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
}