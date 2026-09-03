package com.example.projectProduct.sale;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SaleDetailRequestDTO(

        @NotNull(message = "Product id is required")
        Long productId,

        @Positive(message = "Quantity must be greater than zero")
        Integer quantity
) {}