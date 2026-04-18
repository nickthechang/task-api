package com.nick.taskapp.dto;

import java.time.LocalDate;

public class TaskResponseDto {

    private Long id;
    private String title;
    private boolean completed;
    private String priority;
    private LocalDate dueDate;

    public TaskResponseDto() {
    }

    public TaskResponseDto(Long id, String title, boolean completed, String priority, LocalDate dueDate) {
        this.id = id;
        this.title = title;
        this.completed = completed;
        this.priority = priority;
        this.dueDate = dueDate;
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

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}