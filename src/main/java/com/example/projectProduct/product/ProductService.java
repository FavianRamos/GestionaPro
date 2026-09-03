package com.example.projectProduct.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    ProductResponseDTO create(ProductRequestDTO dto);
    ProductResponseDTO findById(Long id);
    Page<ProductResponseDTO> findAll(String name, Pageable pageable);
    ProductResponseDTO update(Long id, ProductRequestDTO dto);
    void delete(Long id);
    long countAll();
}