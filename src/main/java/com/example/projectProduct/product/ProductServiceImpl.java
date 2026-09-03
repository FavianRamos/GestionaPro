package com.example.projectProduct.product;

import com.example.projectProduct.category.Category;
import com.example.projectProduct.category.CategoryRepository;
import com.example.projectProduct.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductResponseDTO create(ProductRequestDTO dto) {
        Category category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + dto.categoryId()));

        Product product = productMapper.toEntity(dto);
        product.setCategory(category);

        Product saved = productRepository.save(product);
        return productMapper.toResponseDTO(saved);
    }

    @Override
    public ProductResponseDTO findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return productMapper.toResponseDTO(product);
    }

    @Override
    public Page<ProductResponseDTO> findAll(String name, Pageable pageable) {
        if (name != null && !name.isBlank()) {
            return productRepository.findByNameContainingIgnoreCase(name, pageable)
                    .map(productMapper::toResponseDTO);
        }
        return productRepository.findAll(pageable)
                .map(productMapper::toResponseDTO);
    }

    @Override
    public ProductResponseDTO update(Long id, ProductRequestDTO dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        Category category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + dto.categoryId()));

        product.setName(dto.name());
        product.setPrice(dto.price());
        product.setStock(dto.stock());
        product.setCategory(category);

        Product updated = productRepository.save(product);
        return productMapper.toResponseDTO(updated);
    }

    @Override
    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    @Override
    public long countAll() {
        return productRepository.count();
    }
}