package com.nick.taskapp.mapper;

import com.nick.taskapp.dto.TaskRequestDto;
import com.nick.taskapp.dto.TaskResponseDto;
import com.nick.taskapp.model.Task;
import org.springframework.stereotype.Component;

/*
 * Maps Task entities to DTOs and DTOs to Task entities.
 *
 * Keeps conversion logic in one place so the service layer
 * can focus on business logic instead of object mapping.
 */
@Component
public class TaskMapper {
    /*
     * Converts a Task entity into a response DTO.
     */
    public TaskResponseDto toResponseDto(Task task) {
        return new TaskResponseDto(
                task.getId(),
                task.getTitle(),
                task.isCompleted(),
                task.getPriority(),
                task.getDueDate(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }
    /*
     * Converts a request DTO into a Task entity.
     */
    public Task toEntity(TaskRequestDto dto) {
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setCompleted(dto.isCompleted());
        task.setPriority(dto.getPriority());
        task.setDueDate(dto.getDueDate());
        return task;
    }
}

/*
TaskMapper converts between your API layer objects and your database layer objects.
In your app, it handles:
TaskRequestDto → Task
Task → TaskResponseDto
So this file exists to keep conversion logic out of the controller and service.


Mapper pattern
A dedicated class handles object-to-object conversion instead of mixing that logic into controllers or services.

Separation of concerns
The DTO layer and entity layer stay separate. The mapper is the bridge between them.
*/