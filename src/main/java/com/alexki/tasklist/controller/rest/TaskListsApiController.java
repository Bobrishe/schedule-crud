package com.alexki.tasklist.controller.rest;

import com.alexki.tasklist.dto.TaskListDto;
import com.alexki.tasklist.entities.TaskList;
import com.alexki.tasklist.mapper.TaskListMapper;
import com.alexki.tasklist.services.TaskListService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/task-lists")
public class TaskListsApiController {

    private final TaskListService taskListService;
    private final TaskListMapper taskListMapper;

    public TaskListsApiController(TaskListService taskListService, TaskListMapper taskListMapper) {
        this.taskListService = taskListService;
        this.taskListMapper = taskListMapper;
    }

    @GetMapping
    public List<TaskListDto> listTaskLists() {
        return taskListService.getTaskLists()
                .stream()
                .map(taskListMapper::toDto)
                .toList();
    }

    @PostMapping
    public TaskListDto createTaskList(@RequestBody TaskListDto taskListDto) {
        TaskList newTaskList = taskListService.createTaskList(taskListMapper.toEntity(taskListDto));
        return taskListMapper.toDto(newTaskList);
    }

    @GetMapping("/{task_list_id}")
    public Optional<TaskListDto> getTaskList(@PathVariable("task_list_id") UUID id) {

        Optional<TaskList> taskList = taskListService.getTaskList(id);

        return taskList.map(taskListMapper::toDto);
    }


    @PutMapping("/{task_list_id}")
    public TaskListDto updateTaskList(@PathVariable("task_list_id") UUID id, @RequestBody TaskListDto taskListDto) {
        TaskList updatedTaskList = taskListService.updateTaskList(id, taskListMapper.toEntity(taskListDto));

        return taskListMapper.toDto(updatedTaskList);

    }

    @DeleteMapping("/{task_list_id}")
    public void deleteTaskList(@PathVariable("task_list_id") UUID id) {
        taskListService.deleteTaskList(id);
    }
}
