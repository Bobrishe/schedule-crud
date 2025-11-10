package com.alexki.schedule.mapper;

import com.alexki.schedule.dto.ScheduleDto;
import com.alexki.schedule.entities.Schedule;
import com.alexki.schedule.entities.Task;
import com.alexki.schedule.entities.TaskStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ScheduleMapper implements BaseMapper<ScheduleDto, Schedule> {

    private final TaskMapper taskMapper;

    public ScheduleMapper(TaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }

    @Override
    public ScheduleDto toDto(Schedule schedule) {
        return new ScheduleDto(
                schedule.getId(),
                schedule.getTitle(),
                schedule.getDescription(),
                Optional.ofNullable(schedule.getTasks()).map(List::size)
                        .orElse(0),
                calculateProgress(schedule.getTasks()),
                Optional.ofNullable(schedule.getTasks())
                        .map(schedules -> schedules.stream()
                                .map(taskMapper::toDto).toList())
                                .orElse(null)

        );
    }

    @Override
    public Schedule toEntity(ScheduleDto dto) {
        return new Schedule(
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
