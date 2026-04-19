package com.nick.taskapp.service;

import java.time.LocalDate;
// import java.util.ArrayList;
import java.util.List;

//for logging to see what broke
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
// import org.springframework.web.bind.annotation.PathVariable;

import com.nick.taskapp.dto.PaginatedResponseDto;
import com.nick.taskapp.dto.TaskRequestDto;
import com.nick.taskapp.dto.TaskResponseDto;
import com.nick.taskapp.exception.TaskNotFoundException;
import com.nick.taskapp.model.Task;
import com.nick.taskapp.repository.TaskRepository;
import com.nick.taskapp.model.Priority;
@Service
public class TaskService {

    //Temporary database that resets when app restarts
    //private final List<Task> tasks = new ArrayList<>();
    //simulates auto-increment IDs
    //private Long nextId = 1L;
    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
    
    //just returns everything
    // public List<Task> getAllTasks() {
    //     //return tasks;

    //     return taskRepository.findAll();
    // }
    //task has no id, assign it one then increment counter store it and return
    // public Task createTask(Task task) {
    //     // task.setId(nextId);
    //     // nextId++;
    //     // tasks.add(task);
    //     // return task;

    //     return taskRepository.save(task);
    // }


    // public Task getTaskById(Long id){
    //     // for(Task task:tasks){
    //     //     if(task.getId().equals(id)){
    //     //         return task;
    //     //     }
    //     // }
    //     // //throws error to controller; this is too redundant using TaskNotFoundException isntead
    //     // //throw new RuntimeException("Task not found");

    //     // throw new TaskNotFoundException("Task not found");

    //     return taskRepository.findById(id)
    //             .orElseThrow(() -> new TaskNotFoundException("Task not found"));

    // }

    // public void deleteTaskById(Long id){
    //     // tasks.removeIf(task->task.getId().equals(id));
    //     Task task = getTaskById(id);
    //     taskRepository.delete(task);
    // }

    // public Task updateTask(Long id, Task updatedTask){
    //     // for(Task task:tasks){
    //     //     if(task.getId().equals(id)){
    //     //         task.setTitle(updatedTask.getTitle());
    //     //         task.setCompleted(updatedTask.isCompleted());
    //     //         return task;
    //     //     }
    //     // }
    //     // //throw new RuntimeException("Task not found");
    //     // throw new TaskNotFoundException("Task not found");
    //     Task existingTask = getTaskById(id);
    //     existingTask.setTitle(updatedTask.getTitle());
    //     existingTask.setCompleted(updatedTask.isCompleted());
    //     existingTask.setPriority(updatedTask.getPriority());
    //     existingTask.setDueDate(updatedTask.getDueDate());
    //     return taskRepository.save(existingTask);
    // }


    //DTO
    public List<TaskResponseDto> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }
    public TaskResponseDto createTask(TaskRequestDto dto) {
        logger.info("Creating task with title: {}", dto.getTitle());
        Task savedTask = taskRepository.save(mapToEntity(dto));
        logger.info("Task created with id: {}", savedTask.getId());
        return mapToResponseDto(savedTask);
    }
    public TaskResponseDto getTaskById(Long id) {
        logger.info("Fetching task with id: {}", id);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Task not found with id: {}", id);
                    return new TaskNotFoundException("Task not found");
                });
        return mapToResponseDto(task);
    }
    public TaskResponseDto updateTask(Long id, TaskRequestDto updatedTask) {
        logger.info("Updating task with id: {}", id);
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Task not found for update: {}", id);
                    return new TaskNotFoundException("Task not found");
                });
        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setCompleted(updatedTask.isCompleted());
        existingTask.setPriority(updatedTask.getPriority());
        existingTask.setDueDate(updatedTask.getDueDate());
        Task savedTask = taskRepository.save(existingTask);
        logger.info("Task updated with id: {}", id);
        return mapToResponseDto(savedTask);
    }
    public void deleteTaskById(Long id) {
        logger.info("Deleting task with id: {}", id);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Task not found for deletion: {}", id);
                    return new TaskNotFoundException("Task not found");
                });
        taskRepository.delete(task);
        logger.info("Task deleted with id: {}", id);
    }


        
    // public List<Task> getCompletedTasks() {
    //     return taskRepository.findByCompleted(true);
    // }

    // public List<Task> searchTasksByTitle(String title) {
    //     return taskRepository.findByTitleContainingIgnoreCase(title);
    // }

    // public List<Task> getOverdueTasks() {
    //     return taskRepository.findByDueDateBeforeAndCompletedFalse(LocalDate.now());
    // }

    // public List<Task> getTasksSortedByDueDate() {
    //     return taskRepository.findAllByOrderByDueDateAsc();
    // }

    // public List<Task> getTasksByPriority(String priority) {
    //     return taskRepository.findByPriorityIgnoreCase(priority);
    // }    

    // public Page<Task> getTasksPaginated(int page, int size) {
    //     Pageable pageable = PageRequest.of(page, size);
    //     return taskRepository.findAll(pageable);
    // }



    public List<TaskResponseDto> getCompletedTasks() {
        return taskRepository.findByCompleted(true)
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    public List<TaskResponseDto> searchTasksByTitle(String title) {
        return taskRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    public List<TaskResponseDto> getOverdueTasks() {
        return taskRepository.findByDueDateBeforeAndCompletedFalse(LocalDate.now())
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    public List<TaskResponseDto> getTasksSortedByDueDate() {
        return taskRepository.findAllByOrderByDueDateAsc()
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    public List<TaskResponseDto> getTasksByPriority(Priority priority) {
        return taskRepository.findByPriorityIgnoreCase(priority)
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    // public Page<TaskResponseDto> getTasksPaginated(int page, int size) {
    //     Pageable pageable = PageRequest.of(page, size);
    //     return taskRepository.findAll(pageable)
    //             .map(this::mapToResponseDto);
    // }

    public PaginatedResponseDto<TaskResponseDto> getTasksPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Task> taskPage = taskRepository.findAll(pageable);

        List<TaskResponseDto> data = taskPage
                .getContent()
                .stream()
                .map(this::mapToResponseDto)
                .toList();

        return new PaginatedResponseDto<>(
                data,
                taskPage.getNumber(),
                taskPage.getSize(),
                taskPage.getTotalElements(),
                taskPage.getTotalPages()
        );
    }


    //DTO helper methods
    private TaskResponseDto mapToResponseDto(Task task) {
        return new TaskResponseDto(
                task.getId(),
                task.getTitle(),
                task.isCompleted(),
                task.getPriority(),
                task.getDueDate()
        );
    }

    private Task mapToEntity(TaskRequestDto dto) {
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setCompleted(dto.isCompleted());
        task.setPriority(dto.getPriority());
        task.setDueDate(dto.getDueDate());
        return task;
    }

}

/*
Handles business logic

database stores tasks now
JPA handles IDs
repository talks to DB for you.

*/