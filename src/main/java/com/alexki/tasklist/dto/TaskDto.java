package com.alexki.tasklist.dto;

import com.alexki.tasklist.entities.TaskPriority;
import com.alexki.tasklist.entities.TaskStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskDto(
        UUID id,
        String title,
        String description,
        LocalDateTime dueDate,
        TaskPriority priority,
        TaskStatus status
) {
}
