package com.alexki.tasklist.controller;

import com.alexki.tasklist.dto.TaskDto;
import com.alexki.tasklist.entities.Task;
import com.alexki.tasklist.mapper.TaskMapper;
import com.alexki.tasklist.services.TaskService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/task-lists/{task_list_id}/tasks")
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    public TaskController(TaskService taskService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    @GetMapping()
    public String getTasks(@PathVariable UUID task_list_id, Model model) {
        List<TaskDto> taskDtoList = taskService.getTaskListByListId(task_list_id)
                .stream().map(taskMapper::toDto)
                .toList();
        model.addAttribute("tasks_list", taskDtoList);
        return "task/all_tasks";
    }

    @GetMapping("/{task_id}")
    public String getTaskById(@PathVariable UUID task_list_id, @PathVariable UUID task_id, Model model) {
        Optional<Task> task = taskService.getTaskByListIdAndId(task_list_id, task_id);
        TaskDto taskDto = task.map(taskMapper::toDto).orElse(null);
        model.addAttribute("task", taskDto);

        return "list/one_task";
    }


    @PostMapping
    public String createNewTask(@PathVariable UUID task_list_id, @RequestBody TaskDto taskDto) {
        taskService.createNewTask(task_list_id, taskMapper.toEntity(taskDto));

        return getHomePageRedirect(task_list_id);
    }

    @PutMapping("/{task_id}")
    public String updateTask(@PathVariable UUID task_list_id,
                             @PathVariable UUID task_id,
                             @RequestBody TaskDto taskDto) {

        taskService.updateTask(task_list_id, task_id, taskMapper.toEntity(taskDto));

        return getHomePageRedirect(task_list_id);

    }

    @DeleteMapping("/{task_id}")
    public String deleteTask(@PathVariable UUID task_list_id, @PathVariable UUID task_id) {
        taskService.deleteTask(task_list_id, task_id);
        return getHomePageRedirect(task_list_id);
    }

    private String getHomePageRedirect(UUID task_list_id) {
        return "redirect:/task-lists/%s/tasks".formatted(task_list_id.toString());
    }
}
