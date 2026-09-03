package com.example.projectProduct.auth;

import com.example.projectProduct.security.JwtService;
import com.example.projectProduct.user.Role;
import com.example.projectProduct.user.User;
import com.example.projectProduct.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponseDTO login(LoginRequestDTO dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.username(), dto.password())
        );

        User user = userRepository.findByUsername(dto.username())
                .orElseThrow(() -> new IllegalStateException("User not found: " + dto.username()));

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", user.getRole().name());

        String token = jwtService.generateToken(extraClaims, user);
        return new AuthResponseDTO(token);
    }

    @Override
    public AuthResponseDTO register(RegisterRequestDTO dto) {
        User user = new User();
        user.setUsername(dto.username());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setRole(Role.USER);

        userRepository.save(user);

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", user.getRole().name());

        String token = jwtService.generateToken(extraClaims, user);
        return new AuthResponseDTO(token);
    }
}