package com.example.projectProduct.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequestDTO(
        @NotBlank(message = "Username is required") String username,
        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must have at least 6 characters") String password
) {}