package com.alexki.tasklist.services;

import com.alexki.tasklist.entities.Task;
import com.alexki.tasklist.entities.TaskList;
import com.alexki.tasklist.repositories.TaskListRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskListService {

    private final String ID_IS_NULL = "Task list ID can't be NULL";

    private final TaskListRepository taskListRepository;

    public TaskListService(TaskListRepository repository) {
        this.taskListRepository = repository;
    }

    public List<TaskList> getTaskLists() {
        return taskListRepository.findAll();
    }

    @Transactional
    public TaskList createTaskList(TaskList taskList) {
        if (taskList.getId() != null) {
            throw new IllegalArgumentException("Task already has an ID!!!");
        }
        if (taskList.getTitle() == null || taskList.getTitle().isBlank()) {
            throw new IllegalArgumentException("There is no title, or a title is empty!");
        }

        LocalDateTime now = LocalDateTime.now();

        return taskListRepository.save(new TaskList(
                null,
                taskList.getTitle(),
                taskList.getDescription(),
                null,
                now,
                now
        ));
    }


    public Optional<TaskList> getTaskList(UUID id) {
        if (id == null) {
            throw new NullPointerException(ID_IS_NULL);
        }

        return taskListRepository.findById(id);
    }

    public List<Task> getTaskListsTasks(UUID id) {
        if (id == null) {
            throw new NullPointerException(ID_IS_NULL);
        }

        TaskList taskList = taskListRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Task list not found")
        );

        return taskList.getTasks();

    }

    @Transactional
    public TaskList updateTaskList(UUID id, TaskList taskList) {
        if (taskList.getId() == null) {
            throw new NullPointerException(ID_IS_NULL);
        }
        if (!taskList.getId().equals(id)) {
            throw new IllegalArgumentException("Attempting to change task list ID");
        }

        TaskList existingList = taskListRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task list not found"));

        existingList.setTitle(taskList.getTitle());
        existingList.setDescription(taskList.getDescription());
        existingList.setUpdated(LocalDateTime.now());

        return taskListRepository.save(existingList);
    }

    @Transactional
    public void deleteTaskList(UUID id) {
        TaskList existingList = taskListRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task list not found"));

        taskListRepository.delete(existingList);
    }

}
