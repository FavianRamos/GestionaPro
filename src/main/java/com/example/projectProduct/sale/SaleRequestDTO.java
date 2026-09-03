package com.example.projectProduct.sale;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record SaleRequestDTO(

        @NotEmpty(message = "The sale must have at least one product")
        @Valid
        List<SaleDetailRequestDTO> details
) {}