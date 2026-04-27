package com.nick.taskapp.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import com.nick.taskapp.model.Priority;
/*
 * Response DTO for sending task data to the client.
 *
 * Defines the structure of the API response and controls
 * what fields are exposed externally.
 */
public class TaskResponseDto {

    private Long id;
    private String title;
    private boolean completed;
    private Priority priority;
    private LocalDate dueDate;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public TaskResponseDto() {
    }

    public TaskResponseDto(Long id, String title, boolean completed, Priority priority, LocalDate dueDate, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.completed = completed;
        this.priority = priority;
        this.dueDate = dueDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

/*
TaskResponseDto defines what your API sends back to the client.

Response DTO
Controls exactly what data is exposed to the client.

Separation of Concerns
Entity = database structure
Response DTO = API output structure

Data Shaping
Lets you decide:
what fields to include
what format to return


Why:
1. Control what the client sees
2. Prevent breaking changes when database changes
3. Security. Doenst expose passwords or ID's


TaskResponseDto is the final shape of your API response — not your database model.
*/