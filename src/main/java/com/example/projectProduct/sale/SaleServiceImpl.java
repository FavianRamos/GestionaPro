package com.example.projectProduct.sale;

import com.example.projectProduct.exception.ResourceNotFoundException;
import com.example.projectProduct.exception.InsufficientStockException;
import com.example.projectProduct.product.Product;
import com.example.projectProduct.product.ProductRepository;
import com.example.projectProduct.user.User;
import com.example.projectProduct.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SaleServiceImpl implements SaleService {

    private static final BigDecimal IGV_RATE = new BigDecimal("0.18");

    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public SaleResponseDTO createSale(SaleRequestDTO saleRequestDTO, Long authenticatedUserId) {

        User user = userRepository.findById(authenticatedUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + authenticatedUserId));

        Sale sale = new Sale();
        sale.setUser(user);

        List<SaleDetail> details = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;

        for (SaleDetailRequestDTO itemRequest : saleRequestDTO.details()) {

            Product product = productRepository.findById(itemRequest.productId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + itemRequest.productId()));

            if (product.getStock() < itemRequest.quantity()) {
                throw new InsufficientStockException(
                        "Insufficient stock for product: " + product.getName());
            }

            product.setStock(product.getStock() - itemRequest.quantity());
            productRepository.save(product);

            BigDecimal unitPrice = product.getPrice();
            BigDecimal lineSubtotal = unitPrice.multiply(BigDecimal.valueOf(itemRequest.quantity()));

            SaleDetail detail = new SaleDetail();
            detail.setSale(sale);
            detail.setProduct(product);
            detail.setQuantity(itemRequest.quantity());
            detail.setUnitPrice(unitPrice);
            detail.setSubtotal(lineSubtotal);

            details.add(detail);
            subtotal = subtotal.add(lineSubtotal);
        }

        BigDecimal igv = subtotal.multiply(IGV_RATE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = subtotal.add(igv);

        sale.setDetails(details);
        sale.setSubtotal(subtotal);
        sale.setIgv(igv);
        sale.setTotal(total);

        Sale savedSale = saleRepository.save(sale);

        return mapToResponseDTO(savedSale);
    }

    @Override
    public SaleResponseDTO getSaleById(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found with id: " + id));
        return mapToResponseDTO(sale);
    }

    @Override
    public Page<SaleResponseDTO> getAllSales(Long userId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return saleRepository.findWithFilters(userId, startDate, endDate, pageable)
                .map(this::mapToResponseDTO);
    }

    @Override
    public Page<SaleResponseDTO> getMySales(Long userId, Pageable pageable) {
        return saleRepository.findByUserId(userId, pageable)
                .map(this::mapToResponseDTO);
    }

    private SaleResponseDTO mapToResponseDTO(Sale sale) {

        List<SaleDetailResponseDTO> detailDTOs = sale.getDetails().stream()
                .map(d -> new SaleDetailResponseDTO(
                        d.getProduct().getId(),
                        d.getProduct().getName(),
                        d.getQuantity(),
                        d.getUnitPrice(),
                        d.getSubtotal()
                ))
                .collect(Collectors.toList());

        return new SaleResponseDTO(
                sale.getId(),
                sale.getUser().getId(),
                sale.getUser().getUsername(),
                sale.getSaleDate(),
                sale.getSubtotal(),
                sale.getIgv(),
                sale.getTotal(),
                detailDTOs
        );
    }
}