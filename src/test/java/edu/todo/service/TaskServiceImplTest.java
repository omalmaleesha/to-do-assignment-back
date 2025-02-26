package edu.todo.service;


import edu.todo.dto.TaskDTO;
import edu.todo.model.Task;
import edu.todo.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task testTask1;
    private Task testTask2;
    private TaskDTO testTaskDTO;

    @BeforeEach
    public void setup() {
        // Setup test data
        testTask1 = new Task();
        testTask1.setId(1L);
        testTask1.setTitle("Test Task 1");
        testTask1.setDescription("Test Description 1");
        testTask1.setCompleted(false);
        testTask1.setPriority(1);
        testTask1.setCreatedAt(LocalDateTime.now());

        testTask2 = new Task();
        testTask2.setId(2L);
        testTask2.setTitle("Test Task 2");
        testTask2.setDescription("Test Description 2");
        testTask2.setCompleted(true);
        testTask2.setPriority(2);
        testTask2.setCreatedAt(LocalDateTime.now());

        testTaskDTO = new TaskDTO();
        testTaskDTO.setTitle("New Task");
        testTaskDTO.setDescription("New Description");
        testTaskDTO.setCompleted(false);
        testTaskDTO.setPriority(3);
    }

    @Test
    public void createTask_ShouldReturnCreatedTaskDTO() {
        // Arrange
        Task savedTask = new Task();
        savedTask.setId(3L);
        savedTask.setTitle("New Task");
        savedTask.setDescription("New Description");
        savedTask.setCompleted(false);
        savedTask.setPriority(3);

        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        // Act
        TaskDTO result = taskService.createTask(testTaskDTO);

        // Assert
        assertNotNull(result);
        assertEquals(savedTask.getId(), result.getId());
        assertEquals(savedTask.getTitle(), result.getTitle());
        assertEquals(savedTask.getDescription(), result.getDescription());
        assertEquals(savedTask.getCompleted(), result.getCompleted());
        assertEquals(savedTask.getPriority(), result.getPriority());

        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    public void updateTask_WhenTaskExists_ShouldReturnUpdatedTaskDTO() {
        // Arrange
        Long taskId = 1L;
        TaskDTO updateDTO = new TaskDTO();
        updateDTO.setTitle("Updated Title");
        updateDTO.setDescription("Updated Description");
        updateDTO.setCompleted(true);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(testTask1));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        TaskDTO result = taskService.updateTask(taskId, updateDTO);

        // Assert
        assertNotNull(result);
        assertEquals(taskId, result.getId());
        assertEquals("Updated Title", result.getTitle());
        assertEquals("Updated Description", result.getDescription());
        assertTrue(result.getCompleted());

        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    public void updateTask_WhenTaskDoesNotExist_ShouldReturnNull() {
        // Arrange
        Long taskId = 999L;
        TaskDTO updateDTO = new TaskDTO();
        updateDTO.setTitle("Updated Title");

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // Act
        TaskDTO result = taskService.updateTask(taskId, updateDTO);

        // Assert
        assertNull(result);

        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    public void deleteTask_WhenTaskExists_ShouldDeleteTask() {
        // Arrange
        Long taskId = 1L;
        when(taskRepository.existsById(taskId)).thenReturn(true);
        doNothing().when(taskRepository).deleteById(taskId);

        // Act
        taskService.deleteTask(taskId);

        // Assert
        verify(taskRepository, times(1)).existsById(taskId);
        verify(taskRepository, times(1)).deleteById(taskId);
    }

    @Test
    public void deleteTask_WhenTaskDoesNotExist_ShouldStillCallDeleteById() {
        // Arrange
        Long taskId = 999L;
        when(taskRepository.existsById(taskId)).thenReturn(false);
        doNothing().when(taskRepository).deleteById(taskId);

        // Act
        taskService.deleteTask(taskId);

        // Assert
        verify(taskRepository, times(1)).existsById(taskId);
        verify(taskRepository, times(1)).deleteById(taskId);
    }

    @Test
    public void getTask_ShouldReturnTaskDTO() {
        // Arrange
        Long taskId = 1L;
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(testTask1));

        // Act
        TaskDTO result = taskService.getTask(taskId);

        // Assert
        assertNotNull(result);
        // Note: This is expected to fail due to a bug in the implementation
        // where it tries to copy from Optional<Task> to TaskDTO directly
        // This test highlights that bug
    }

    @Test
    public void getTask_WhenTaskDoesNotExist_ShouldReturnEmptyDTO() {
        // Arrange
        Long taskId = 999L;
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // Act
        TaskDTO result = taskService.getTask(taskId);

        // Assert
        assertNotNull(result);
        // This will also fail due to the same bug as above
    }

    @Test
    public void getAllTasks_ShouldReturnAllTaskDTOs() {
        // Arrange
        List<Task> tasks = Arrays.asList(testTask1, testTask2);
        when(taskRepository.findAll()).thenReturn(tasks);

        // Act
        List<TaskDTO> results = taskService.getAllTasks();

        // Assert
        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals(testTask1.getId(), results.get(0).getId());
        assertEquals(testTask1.getTitle(), results.get(0).getTitle());
        assertEquals(testTask2.getId(), results.get(1).getId());
        assertEquals(testTask2.getTitle(), results.get(1).getTitle());

        verify(taskRepository, times(1)).findAll();
    }

    @Test
    public void getTasksByStatus_ShouldReturnFilteredTasks() {
        // Arrange
        boolean completed = true;
        List<Task> completedTasks = Arrays.asList(testTask2);
        when(taskRepository.findByCompletedOrderByPriorityDesc(completed)).thenReturn(completedTasks);

        // Act
        List<TaskDTO> results = taskService.getTasksByStatus(completed);

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(testTask2.getId(), results.get(0).getId());
        assertEquals(testTask2.getTitle(), results.get(0).getTitle());
        assertTrue(results.get(0).getCompleted());

        verify(taskRepository, times(1)).findByCompletedOrderByPriorityDesc(completed);
    }
}
