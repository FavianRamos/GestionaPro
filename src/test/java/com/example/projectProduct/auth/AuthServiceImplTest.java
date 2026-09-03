package com.example.projectProduct.auth;

import com.example.projectProduct.security.JwtService;
import com.example.projectProduct.user.Role;
import com.example.projectProduct.user.User;
import com.example.projectProduct.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    private AuthServiceImpl authService;

    private User user;

    @BeforeEach
    void setUp() {

        authService = new AuthServiceImpl(
                userRepository,
                passwordEncoder,
                jwtService,
                authenticationManager
        );

        user = new User();
        user.setId(1L);
        user.setUsername("favian");
        user.setPassword("encodedPassword");
        user.setRole(Role.USER);
    }

    @Test
    void login_shouldReturnToken_whenCredentialsAreValid() {

        // Arrange
        LoginRequestDTO request =
                new LoginRequestDTO("favian", "123456");

        when(userRepository.findByUsername("favian"))
                .thenReturn(Optional.of(user));

        when(jwtService.generateToken(any(Map.class), eq(user)))
                .thenReturn("jwt-token");

        // Act
        AuthResponseDTO result = authService.login(request);

        // Assert
        assertThat(result.token())
                .isEqualTo("jwt-token");

        verify(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        verify(userRepository)
                .findByUsername("favian");

        verify(jwtService)
                .generateToken(any(Map.class), eq(user));
    }

    @Test
    void login_shouldThrowException_whenUserNotFound() {

        // Arrange
        LoginRequestDTO request =
                new LoginRequestDTO("favian", "123456");

        when(userRepository.findByUsername("favian"))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() ->
                authService.login(request)
        )
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("User not found");

        verify(jwtService, never())
                .generateToken(any(Map.class), any(User.class));
    }

    @Test
    void register_shouldCreateUserAndReturnToken() {

        // Arrange
        RegisterRequestDTO request =
                new RegisterRequestDTO("favian", "123456");

        when(passwordEncoder.encode("123456"))
                .thenReturn("encodedPassword");

        when(jwtService.generateToken(any(Map.class), any(User.class)))
                .thenReturn("jwt-token");

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        AuthResponseDTO result = authService.register(request);

        // Assert
        assertThat(result.token())
                .isEqualTo("jwt-token");

        verify(passwordEncoder)
                .encode("123456");

        verify(userRepository)
                .save(any(User.class));

        verify(jwtService)
                .generateToken(any(Map.class), any(User.class));
    }

    @Test
    void register_shouldEncodePassword() {

        // Arrange
        RegisterRequestDTO request =
                new RegisterRequestDTO("favian", "123456");

        when(passwordEncoder.encode("123456"))
                .thenReturn("encodedPassword");

        when(jwtService.generateToken(any(Map.class), any(User.class)))
                .thenReturn("jwt-token");

        // Act
        authService.register(request);

        // Assert
        ArgumentCaptor<User> userCaptor =
                ArgumentCaptor.forClass(User.class);

        verify(userRepository)
                .save(userCaptor.capture());

        User savedUser = userCaptor.getValue();

        assertThat(savedUser.getPassword())
                .isEqualTo("encodedPassword");
    }

    @Test
    void register_shouldAssignUserRole() {

        // Arrange
        RegisterRequestDTO request =
                new RegisterRequestDTO("favian", "123456");

        when(passwordEncoder.encode("123456"))
                .thenReturn("encodedPassword");

        when(jwtService.generateToken(any(Map.class), any(User.class)))
                .thenReturn("jwt-token");

        // Act
        authService.register(request);

        // Assert
        ArgumentCaptor<User> userCaptor =
                ArgumentCaptor.forClass(User.class);

        verify(userRepository)
                .save(userCaptor.capture());

        User savedUser = userCaptor.getValue();

        assertThat(savedUser.getRole())
                .isEqualTo(Role.USER);
    }
}

