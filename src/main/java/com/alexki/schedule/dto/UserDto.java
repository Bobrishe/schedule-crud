package com.alexki.schedule.dto;

import com.alexki.schedule.entities.UserRole;
import jakarta.validation.constraints.*;

import java.util.UUID;

public record UserDto(
        UUID id,
        @Email
        @NotBlank
        String email,

        @Min(value = 10, message = "Password must be at least 10 characters long")
        @NotBlank
        String password,
        UserRole role) {
}
