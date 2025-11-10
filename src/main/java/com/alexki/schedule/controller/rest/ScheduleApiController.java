package com.alexki.schedule.controller.rest;

import com.alexki.schedule.dto.ScheduleDto;
import com.alexki.schedule.entities.Schedule;
import com.alexki.schedule.mapper.ScheduleMapper;
import com.alexki.schedule.services.ScheduleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/schedule")
public class ScheduleApiController {

    private final ScheduleService scheduleService;
    private final ScheduleMapper scheduleMapper;

    public ScheduleApiController(ScheduleService scheduleService, ScheduleMapper scheduleMapper) {
        this.scheduleService = scheduleService;
        this.scheduleMapper = scheduleMapper;
    }

    @GetMapping
    public List<ScheduleDto> listSchedule() {
        return scheduleService.getSchedules()
                .stream()
                .map(scheduleMapper::toDto)
                .toList();
    }

    @PostMapping
    public ScheduleDto createSchedule(@RequestBody ScheduleDto scheduleDto) {
        Schedule newSchedule = scheduleService.createSchedule(scheduleMapper.toEntity(scheduleDto));
        return scheduleMapper.toDto(newSchedule);
    }

    @GetMapping("/{schedule_id}")
    public ScheduleDto getSchedule(@PathVariable("schedule_id") UUID id) {

        Schedule schedule = scheduleService.getSchedule(id);

        return scheduleMapper.toDto(schedule);
    }


    @PutMapping("/{schedule_id}")
    public ScheduleDto updateSchedule(@PathVariable("schedule_id") UUID id, @RequestBody ScheduleDto scheduleDto) {
        Schedule updatedSchedule = scheduleService.updateSchedule(id, scheduleMapper.toEntity(scheduleDto));

        return scheduleMapper.toDto(updatedSchedule);

    }

    @DeleteMapping("/{schedule_id}")
    public void deleteSchedule(@PathVariable("schedule_id") UUID id) {
        scheduleService.deleteSchedule(id);
    }
}
