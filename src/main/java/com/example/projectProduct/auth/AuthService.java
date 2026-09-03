package com.example.projectProduct.auth;

public interface AuthService {
    AuthResponseDTO login(LoginRequestDTO dto);
    AuthResponseDTO register(RegisterRequestDTO dto);
}