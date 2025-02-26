# Todo Application API

A Spring Boot RESTful API for managing tasks in a todo application.

## Overview

This project is a task management API built with Spring Boot. It provides endpoints for creating, reading, updating, and deleting tasks. Each task has properties such as title, description, completion status, due date, and priority level.

## Features

- Create new tasks
- Update existing tasks
- Delete tasks
- Retrieve a specific task by ID
- Get all tasks
- Filter tasks by completion status
- Tasks are automatically sorted by priority

## Tech Stack

- Java
- Spring Boot
- Spring Data JPA
- Hibernate
- Lombok
- Jakarta Validation

## Project Structure

```
└── edu
    └── todo
        ├── controller
        │   └── TaskController.java
        ├── dto
        │   └── TaskDTO.java
        ├── model
        │   └── Task.java
        ├── repository
        │   └── TaskRepository.java
        └── service
            ├── TaskService.java
            └── TaskServiceImpl.java
```

## API Endpoints

| Method | URL                   | Description                       | Request Body | Response Body |
|--------|------------------------|-----------------------------------|--------------|---------------|
| POST   | /api/tasks             | Create a new task                 | TaskDTO      | TaskDTO       |
| PUT    | /api/tasks/{id}        | Update an existing task          | TaskDTO      | TaskDTO       |
| DELETE | /api/tasks/{id}        | Delete a task                     | -            | -             |
| GET    | /api/tasks/{id}        | Get a task by ID                  | -            | TaskDTO       |
| GET    | /api/tasks             | Get all tasks                     | -            | List<TaskDTO> |
| GET    | /api/tasks?completed=true/false | Get tasks by completion status | -      | List<TaskDTO> |

## Model Properties

### Task

| Property   | Type          | Description                                |
|------------|---------------|--------------------------------------------|
| id         | Long          | Unique identifier (auto-generated)         |
| title      | String        | Task title (required)                      |
| description| String        | Task description (max 1000 characters)     |
| completed  | Boolean       | Completion status (default: false)         |
| createdAt  | LocalDateTime | Creation timestamp (auto-generated)        |
| dueDate    | LocalDateTime | Due date (optional)                        |
| priority   | Integer       | Task priority (default: 0)                 |

## Running the Application

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

### Steps

1. Clone this repository:
   ```bash
   git clone https://github.com/yourusername/todo-application.git
   cd todo-application
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

4. The API will be available at `http://localhost:8080`

## Frontend Integration

The API is configured to allow Cross-Origin Resource Sharing (CORS) from `http://localhost:5173`, making it easy to integrate with a frontend application running on that port.

## Future Improvements

- Add user authentication and authorization
- Implement task categories/tags
- Add search functionality
- Include pagination for large task lists
- Add subtasks support

## License

This project is licensed under the MIT License - see the LICENSE file for details.
