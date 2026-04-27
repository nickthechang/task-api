package com.nick.taskapp.dto;

import com.nick.taskapp.model.Priority;

import java.time.LocalDate;
/*
 * Request DTO for partially updating a task.
 *
 * Only non-null fields are applied to the existing task.
 * Used by the PATCH endpoint to update selected values.
 */
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


/*
PATCH means:
update only the fields the client actually sends

TaskPatchDto defines the shape of the data your API accepts for a partial update. 
It contains only patchable task fields: title, completed, priority, and dueDate. 
Notice that completed is a Boolean, not a primitive boolean, so it can be null when the client does not send it.

This DTO exists specifically for PATCH so the API can distinguish between:

“field not sent”
“field sent with a value”


Because TaskRequestDto is for full create/full update and has required fields with validation like @NotBlank and @NotNull. 
TaskPatchDto is for partial updates, so fields must be optional. Those two purposes are different, so separate DTOs are the right design.
This is also why validation is not the same here:

create/update DTO = required fields
patch DTO = optional fields

*/