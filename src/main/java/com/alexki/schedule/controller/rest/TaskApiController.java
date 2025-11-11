package com.alexki.schedule.controller.rest;

import com.alexki.schedule.dto.TaskDto;
import com.alexki.schedule.entities.Task;
import com.alexki.schedule.mapper.TaskMapper;
import com.alexki.schedule.services.ScheduleService;
import com.alexki.schedule.services.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/schedule/{schedule_id}/tasks")
public class TaskApiController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;
    private final ScheduleService scheduleService;

    public TaskApiController(TaskService taskService, TaskMapper taskMapper, ScheduleService scheduleService) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
        this.scheduleService = scheduleService;
    }


    @GetMapping()
    public List<TaskDto> getSchedule(@PathVariable UUID schedule_id) {
        return scheduleService.getScheduleTasksById(schedule_id)
                .stream().map(taskMapper::toDto)
                .toList();
    }

    @GetMapping("/{task_id}")
    public TaskDto getScheduleId(@PathVariable UUID schedule_id, @PathVariable UUID task_id) {
        Task task = taskService.getTaskByListIdAndId(schedule_id, task_id);
        return taskMapper.toDto(task);
    }


    @PostMapping
    public TaskDto createNewSchedule(@PathVariable UUID schedule_id, @RequestBody TaskDto taskDto) {
        Task createdTaskDto = taskService.createNewTask(schedule_id, taskMapper.toEntity(taskDto));
        return taskMapper.toDto(createdTaskDto);
    }

    @PutMapping("/{task_id}")
    public TaskDto updateSchedule(@PathVariable UUID schedule_id,
                              @PathVariable UUID task_id,
                              @RequestBody TaskDto taskDto) {

        Task updatedTask = taskService.updateTask(schedule_id, task_id, taskMapper.toEntity(taskDto));

        return taskMapper.toDto(updatedTask);

    }

    @DeleteMapping("/{task_id}")
    public void deleteSchedule(@PathVariable UUID schedule_id, @PathVariable UUID task_id) {
        taskService.deleteTask(schedule_id, task_id);
    }
}
