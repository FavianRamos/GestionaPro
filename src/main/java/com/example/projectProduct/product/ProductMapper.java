package com.example.projectProduct.product;

import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductResponseDTO toResponseDTO(Product product) {
        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getStock(),
                product.getCategory() != null ? product.getCategory().getName() : null
        );
    }

    public Product toEntity(ProductRequestDTO dto) {
        Product product = new Product();
        product.setName(dto.name());
        product.setPrice(dto.price());
        product.setStock(dto.stock());
        return product;
    }
}