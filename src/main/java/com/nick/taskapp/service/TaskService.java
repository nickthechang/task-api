package com.nick.taskapp.service;

import com.nick.taskapp.exception.TaskNotFoundException;
import com.nick.taskapp.model.Task;
import com.nick.taskapp.repository.TaskRepository;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {

    //Temporary database that resets when app restarts
    //private final List<Task> tasks = new ArrayList<>();
    //simulates auto-increment IDs
    //private Long nextId = 1L;

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
    
    //just returns everything
    public List<Task> getAllTasks() {
        //return tasks;

        return taskRepository.findAll();
    }
    //task has no id, assign it one then increment counter store it and return
    public Task createTask(Task task) {
        // task.setId(nextId);
        // nextId++;
        // tasks.add(task);
        // return task;

        return taskRepository.save(task);
    }


    public Task getTaskById(Long id){
        // for(Task task:tasks){
        //     if(task.getId().equals(id)){
        //         return task;
        //     }
        // }
        // //throws error to controller; this is too redundant using TaskNotFoundException isntead
        // //throw new RuntimeException("Task not found");

        // throw new TaskNotFoundException("Task not found");

        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

    }

    public void deleteTaskById(Long id){
        // tasks.removeIf(task->task.getId().equals(id));
        Task task = getTaskById(id);
        taskRepository.delete(task);
    }

    public Task updateTask(Long id, Task updatedTask){
        // for(Task task:tasks){
        //     if(task.getId().equals(id)){
        //         task.setTitle(updatedTask.getTitle());
        //         task.setCompleted(updatedTask.isCompleted());
        //         return task;
        //     }
        // }
        // //throw new RuntimeException("Task not found");
        // throw new TaskNotFoundException("Task not found");
        Task existingTask = getTaskById(id);
        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setCompleted(updatedTask.isCompleted());
        return taskRepository.save(existingTask);
    }
        
    public List<Task> getCompletedTasks() {
        return taskRepository.findByCompleted(true);
    }

}

/*
Handles business logic

database stores tasks now
JPA handles IDs
repository talks to DB for you.

*/