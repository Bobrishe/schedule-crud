package com.alexki.schedule.controller;

import com.alexki.schedule.services.ScheduleService;
import com.alexki.schedule.dto.ScheduleDto;
import com.alexki.schedule.entities.Schedule;
import com.alexki.schedule.mapper.ScheduleMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping(path = "/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final ScheduleMapper scheduleMapper;
    private final String homePageRedirect = "redirect:/schedule";

    public ScheduleController(ScheduleService scheduleService, ScheduleMapper scheduleMapper) {
        this.scheduleService = scheduleService;
        this.scheduleMapper = scheduleMapper;
    }

    @GetMapping
    public String getSchedule(Model model) {
        List<ScheduleDto> scheduleDto = scheduleService.getSchedules()
                .stream()
                .map(scheduleMapper::toDto)
                .toList();

        model.addAttribute("schedules", scheduleDto);

        return "schedule/index";
    }

    @GetMapping("/create")
    public String createScheduleIndex(Model model) {
        model.addAttribute("schedule", scheduleMapper.toDto(new Schedule()));
        return "schedule/create";
    }

    @PostMapping
    public String createSchedule(@ModelAttribute ScheduleDto schedule) {
        scheduleService.createSchedule(scheduleMapper.toEntity(schedule));
        return homePageRedirect;
    }

    @GetMapping("/edit/{schedule_id}")
    public String editScheduleIndex(@PathVariable("schedule_id") UUID id, Model model) {

        Schedule schedule = scheduleService.getSchedule(id);
        model.addAttribute("schedule", scheduleMapper.toDto(schedule));

        return "schedule/edit";

    }

    @PostMapping("/edit/{schedule_id}")
    public String editSchedule(@PathVariable("schedule_id") UUID id, @ModelAttribute ScheduleDto schedule) {
        scheduleService.updateSchedule(id, scheduleMapper.toEntity(schedule));

        return homePageRedirect;

    }

    @GetMapping("/delete/{schedule_id}")
    public String deleteSchedule(@PathVariable("schedule_id") UUID id) {
        scheduleService.deleteSchedule(id);
        return homePageRedirect;
    }
}
