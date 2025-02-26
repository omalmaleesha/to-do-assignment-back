package edu.todo.service;

import edu.todo.dto.TaskDTO;
import edu.todo.model.Task;
import edu.todo.repository.TaskRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public TaskDTO createTask(TaskDTO taskDTO) {
        Task task = new Task();
        BeanUtils.copyProperties(taskDTO, task);
        Task savedTask = taskRepository.save(task);
        TaskDTO savedTaskDTO = new TaskDTO();
        BeanUtils.copyProperties(savedTask, savedTaskDTO);
        return savedTaskDTO;
    }

    @Override
    public TaskDTO updateTask(Long id, TaskDTO taskDTO) {
        Optional<Task> existingTaskOptional = taskRepository.findById(id);

        if (existingTaskOptional.isPresent()) {
            Task existingTask = existingTaskOptional.get();

            BeanUtils.copyProperties(taskDTO, existingTask, "id");

            Task updatedTask = taskRepository.save(existingTask);

            TaskDTO updatedTaskDTO = new TaskDTO();
            BeanUtils.copyProperties(updatedTask, updatedTaskDTO);

            return updatedTaskDTO;
        } else {
            return null;
        }
    }

    @Override
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            System.out.println();
        }
        taskRepository.deleteById(id);
    }

    @Override
    public TaskDTO getTask(Long id) {
        Optional<Task> task = taskRepository.findById(id);
        TaskDTO taskDTO = new TaskDTO();
        BeanUtils.copyProperties(task, taskDTO);
        return taskDTO;
    }

    @Override
    public List<TaskDTO> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(task -> {
                    TaskDTO dto = new TaskDTO();
                    BeanUtils.copyProperties(task, dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> getTasksByStatus(Boolean completed) {
        return taskRepository.findByCompletedOrderByPriorityDesc(completed).stream()
                .map(task -> {
                    TaskDTO dto = new TaskDTO();
                    BeanUtils.copyProperties(task, dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }
}