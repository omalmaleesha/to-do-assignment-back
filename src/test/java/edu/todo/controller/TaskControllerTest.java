package edu.todo.controller;


import edu.todo.dto.TaskDTO;
import edu.todo.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    private TaskDTO testTask;
    private List<TaskDTO> testTasks;

    @BeforeEach
    public void setup() {
        // Setup test data
        testTask = new TaskDTO();
        testTask.setId(1L);
        testTask.setTitle("Test Task");
        testTask.setDescription("Test Description");
        testTask.setCompleted(false);
        testTask.setPriority(1);

        TaskDTO testTask2 = new TaskDTO();
        testTask2.setId(2L);
        testTask2.setTitle("Test Task 2");
        testTask2.setDescription("Test Description 2");
        testTask2.setCompleted(true);
        testTask2.setPriority(2);

        testTasks = Arrays.asList(testTask, testTask2);
    }

    @Test
    public void createTask_ShouldReturnCreatedTask() throws Exception {
        when(taskService.createTask(any(TaskDTO.class))).thenReturn(testTask);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testTask)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.completed").value(false));

        verify(taskService, times(1)).createTask(any(TaskDTO.class));
    }

    @Test
    public void updateTask_ShouldReturnUpdatedTask() throws Exception {
        Long taskId = 1L;

        when(taskService.updateTask(eq(taskId), any(TaskDTO.class))).thenReturn(testTask);

        mockMvc.perform(put("/api/tasks/{id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testTask)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Task"));

        verify(taskService, times(1)).updateTask(eq(taskId), any(TaskDTO.class));
    }

    @Test
    public void deleteTask_ShouldReturnNoContent() throws Exception {
        Long taskId = 1L;

        doNothing().when(taskService).deleteTask(taskId);

        mockMvc.perform(delete("/api/tasks/{id}", taskId))
                .andExpect(status().isNoContent());

        verify(taskService, times(1)).deleteTask(taskId);
    }

    @Test
    public void getTask_ShouldReturnTask() throws Exception {
        Long taskId = 1L;

        when(taskService.getTask(taskId)).thenReturn(testTask);

        mockMvc.perform(get("/api/tasks/{id}", taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Task"));

        verify(taskService, times(1)).getTask(taskId);
    }

    @Test
    public void getAllTasks_ShouldReturnAllTasks() throws Exception {
        when(taskService.getAllTasks()).thenReturn(testTasks);

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Task"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("Test Task 2"));

        verify(taskService, times(1)).getAllTasks();
    }

    @Test
    public void getTasksByStatus_ShouldReturnCompletedTasks() throws Exception {
        List<TaskDTO> completedTasks = Arrays.asList(testTasks.get(1));
        when(taskService.getTasksByStatus(true)).thenReturn(completedTasks);

        mockMvc.perform(get("/api/tasks")
                        .param("completed", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].completed").value(true));

        verify(taskService, times(1)).getTasksByStatus(true);
    }

    @Test
    public void getTasksByStatus_ShouldReturnIncompleteTasks() throws Exception {
        List<TaskDTO> incompleteTasks = Arrays.asList(testTasks.get(0));
        when(taskService.getTasksByStatus(false)).thenReturn(incompleteTasks);

        mockMvc.perform(get("/api/tasks")
                        .param("completed", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].completed").value(false));

        verify(taskService, times(1)).getTasksByStatus(false);
    }


}
