package com.alexki.schedule.dto;

import com.alexki.schedule.entities.UserRole;

import java.util.UUID;

public record UserDto(
        UUID id,
        String email,
        String password,
        UserRole role) {
}
