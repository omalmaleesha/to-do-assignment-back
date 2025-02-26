package edu.todo.service;

import edu.todo.dto.TaskDTO;

import java.util.List;

public interface TaskService {
    TaskDTO createTask(TaskDTO taskDTO);
    TaskDTO updateTask(Long id, TaskDTO taskDTO);
    void deleteTask(Long id);
    TaskDTO getTask(Long id);
    List<TaskDTO> getAllTasks();
    List<TaskDTO> getTasksByStatus(Boolean completed);
}