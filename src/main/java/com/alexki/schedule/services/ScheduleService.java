package com.alexki.schedule.services;

import com.alexki.schedule.entities.Task;
import com.alexki.schedule.entities.Schedule;
import com.alexki.schedule.repositories.ScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ScheduleService {

    public static final String ID_IS_NULL = "Schedule ID can't be NULL";
    public static final String NOT_FOUND = "Schedule not found";

    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository repository) {
        this.scheduleRepository = repository;
    }

    public List<Schedule> getSchedules() {
        return scheduleRepository.findAll();
    }

    @Transactional
    public Schedule createSchedule(Schedule schedule) {
        if (schedule.getId() != null) {
            throw new IllegalArgumentException("Task already has an ID!!!");
        }
        if (schedule.getTitle() == null || schedule.getTitle().isBlank()) {
            throw new IllegalArgumentException("There is no title, or a title is empty!");
        }

        LocalDateTime now = LocalDateTime.now();

        return scheduleRepository.save(new Schedule(
                null,
                schedule.getTitle(),
                schedule.getDescription(),
                null,
                now,
                now
        ));
    }


    public Schedule getSchedule(UUID id) {
        if (id == null) {
            throw new NullPointerException(ID_IS_NULL);
        }

        return scheduleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(NOT_FOUND));
    }

    public List<Task> getScheduleTasksById(UUID id) {
        if (id == null) {
            throw new NullPointerException(ID_IS_NULL);
        }

        Schedule schedule = scheduleRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException(NOT_FOUND)
        );

        return schedule.getTasks();

    }

    @Transactional
    public Schedule updateSchedule(UUID id, Schedule schedule) {
        if (schedule.getId() == null) {
            throw new NullPointerException(ID_IS_NULL);
        }
        if (!schedule.getId().equals(id)) {
            throw new IllegalArgumentException("Attempting to change schedule ID");
        }

        Schedule existingList = scheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND));

        existingList.setTitle(schedule.getTitle());
        existingList.setDescription(schedule.getDescription());
        existingList.setUpdated(LocalDateTime.now());

        return scheduleRepository.save(existingList);
    }

    @Transactional
    public void deleteSchedule(UUID id) {
        Schedule existingList = scheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND));

        scheduleRepository.delete(existingList);
    }

}
