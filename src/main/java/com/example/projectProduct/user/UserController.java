package com.example.projectProduct.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserResponseDTO>> findAll(Pageable pageable) {
        return ResponseEntity.ok(userService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PatchMapping("/{id}/role")
    public ResponseEntity<UserResponseDTO> updateRole(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRoleRequestDTO dto) {
        return ResponseEntity.ok(userService.updateRole(id, dto.role()));
    }

    @PatchMapping("/{id}/toggle-enabled")
    public ResponseEntity<UserResponseDTO> toggleEnabled(@PathVariable Long id) {
        return ResponseEntity.ok(userService.toggleEnabled(id));
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countAll() {
        return ResponseEntity.ok(userService.countAll());
    }
}