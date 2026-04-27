package com.nick.taskapp.service;

import com.nick.taskapp.dto.TaskPatchDto;
import com.nick.taskapp.dto.TaskRequestDto;
import com.nick.taskapp.dto.TaskResponseDto;
import com.nick.taskapp.exception.TaskNotFoundException;
import com.nick.taskapp.mapper.TaskMapper;
import com.nick.taskapp.model.Priority;
import com.nick.taskapp.model.Task;
import com.nick.taskapp.repository.TaskRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/*
 * Unit tests for TaskService.
 *
 * Uses a mocked repository and real mapper to test service-layer
 * behavior without starting the full application or database.
 */
@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    /*
     * Mocked database access dependency.
     */
    @Mock
    private TaskRepository taskRepository;

    private TaskMapper taskMapper;
    private TaskService taskService;

    private Task task;
    private TaskRequestDto requestDto;

    @BeforeEach
    void setUp() {
        taskMapper = new TaskMapper();
        taskService = new TaskService(taskRepository, taskMapper);

        task = new Task(
                1L,
                "Learn Spring Testing",
                false,
                Priority.HIGH,
                LocalDate.of(2026, 4, 20)
        );

        requestDto = new TaskRequestDto(
                "Learn Spring Testing",
                false,
                Priority.HIGH,
                LocalDate.of(2026, 4, 20)
        );
    }

    /*
     * Verifies that a task can be created from a request DTO.
     */
    @Test
    void shouldCreateTask() {
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskResponseDto result = taskService.createTask(requestDto);

        assertNotNull(result);
        assertEquals("Learn Spring Testing", result.getTitle());
        assertEquals(Priority.HIGH, result.getPriority());

        verify(taskRepository, times(1)).save(any(Task.class));
    }

    /*
     * Verifies that all tasks are returned as response DTOs.
     */
    @Test
    void shouldGetAllTasks() {
        when(taskRepository.findAll()).thenReturn(List.of(task));

        List<TaskResponseDto> result = taskService.getAllTasks();

        assertEquals(1, result.size());
        assertEquals("Learn Spring Testing", result.get(0).getTitle());

        verify(taskRepository, times(1)).findAll();
    }

    /*
     * Verifies that a task can be found by id.
     */
    @Test
    void shouldGetTaskById() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        TaskResponseDto result = taskService.getTaskById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Learn Spring Testing", result.getTitle());

        verify(taskRepository, times(1)).findById(1L);
    }

    /*
     * Verifies that a missing task throws TaskNotFoundException.
     */
    @Test
    void shouldThrowWhenTaskNotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(99L));

        verify(taskRepository, times(1)).findById(99L);
    }

    /*
     * Verifies that a task can be fully updated.
     */
    @Test
    void shouldUpdateTask() {
        TaskRequestDto updatedDto = new TaskRequestDto(
                "Updated Title",
                true,
                Priority.LOW,
                LocalDate.of(2026, 4, 25)
        );

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TaskResponseDto result = taskService.updateTask(1L, updatedDto);

        assertEquals("Updated Title", result.getTitle());
        assertTrue(result.isCompleted());
        assertEquals(Priority.LOW, result.getPriority());
        assertEquals(LocalDate.of(2026, 4, 25), result.getDueDate());

        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(task);
    }

    /*
     * Verifies that a task can be deleted by id.
     */
    @Test
    void shouldDeleteTask() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        taskService.deleteTaskById(1L);

        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).delete(task);
    }

    /*
     * Verifies that completed tasks are returned.
     */
    @Test
    void shouldGetCompletedTasks() {
        Task completedTask = new Task(
                2L,
                "Done Task",
                true,
                Priority.MEDIUM,
                LocalDate.of(2026, 4, 21)
        );

        when(taskRepository.findByCompleted(true)).thenReturn(List.of(completedTask));

        List<TaskResponseDto> result = taskService.getCompletedTasks();

        assertEquals(1, result.size());
        assertTrue(result.get(0).isCompleted());

        verify(taskRepository, times(1)).findByCompleted(true);
    }

    /*
     * Verifies that tasks can be searched by title.
     */
    @Test
    void shouldSearchTasksByTitle() {
        Task task1 = new Task(
                1L,
                "Learn Spring Boot",
                false,
                Priority.HIGH,
                LocalDate.of(2026, 4, 20)
        );

        Task task2 = new Task(
                2L,
                "Spring Project",
                true,
                Priority.LOW,
                LocalDate.of(2026, 4, 22)
        );

        when(taskRepository.findByTitleContainingIgnoreCase("spring"))
                .thenReturn(List.of(task1, task2));

        List<TaskResponseDto> result = taskService.searchTasksByTitle("spring");

        assertEquals(2, result.size());
        assertEquals("Learn Spring Boot", result.get(0).getTitle());
        assertEquals("Spring Project", result.get(1).getTitle());

        verify(taskRepository, times(1)).findByTitleContainingIgnoreCase("spring");
    }

    /*
     * Verifies that overdue incomplete tasks are returned.
     */
    @Test
    void shouldGetOverdueTasks() {
        Task overdueTask = new Task(
                3L,
                "Late Task",
                false,
                Priority.HIGH,
                LocalDate.of(2026, 4, 1)
        );

        when(taskRepository.findByDueDateBeforeAndCompletedFalse(any(LocalDate.class)))
                .thenReturn(List.of(overdueTask));

        List<TaskResponseDto> result = taskService.getOverdueTasks();

        assertEquals(1, result.size());
        assertEquals("Late Task", result.get(0).getTitle());
        assertFalse(result.get(0).isCompleted());

        verify(taskRepository, times(1)).findByDueDateBeforeAndCompletedFalse(any(LocalDate.class));
    }

    /*
     * Verifies that tasks are returned sorted by due date.
     */
    @Test
    void shouldGetTasksSortedByDueDate() {
        Task earlierTask = new Task(
                2L,
                "First Task",
                false,
                Priority.HIGH,
                LocalDate.of(2026, 4, 20)
        );

        Task laterTask = new Task(
                1L,
                "Second Task",
                false,
                Priority.MEDIUM,
                LocalDate.of(2026, 4, 30)
        );

        when(taskRepository.findAllByOrderByDueDateAsc())
                .thenReturn(List.of(earlierTask, laterTask));

        List<TaskResponseDto> result = taskService.getTasksSortedByDueDate();

        assertEquals(2, result.size());
        assertEquals("First Task", result.get(0).getTitle());
        assertEquals(LocalDate.of(2026, 4, 20), result.get(0).getDueDate());

        verify(taskRepository, times(1)).findAllByOrderByDueDateAsc();
    }

    /*
     * Verifies that tasks can be filtered by priority.
     */
    @Test
    void shouldGetTasksByPriority() {
        when(taskRepository.findByPriority(Priority.HIGH))
                .thenReturn(List.of(task));

        List<TaskResponseDto> result = taskService.getTasksByPriority(Priority.HIGH);

        assertEquals(1, result.size());
        assertEquals(Priority.HIGH, result.get(0).getPriority());

        verify(taskRepository, times(1)).findByPriority(Priority.HIGH);
    }

    /*
     * Verifies that PATCH updates only provided fields.
     */
    @Test
    void shouldPatchTask() {
        TaskPatchDto patchDto = new TaskPatchDto();
        patchDto.setTitle("Patched Title");
        patchDto.setCompleted(true);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TaskResponseDto result = taskService.patchTask(1L, patchDto);

        assertEquals("Patched Title", result.getTitle());
        assertTrue(result.isCompleted());

        /*
         * Priority and due date were not provided,
         * so they should remain unchanged.
         */
        assertEquals(Priority.HIGH, result.getPriority());
        assertEquals(LocalDate.of(2026, 4, 20), result.getDueDate());

        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(task);
    }
}


/*
TaskServiceTest should test the business logic layer without starting the full application.
It should answer:
“Does TaskService behave correctly when repository and mapper dependencies return expected values?”

1. Unit testing
Tests one class directly: TaskService.
2. Mockito mocking
Mocks TaskRepository so you do not need a real database.
3. Service-layer testing
Checks create, read, update, delete, filtering, and exceptions.
4. Exception testing
Uses assertThrows(...) to confirm not-found behavior.
*/