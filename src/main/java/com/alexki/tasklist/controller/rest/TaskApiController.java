package com.alexki.tasklist.controller.rest;

import com.alexki.tasklist.dto.TaskDto;
import com.alexki.tasklist.entities.Task;
import com.alexki.tasklist.mapper.TaskMapper;
import com.alexki.tasklist.services.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/task-lists/{task_list_id}/tasks")
public class TaskApiController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    public TaskApiController(TaskService taskService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }


    @GetMapping()
    public List<TaskDto> getTasks(@PathVariable UUID task_list_id) {
        return taskService.getTaskListByListId(task_list_id)
                .stream().map(taskMapper::toDto)
                .toList();
    }

    @GetMapping("/{task_id}")
    public Optional<TaskDto> getTaskById(@PathVariable UUID task_list_id, @PathVariable UUID task_id) {
        Optional<Task> task = taskService.getTaskByListIdAndId(task_list_id, task_id);
        TaskDto dto = task.map(taskMapper::toDto).orElse(null);
        int ii;
        return task.map(taskMapper::toDto);
    }


    @PostMapping
    public TaskDto createNewTask(@PathVariable UUID task_list_id, @RequestBody TaskDto taskDto) {
        Task createdTaskDto = taskService.createNewTask(task_list_id, taskMapper.toEntity(taskDto));
        return taskMapper.toDto(createdTaskDto);
    }

    @PutMapping("/{task_id}")
    public TaskDto updateTask(@PathVariable UUID task_list_id,
                              @PathVariable UUID task_id,
                              @RequestBody TaskDto taskDto) {

        Task updatedTask = taskService.updateTask(task_list_id, task_id, taskMapper.toEntity(taskDto));

        return taskMapper.toDto(updatedTask);

    }

    @DeleteMapping("/{task_id}")
    public void deleteTask(@PathVariable UUID task_list_id, @PathVariable UUID task_id) {
        taskService.deleteTask(task_list_id, task_id);
    }
}
