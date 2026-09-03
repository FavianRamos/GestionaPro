package com.example.projectProduct.product;

import com.example.projectProduct.category.Category;
import com.example.projectProduct.category.CategoryRepository;
import com.example.projectProduct.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    private Category category;
    private Product product;
    private ProductRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        product = new Product();
        product.setId(1L);
        product.setName("Laptop");
        product.setPrice(BigDecimal.valueOf(3500));
        product.setStock(20);
        product.setCategory(category);

        requestDTO = new ProductRequestDTO("Laptop", BigDecimal.valueOf(3500), 20, 1L);
    }

    @Test
    void create_shouldSaveProduct_whenCategoryExists() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productMapper.toEntity(requestDTO)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toResponseDTO(product)).thenReturn(
                new ProductResponseDTO(1L, "Laptop", BigDecimal.valueOf(3500), 20, "Electronics")
        );

        ProductResponseDTO result = productService.create(requestDTO);

        assertThat(result.name()).isEqualTo("Laptop");
        verify(productRepository).save(product);
    }

    @Test
    void create_shouldThrowException_whenCategoryNotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.create(requestDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Category not found");

        verify(productRepository, never()).save(any());
    }

    @Test
    void findById_shouldReturnProduct_whenExists() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.toResponseDTO(product)).thenReturn(
                new ProductResponseDTO(1L, "Laptop", BigDecimal.valueOf(3500), 20, "Electronics")
        );

        ProductResponseDTO result = productService.findById(1L);

        assertThat(result.id()).isEqualTo(1L);
    }

    @Test
    void findById_shouldThrowException_whenNotExists() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.findById(1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void delete_shouldThrowException_whenProductNotExists() {
        when(productRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> productService.delete(1L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(productRepository, never()).deleteById(any());
    }
}