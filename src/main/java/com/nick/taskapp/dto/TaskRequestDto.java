package com.nick.taskapp.dto;
import com.nick.taskapp.model.Priority;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;


//DTO -> shape of data your API sends/receives.. dont expose db entity directly. control what frontendsees. easier to change backend later without breaking api


public class TaskRequestDto {

    @NotBlank(message = "Title is required")
    private String title;

    private boolean completed;

    private Priority priority;

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