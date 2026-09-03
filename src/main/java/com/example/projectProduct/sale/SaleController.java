package com.example.projectProduct.sale;

import com.example.projectProduct.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
public class SaleController {

    private final SaleService saleService;

    @PostMapping
    public ResponseEntity<SaleResponseDTO> createSale(
            @Valid @RequestBody SaleRequestDTO saleRequestDTO,
            @AuthenticationPrincipal User currentUser) {
        SaleResponseDTO createdSale = saleService.createSale(saleRequestDTO, currentUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSale);
    }

    @GetMapping("/my")
    public ResponseEntity<Page<SaleResponseDTO>> getMySales(
            @AuthenticationPrincipal User currentUser,
            Pageable pageable) {
        return ResponseEntity.ok(saleService.getMySales(currentUser.getId(), pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleResponseDTO> getSaleById(@PathVariable Long id) {
        SaleResponseDTO sale = saleService.getSaleById(id);
        return ResponseEntity.ok(sale);
    }

    @GetMapping
    public ResponseEntity<Page<SaleResponseDTO>> getAllSales(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            Pageable pageable) {
        Page<SaleResponseDTO> sales = saleService.getAllSales(userId, startDate, endDate, pageable);
        return ResponseEntity.ok(sales);
    }
}