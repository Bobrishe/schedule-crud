package com.alexki.tasklist.services;

import com.alexki.tasklist.entities.Task;
import com.alexki.tasklist.entities.TaskList;
import com.alexki.tasklist.entities.TaskPriority;
import com.alexki.tasklist.entities.TaskStatus;
import com.alexki.tasklist.repositories.TaskListRepository;
import com.alexki.tasklist.repositories.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskListRepository taskListRepository;

    private final String ID_IS_NULL = "Task list ID can't be NULL";

    public TaskService(TaskRepository taskRepository, TaskListRepository taskListRepository) {
        this.taskRepository = taskRepository;
        this.taskListRepository = taskListRepository;
    }


    public List<Task> getTaskListByListId(UUID id) {
        return taskRepository.findByTaskListId(id);
    }

    public Optional<Task> getTaskByListIdAndId(UUID taskListId, UUID id) {
        return taskRepository.findByTaskListIdAndId(taskListId, id);
    }

    @Transactional
    public Task createNewTask(UUID taskListId, Task task) {
        if (task.getId() != null) {
            throw new IllegalArgumentException("Task already has an ID!");
        }
        if (task.getTitle() == null || task.getTitle().isBlank()) {
            throw new IllegalArgumentException("There is no title, or a title is empty!");
        }

        LocalDateTime now = LocalDateTime.now();

        TaskList taskList = taskListRepository.findById(taskListId).orElseThrow(
                () -> new IllegalArgumentException("Task list not found")
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
                        taskList
                )
        );
    }

    @Transactional
    public Task updateTask(UUID taskListId, UUID id, Task task) {
        if (task.getId() == null) {
            throw new IllegalArgumentException(ID_IS_NULL);
        }
        if (!task.getId().equals(id)) {
            throw new IllegalArgumentException("Attempting to change list ID");
        }

        Task existingTask = taskRepository.findByTaskListIdAndId(taskListId, id)
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
    public void deleteTask(UUID taskListId, UUID taskId) {
        Task task = taskRepository.findByTaskListIdAndId(taskListId, taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task or Task list not found!"));

        taskRepository.delete(task);
    }
}
