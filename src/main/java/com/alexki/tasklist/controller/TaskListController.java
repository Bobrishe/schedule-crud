package com.alexki.tasklist.controller;

import com.alexki.tasklist.dto.TaskListDto;
import com.alexki.tasklist.entities.TaskList;
import com.alexki.tasklist.mapper.TaskListMapper;
import com.alexki.tasklist.services.TaskListService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path = "/task-lists")
public class TaskListController {

    private final TaskListService taskListService;
    private final TaskListMapper taskListMapper;
    private final String homePageRedirect = "redirect:/task-lists";

    public TaskListController(TaskListService taskListService, TaskListMapper taskListMapper) {
        this.taskListService = taskListService;
        this.taskListMapper = taskListMapper;
    }

    @GetMapping
    public String listTaskLists(Model model) {
        List<TaskListDto> taskListDto = taskListService.getTaskLists()
                .stream()
                .map(taskListMapper::toDto)
                .toList();

        model.addAttribute("tasks", taskListDto);

        return "task_list/all_task_lists";
    }

    @GetMapping("/{task_list_id}")
    public String getTaskList(@PathVariable("task_list_id") UUID id, Model model) {

        Optional<TaskList> taskList = taskListService.getTaskList(id);
        TaskListDto taskListDto = taskList.map(taskListMapper::toDto).orElse(null);
        model.addAttribute("taskList", taskListDto);
        return "task_list/one_task_list";
    }

    @PostMapping
    public String createTaskList(@RequestBody TaskListDto taskListDto) {
        taskListService.createTaskList(taskListMapper.toEntity(taskListDto));
        return homePageRedirect;
    }

    @PutMapping("/{task_list_id}")
    public String updateTaskList(@PathVariable("task_list_id") UUID id, @RequestBody TaskListDto taskListDto) {
        taskListService.updateTaskList(id, taskListMapper.toEntity(taskListDto));

        return homePageRedirect;

    }

    @DeleteMapping("/{task_list_id}")
    public String deleteTaskList(@PathVariable("task_list_id") UUID id) {
        taskListService.deleteTaskList(id);
        return homePageRedirect;
    }
}
