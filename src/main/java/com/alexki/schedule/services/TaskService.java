package com.alexki.schedule.services;

import com.alexki.schedule.entities.Schedule;
import com.alexki.schedule.entities.Task;
import com.alexki.schedule.entities.TaskPriority;
import com.alexki.schedule.entities.TaskStatus;
import com.alexki.schedule.repositories.ScheduleRepository;
import com.alexki.schedule.repositories.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final ScheduleRepository scheduleRepository;

    private final String ID_IS_NULL = "Schedule ID can't be NULL";

    public TaskService(TaskRepository taskRepository, ScheduleRepository scheduleRepository) {
        this.taskRepository = taskRepository;
        this.scheduleRepository = scheduleRepository;
    }

    public Task getTaskByListIdAndId(UUID scheduleId, UUID id) {
        return taskRepository.findByScheduleIdAndId(scheduleId, id).orElseThrow(() -> new IllegalArgumentException("Not found"));
    }

    @Transactional
    public Task createNewTask(UUID scheduleId, Task task) {
        if (task.getId() != null) {
            throw new IllegalArgumentException("Task already has an ID!");
        }
        if (task.getTitle() == null || task.getTitle().isBlank()) {
            throw new IllegalArgumentException("There is no title, or a title is empty!");
        }

        LocalDateTime now = LocalDateTime.now();

        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new IllegalArgumentException("Schedule not found")
        );

        TaskPriority priority = Optional.ofNullable(task.getPriority())
                .orElse(TaskPriority.MEDIUM);

        return taskRepository.save(
                new Task(
                        null,
                        task.getTitle(),
                        task.getDescription(),
                        task.getDueDate(),
                        TaskStatus.OPEN,
                        priority,
                        now,
                        now,
                        schedule
                )
        );
    }

    @Transactional
    public Task updateTask(UUID scheduleId, UUID id, Task task) {
        if (task.getId() == null) {
            throw new IllegalArgumentException(ID_IS_NULL);
        }
        if (!task.getId().equals(id)) {
            throw new IllegalArgumentException("Attempting to change list ID");
        }

        Task existingTask = taskRepository.findByScheduleIdAndId(scheduleId, id)
                .orElseThrow(() -> new IllegalArgumentException("No task found!!!"));

        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        existingTask.setDueDate(task.getDueDate());
        existingTask.setPriority(task.getPriority());
        existingTask.setStatus(task.getStatus());
        existingTask.setUpdated(LocalDateTime.now());

        return taskRepository.save(existingTask);

    }

    @Transactional
    public void deleteTask(UUID scheduleId, UUID taskId) {
        Task task = taskRepository.findByScheduleIdAndId(scheduleId, taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task or schedule not found!"));

        task.getSchedule().getTasks().remove(task);

        taskRepository.delete(task);

    }
}
