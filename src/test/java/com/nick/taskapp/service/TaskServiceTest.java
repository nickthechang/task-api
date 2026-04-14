package com.nick.taskapp.service;

import com.nick.taskapp.exception.TaskNotFoundException;
import com.nick.taskapp.model.Task;
import com.nick.taskapp.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task(1L, "Learn Spring Testing", false, "HIGH");
    }

    @Test
    void shouldCreateTask() {
        when(taskRepository.save(task)).thenReturn(task);

        Task savedTask = taskService.createTask(task);

        assertNotNull(savedTask);
        assertEquals("Learn Spring Testing", savedTask.getTitle());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void shouldGetAllTasks() {
        when(taskRepository.findAll()).thenReturn(List.of(task));

        List<Task> tasks = taskService.getAllTasks();

        assertEquals(1, tasks.size());
        assertEquals("Learn Spring Testing", tasks.get(0).getTitle());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void shouldGetTaskById() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task foundTask = taskService.getTaskById(1L);

        assertNotNull(foundTask);
        assertEquals(1L, foundTask.getId());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowWhenTaskNotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(99L));

        verify(taskRepository, times(1)).findById(99L);
    }

    @Test
    void shouldUpdateTask() {
        Task updatedTask = new Task(null, "Updated Title", true, "LOW");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Task result = taskService.updateTask(1L, updatedTask);

        assertEquals("Updated Title", result.getTitle());
        assertTrue(result.isCompleted());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void shouldDeleteTask() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        doNothing().when(taskRepository).delete(task);

        taskService.deleteTaskById(1L);

        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).delete(task);
    }

    @Test
    void shouldGetCompletedTasks() {
        Task completedTask = new Task(1L, "Done Task", true, "HIGH");
        when(taskRepository.findByCompleted(true)).thenReturn(List.of(completedTask));

        List<Task> result = taskService.getCompletedTasks();

        assertEquals(1, result.size());
        assertTrue(result.get(0).isCompleted());
        verify(taskRepository, times(1)).findByCompleted(true);
    }


    @Test
    void shouldSearchTasksByTitle() {
        Task task1 = new Task(1L, "Learn Spring Boot", false, "HIGH");
        Task task2 = new Task(2L, "Spring project", true, "LOW");

        when(taskRepository.findByTitleContainingIgnoreCase("spring"))
                .thenReturn(List.of(task1, task2));

        List<Task> result = taskService.searchTasksByTitle("spring");

        assertEquals(2, result.size());
        assertEquals("Learn Spring Boot", result.get(0).getTitle());
        assertEquals("Spring project", result.get(1).getTitle());
        verify(taskRepository, times(1)).findByTitleContainingIgnoreCase("spring");
    }

    @Test
    void shouldUpdateTaskPriority() {
        Task existingTask = new Task(1L, "Old Title", false, "LOW");
        Task updatedTask = new Task(null, "New Title", true, "HIGH");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Task result = taskService.updateTask(1L, updatedTask);

        assertEquals("HIGH", result.getPriority());
        assertEquals("New Title", result.getTitle());
        assertTrue(result.isCompleted());
    }
}