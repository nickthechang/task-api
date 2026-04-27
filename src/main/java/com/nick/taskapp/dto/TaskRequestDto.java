package com.nick.taskapp.dto;
import com.nick.taskapp.model.Priority;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


import java.time.LocalDate;



/*
 * Request DTO for creating or fully updating a task.
 *
 * Defines the data expected from the client and applies
 * validation rules before the request reaches the service layer.
 */
public class TaskRequestDto {
    /*
     * Title of the task.
     * Must not be blank.
     */
    @NotBlank(message = "Title is required")
    private String title;
    /*
     * Completion status of the task.
     */
    private boolean completed;
    /*
     * Completion status of the task.
     */
    @NotNull(message = "Priority is required")
    private Priority priority;
    /*
     * Due date of the task.
     * Must be provided in the request.
     */
    @NotNull(message = "Due date is required")
    private LocalDate dueDate;

    public TaskRequestDto() {
    }

    public TaskRequestDto(String title, boolean completed, Priority priority, LocalDate dueDate) {
        this.title = title;
        this.completed = completed;
        this.priority = priority;
        this.dueDate = dueDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}


/*
TaskRequestDto defines the shape of the data your API expects when a client creates or fully updates a task.
When frontend sends JSON, Spring converts that JSON into a TaskRequstDto object first. part of the API layer contract

A DTO is used to carry data between layers, especially between the client and the backend API.
In this case, TaskRequestDto defines what the client is allowed to send when creating or updating a task.

Bean Validation
This DTO uses validation annotations like @NotBlank and @NotNull so invalid requests can be rejected before business logic runs.

Request Body Binding
In the controller, Spring uses @RequestBody to turn incoming JSON into this DTO. When @Valid is present, Spring also validates it automatically before the service layer is called.


DTO represents:
“What the client must send to the API. input rules”

//DTO -> shape of data your API sends/receives.. dont expose db entity directly. control what frontendsees. easier to change backend later without breaking api
*/