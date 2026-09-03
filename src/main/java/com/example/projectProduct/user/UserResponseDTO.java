package com.example.projectProduct.user;

public record UserResponseDTO(
        Long id,
        String username,
        Role role,
        boolean enabled
) {}