package com.alexki.tasklist.mapper;

import com.alexki.tasklist.dto.TaskDto;
import com.alexki.tasklist.entities.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper implements BaseMapper<TaskDto, Task> {

    @Override
    public TaskDto toDto(Task entity) {
        return new TaskDto(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getDueDate(),
                entity.getPriority(),
                entity.getStatus()
        );
    }

    @Override
    public Task toEntity(TaskDto dto) {
        return new Task(
                dto.id(),
                dto.title(),
                dto.description(),
                dto.dueDate(),
                dto.status(),
                dto.priority(),
                null,
                null,
                null
        );
    }
}
