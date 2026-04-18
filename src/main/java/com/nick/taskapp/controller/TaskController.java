package com.nick.taskapp.controller;

// import com.nick.taskapp.model.Task;
import com.nick.taskapp.service.TaskService;
import com.nick.taskapp.dto.*;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;
// import org.springframework.web.server.ResponseStatusException;
// import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Page;

import java.util.List;

//for frontend
@CrossOrigin(origins = "http://localhost:4200")
//API layer
@RestController
//All endpoints start with /tasks
@RequestMapping("/tasks")
public class TaskController {
    //DI: need TaskService injects for you.
    private final TaskService taskService;
    //constructor just shows how dependencies are injected
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }
    //request hits controller this method runs calls service returns list and spring converts to json
    // @GetMapping
    // public List<Task> getAllTasks() {
    //     return taskService.getAllTasks();
    // }

    //paganation - return data in chunks instead of everything at once
    //for performance, scalability, frontendux
    //TaskResponseDto was just Task
    // @GetMapping
    // public Page<TaskResponseDto> getAllTasks(
    //         @RequestParam(defaultValue = "0") int page,
    //         @RequestParam(defaultValue = "10") int size) {
    //     return taskService.getTasksPaginated(page, size);
    // }
    
    //clean pagination response more frontend friendly
    @GetMapping
    public PaginatedResponseDto<TaskResponseDto> getAllTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return taskService.getTasksPaginated(page, size);
    }




    //someone selds POST /tasks converts into Task object method runs calls service returns created task spring->json response
    @PostMapping
    //@valid since request body is required
    public TaskResponseDto createTask(@Valid @RequestBody TaskRequestDto task) {
        return taskService.createTask(task);
    }

    @GetMapping("/{id}")
    public TaskResponseDto getTaskById(@PathVariable Long id){
        //global handler instead of this
        // try{
        //     return taskService.getTaskById(id);
        // } catch (RuntimeException e){
        //     throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        // }

        //global handler catches it automatically
        return taskService.getTaskById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteTaskById(@PathVariable Long id){
        taskService.deleteTaskById(id);
    }


    //PathVariable: get data from the URL path; RequestBody: get data from the request body
    @PutMapping("/{id}")
    public TaskResponseDto updateTask(@PathVariable Long id, @Valid @RequestBody TaskRequestDto updatedTask){
        // try{
        //     return taskService.updateTask(id, updatedTask);
        // } catch (RuntimeException e){
        //     throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        // }
        return taskService.updateTask(id, updatedTask);
    }

    @GetMapping("/completed")
    public List<TaskResponseDto> getCompletedTasks() {
        return taskService.getCompletedTasks();
    }

    @GetMapping("/search")
    public List<TaskResponseDto> searchTasksByTitle(@RequestParam String title) {
        return taskService.searchTasksByTitle(title);
    }

    @GetMapping("/overdue")
    public List<TaskResponseDto> getOverdueTasks() {
        return taskService.getOverdueTasks();
    }

    @GetMapping("/sorted-by-due-date")
    public List<TaskResponseDto> getTasksSortedByDueDate() {
        return taskService.getTasksSortedByDueDate();
    }

    @GetMapping("/priority")
    public List<TaskResponseDto> getTasksByPriority(@RequestParam String level) {
        return taskService.getTasksByPriority(level);
    }

    

}


/*
This class is the API layer. @RestController
hits app with request this handles it

separation of responsibilities with Taskservice
*/