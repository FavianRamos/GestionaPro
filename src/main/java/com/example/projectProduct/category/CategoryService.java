package com.example.projectProduct.category;

import java.util.List;

public interface CategoryService {
    CategoryResponseDTO create(CategoryRequestDTO dto);
    CategoryResponseDTO findById(Long id);
    List<CategoryResponseDTO> findAll();
    CategoryResponseDTO update(Long id, CategoryRequestDTO dto);
    void delete(Long id);
}