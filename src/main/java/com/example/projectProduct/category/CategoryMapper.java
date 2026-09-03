package com.example.projectProduct.category;

import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryResponseDTO toResponseDTO(Category category) {
        return new CategoryResponseDTO(category.getId(), category.getName());
    }

    public Category toEntity(CategoryRequestDTO dto) {
        Category category = new Category();
        category.setName(dto.name());
        return category;
    }
}