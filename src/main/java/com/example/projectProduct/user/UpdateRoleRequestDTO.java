package com.example.projectProduct.user;

import jakarta.validation.constraints.NotNull;

public record UpdateRoleRequestDTO(
        @NotNull(message = "Role is required") Role role
) {}