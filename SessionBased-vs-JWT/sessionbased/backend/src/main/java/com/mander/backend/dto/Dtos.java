package com.mander.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class Dtos {

    public record LoginRequest(@NotBlank String username, @NotBlank String password) {
    }

    public record ChangePasswordRequest(
            @NotBlank String currentPassword,
            @NotBlank @Size(min = 6) String newPassword) {
    }

    public record UserInfo(String username, String role) {
    }

    public record MessageResponse(String message) {
    }

    public record AdminUserView(Long id, String username, String role, boolean enabled, int activeSessions) {
    }
}
