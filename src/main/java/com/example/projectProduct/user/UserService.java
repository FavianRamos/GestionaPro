package com.example.projectProduct.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Page<UserResponseDTO> findAll(Pageable pageable);
    UserResponseDTO findById(Long id);
    UserResponseDTO updateRole(Long id, Role role);
    UserResponseDTO toggleEnabled(Long id);
    long countAll();
}