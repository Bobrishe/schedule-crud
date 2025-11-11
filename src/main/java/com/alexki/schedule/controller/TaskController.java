package com.alexki.schedule.controller;

import com.alexki.schedule.dto.ScheduleDto;
import com.alexki.schedule.dto.TaskDto;
import com.alexki.schedule.entities.Schedule;
import com.alexki.schedule.mapper.ScheduleMapper;
import com.alexki.schedule.services.ScheduleService;
import com.alexki.schedule.entities.Task;
import com.alexki.schedule.entities.TaskPriority;
import com.alexki.schedule.entities.TaskStatus;
import com.alexki.schedule.mapper.TaskMapper;
import com.alexki.schedule.services.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/schedule/{schedule_id}")
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;
    private final ScheduleService scheduleService;
    private final ScheduleMapper scheduleMapper;

    public TaskController(TaskService taskService, TaskMapper taskMapper, ScheduleService scheduleService, ScheduleMapper scheduleMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
        this.scheduleService = scheduleService;
        this.scheduleMapper = scheduleMapper;
    }


    @ModelAttribute("schedule")
    public ScheduleDto getSchedule(@PathVariable UUID schedule_id) {
        Schedule schedule = scheduleService.getSchedule(schedule_id);
        return scheduleMapper.toDto(schedule);
    }

    @GetMapping()
    public String getTasks(@PathVariable UUID schedule_id, Model model) {
        List<TaskDto> taskDtoList = scheduleService.getScheduleTasksById(schedule_id)
                .stream().map(taskMapper::toDto)
                .toList();

        model.addAttribute("tasks", taskDtoList);

        return "task/index";
    }

    @GetMapping("/create")
    public String createTask(Model model){
        model.addAttribute("task",taskMapper.toDto(new Task()));
        model.addAttribute("priority", TaskPriority.values());
        return "task/create";
    }

    @GetMapping("/{task_id}")
    public String getTaskById(@PathVariable UUID schedule_id, @PathVariable UUID task_id, Model model) {
        Task task = taskService.getTaskByListIdAndId(schedule_id, task_id);
        model.addAttribute("task", taskMapper.toDto(task));

        return "task/details";
    }

    @GetMapping("/edit/{task_id}")
    public String editTaskIndex(@PathVariable UUID schedule_id, @PathVariable UUID task_id, Model model){

        Task  task = taskService.getTaskByListIdAndId(schedule_id,task_id);
        model.addAttribute("task",taskMapper.toDto(task));
        model.addAttribute("priority",TaskPriority.values());
        model.addAttribute("status", TaskStatus.values());

        return "task/edit";

    }


    @PostMapping
    public String createNewTask(@PathVariable UUID schedule_id, @ModelAttribute TaskDto task) {
        taskService.createNewTask(schedule_id, taskMapper.toEntity(task));

        return getHomePageRedirect(schedule_id);
    }

    @PostMapping("/edit/{task_id}")
    public String updateTask(@PathVariable UUID schedule_id,
                             @PathVariable UUID task_id,
                             @ModelAttribute TaskDto task) {

        taskService.updateTask(schedule_id, task_id, taskMapper.toEntity(task));

        return getHomePageRedirect(schedule_id);

    }

    @GetMapping("/delete/{task_id}")
    public String deleteTask(@PathVariable UUID schedule_id, @PathVariable UUID task_id) {
        taskService.deleteTask(schedule_id, task_id);
        return getHomePageRedirect(schedule_id);
    }

    private String getHomePageRedirect(UUID schedule_id) {
        return "redirect:/schedule/%s".formatted(schedule_id.toString());
    }
}
