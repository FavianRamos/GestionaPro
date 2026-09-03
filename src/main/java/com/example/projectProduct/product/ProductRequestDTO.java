    package com.example.projectProduct.product;

    import jakarta.validation.constraints.NotBlank;
    import jakarta.validation.constraints.NotNull;
    import jakarta.validation.constraints.Positive;

    import java.math.BigDecimal;

    public record   ProductRequestDTO(
            @NotBlank(message = "Name is required") String name,
            @Positive(message = "Price must be greater than 0") BigDecimal price,
            Integer stock,
            @NotNull(message = "Category is required") Long categoryId
    ) {}