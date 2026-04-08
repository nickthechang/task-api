package com.nick.taskapp.controller;

import com.nick.taskapp.model.Task;
import com.nick.taskapp.service.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }
    //someone selds POST /tasks converts into Task object method runs calls service returns created task spring->json response
    @PostMapping
    public Task createTask(@RequestBody Task task) {
        return taskService.createTask(task);
    }

    @GetMapping("/{id}")
    public Task getTaskID(@PathVariable Long id){
        return taskService.getTaskById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteTaskById(@PathVariable Long id){
        taskService.deleteTaskById(id);
    }
    //PathVariable: get data from the URL path; RequestBody: get data from the request body
    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Long id, @RequestBody Task updatedTask){
        return taskService.updateTask(id, updatedTask);
    }

}


/*
This class is the API layer. @RestController
hits app with request this handles it

separation of responsibilities with Taskservice
*/