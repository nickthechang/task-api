package com.nick.taskapp.service;

import com.nick.taskapp.model.Task;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {
    //Temporary database that resets when app restarts
    private final List<Task> tasks = new ArrayList<>();
    //simulates auto-increment IDs
    private Long nextId = 1L;
    
    
    
    //just returns everything
    public List<Task> getAllTasks() {
        return tasks;
    }
    //task has no id, assign it one then increment counter store it and return
    public Task createTask(Task task) {
        task.setId(nextId);
        nextId++;
        tasks.add(task);
        return task;
    }


    public Task getTaskById(Long id){
        for(Task task:tasks){
            if(task.getId().equals(id)){
                return task;
            }
        }
        return null;
    }

    public void deleteTaskById(Long id){
        tasks.removeIf(task->task.getId().equals(id));
    }

    public Task updateTask(Long id, Task updatedTask){
        for(Task task:tasks){
            if(task.getId().equals(id)){
                task.setTitle(updatedTask.getTitle());
                task.setCompleted(updatedTask.isCompleted());
                return task;
            }
        }
        return null;
    }

}

/*
Handles business logic

*/