package edu.todo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private Boolean completed;
    private LocalDateTime dueDate;
    private Integer priority;
}
