package com.nick.taskapp.dto;

import com.nick.taskapp.model.Priority;

import java.time.LocalDate;

public class TaskPatchDto {

    private String title;
    private Boolean completed;
    private Priority priority;
    private LocalDate dueDate;

    public TaskPatchDto() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
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