package com.alexki.tasklist.mapper;

import com.alexki.tasklist.dto.TaskListDto;
import com.alexki.tasklist.entities.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TaskListMapper implements BaseMapper<TaskListDto, TaskList> {

    private final TaskMapper taskMapper;

    public TaskListMapper(TaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }

    @Override
    public TaskListDto toDto(TaskList taskList) {
        return new TaskListDto(
                taskList.getId(),
                taskList.getTitle(),
                taskList.getDescription(),
                Optional.ofNullable(taskList.getTasks()).map(List::size)
                        .orElse(0),
                calculateProgress(taskList.getTasks()),
                Optional.ofNullable(taskList.getTasks())
                        .map(tasks -> tasks.stream()
                                .map(taskMapper::toDto).toList())
                                .orElse(null)

        );
    }

    @Override
    public TaskList toEntity(TaskListDto dto) {
        return new TaskList(
                dto.id(),
                dto.title(),
                dto.description(),
                Optional.ofNullable(dto.tasks())
                        .map(tasks -> tasks.stream()
                                .map(taskMapper::toEntity).toList()).orElse(null),
                null,
                null
        );
    }

    private Double calculateProgress(List<Task> tasks) {
        if (tasks == null)
            return null;

        long closedTasks = tasks.stream()
                .filter(task -> TaskStatus.CLOSED == task.getStatus())
                .count();

        return (double) closedTasks / tasks.size();
    }
}
