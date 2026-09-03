
package com.example.projectProduct.sale;

import com.example.projectProduct.exception.InsufficientStockException;
import com.example.projectProduct.exception.ResourceNotFoundException;
import com.example.projectProduct.product.Product;
import com.example.projectProduct.product.ProductRepository;
import com.example.projectProduct.user.User;
import com.example.projectProduct.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaleServiceImplTest {

    @Mock
    private SaleRepository saleRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    private SaleServiceImpl saleService;

    private User user;
    private Product product;

    @BeforeEach
    void setUp() {

        saleService = new SaleServiceImpl(
                saleRepository,
                productRepository,
                userRepository
        );

        user = new User();
        user.setId(1L);
        user.setUsername("favian");

        product = new Product();
        product.setId(1L);
        product.setName("Laptop");
        product.setPrice(BigDecimal.valueOf(1500));
        product.setStock(10);
    }

    @Test
    void createSale_shouldDiscountStockAndCalculateTotal_whenStockIsSufficient() {

        SaleDetailRequestDTO itemRequest =
                new SaleDetailRequestDTO(1L, 2);

        SaleRequestDTO request =
                new SaleRequestDTO(List.of(itemRequest));

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        when(saleRepository.save(any(Sale.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        SaleResponseDTO result =
                saleService.createSale(request, 1L);

        // Subtotal: 1500 × 2 = 3000
        // IGV: 3000 × 0.18 = 540
        // Total: 3540
        assertThat(result.total())
                .isEqualByComparingTo(BigDecimal.valueOf(3540));

        // Stock: 10 - 2 = 8
        assertThat(product.getStock())
                .isEqualTo(8);

        verify(productRepository)
                .save(product);

        verify(saleRepository)
                .save(any(Sale.class));
    }

    @Test
    void createSale_shouldThrowException_whenStockIsInsufficient() {

        SaleDetailRequestDTO itemRequest =
                new SaleDetailRequestDTO(1L, 20);

        SaleRequestDTO request =
                new SaleRequestDTO(List.of(itemRequest));

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        assertThatThrownBy(() ->
                saleService.createSale(request, 1L)
        )
                .isInstanceOf(InsufficientStockException.class)
                .hasMessageContaining("Insufficient stock");

        verify(saleRepository, never())
                .save(any());

        verify(productRepository, never())
                .save(any());
    }

    @Test
    void createSale_shouldThrowException_whenUserNotFound() {

        SaleDetailRequestDTO itemRequest =
                new SaleDetailRequestDTO(1L, 2);

        SaleRequestDTO request =
                new SaleRequestDTO(List.of(itemRequest));

        when(userRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                saleService.createSale(request, 99L)
        )
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User not found");

        verify(productRepository, never())
                .findById(any());

        verify(saleRepository, never())
                .save(any());
    }

    @Test
    void createSale_shouldThrowException_whenProductNotFound() {

        SaleDetailRequestDTO itemRequest =
                new SaleDetailRequestDTO(99L, 2);

        SaleRequestDTO request =
                new SaleRequestDTO(List.of(itemRequest));

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(productRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                saleService.createSale(request, 1L)
        )
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Product not found");

        verify(saleRepository, never())
                .save(any());
    }

    @Test
    void getSaleById_shouldThrowException_whenSaleNotFound() {

        when(saleRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                saleService.getSaleById(1L)
        )
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Sale not found");
    }
}

