package com.alexki.schedule.dto;

import java.util.List;
import java.util.UUID;

public record ScheduleDto(
        UUID id,
        String title,
        String description,
        Integer count,
        Double progress,
        List<TaskDto> tasks
) {
}
