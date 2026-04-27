package com.nick.taskapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nick.taskapp.dto.*;
import com.nick.taskapp.exception.GlobalExceptionHandler;
import com.nick.taskapp.model.Priority;
import com.nick.taskapp.service.TaskService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/*
 * Controller tests for TaskController.
 *
 * Uses MockMvc to test HTTP endpoints without starting
 * the full application or connecting to the database.
 */
@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class TaskControllerTest {

    /*
     * MockMvc simulates HTTP requests to the controller.
     */
    @Autowired
    private MockMvc mockMvc;

    /*
     * Mocked service dependency used by the controller.
     */
    @MockitoBean
    private TaskService taskService;

    /*
     * Converts Java objects to JSON for request bodies.
     */
    @Autowired
    private ObjectMapper objectMapper;

    /*
     * Verifies paginated task retrieval.
     */
    @Test
    void shouldReturnPaginatedTasks() throws Exception {
        TaskResponseDto task = new TaskResponseDto(
                1L,
                "Test Task",
                false,
                Priority.HIGH,
                LocalDate.of(2026, 4, 20),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        PaginatedResponseDto<TaskResponseDto> page =
                new PaginatedResponseDto<>(List.of(task), 0, 10, 1, 1);

        when(taskService.getTasksPaginated(0, 10)).thenReturn(page);

        mockMvc.perform(get("/tasks")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.data[0].title").value("Test Task"))
                .andExpect(jsonPath("$.data.page").value(0))
                .andExpect(jsonPath("$.data.size").value(10))
                .andExpect(jsonPath("$.data.totalElements").value(1));
    }

    /*
     * Verifies task creation with a valid request body.
     */
    @Test
    void shouldCreateTask() throws Exception {
        TaskRequestDto request = new TaskRequestDto(
                "New Task",
                false,
                Priority.HIGH,
                LocalDate.of(2026, 4, 20)
        );

        TaskResponseDto response = new TaskResponseDto(
                1L,
                "New Task",
                false,
                Priority.HIGH,
                LocalDate.of(2026, 4, 20),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(taskService.createTask(any(TaskRequestDto.class))).thenReturn(response);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Task created"))
                .andExpect(jsonPath("$.data.title").value("New Task"))
                .andExpect(jsonPath("$.data.priority").value("HIGH"));
    }

    /*
     * Verifies validation errors for an invalid create request.
     */
    @Test
    void shouldReturnValidationErrorForInvalidCreateRequest() throws Exception {
        TaskRequestDto request = new TaskRequestDto(
                "",
                false,
                null,
                null
        );

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.data.title").value("Title is required"))
                .andExpect(jsonPath("$.data.priority").value("Priority is required"))
                .andExpect(jsonPath("$.data.dueDate").value("Due date is required"));
    }

    /*
     * Verifies retrieving a task by id.
     */
    @Test
    void shouldReturnTaskById() throws Exception {
        TaskResponseDto response = new TaskResponseDto(
                1L,
                "Test Task",
                false,
                Priority.LOW,
                LocalDate.of(2026, 4, 20),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(taskService.getTaskById(1L)).thenReturn(response);

        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.title").value("Test Task"));
    }

    /*
     * Verifies deleting a task by id.
     */
    @Test
    void shouldDeleteTask() throws Exception {
        doNothing().when(taskService).deleteTaskById(1L);

        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Task deleted"));
    }

    /*
     * Verifies full task update with PUT.
     */
    @Test
    void shouldUpdateTask() throws Exception {
        TaskRequestDto request = new TaskRequestDto(
                "Updated Task",
                true,
                Priority.MEDIUM,
                LocalDate.of(2026, 4, 25)
        );

        TaskResponseDto response = new TaskResponseDto(
                1L,
                "Updated Task",
                true,
                Priority.MEDIUM,
                LocalDate.of(2026, 4, 25),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(taskService.updateTask(eq(1L), any(TaskRequestDto.class))).thenReturn(response);

        mockMvc.perform(put("/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.title").value("Updated Task"))
                .andExpect(jsonPath("$.data.completed").value(true))
                .andExpect(jsonPath("$.data.priority").value("MEDIUM"));
    }

    /*
     * Verifies completed task retrieval.
     */
    @Test
    void shouldReturnCompletedTasks() throws Exception {
        TaskResponseDto task = new TaskResponseDto(
                1L,
                "Completed Task",
                true,
                Priority.HIGH,
                LocalDate.of(2026, 4, 20),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(taskService.getCompletedTasks()).thenReturn(List.of(task));

        mockMvc.perform(get("/tasks/completed"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].title").value("Completed Task"))
                .andExpect(jsonPath("$.data[0].completed").value(true));
    }

    /*
     * Verifies title search endpoint.
     *
     */
    @Test
    void shouldSearchTasksByTitle() throws Exception {
        TaskResponseDto task = new TaskResponseDto(
                1L,
                "Learn Spring Boot",
                false,
                Priority.HIGH,
                LocalDate.of(2026, 4, 20),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(taskService.searchTasksByTitle("spring")).thenReturn(List.of(task));

        mockMvc.perform(get("/tasks/search").param("title", "spring"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].title").value("Learn Spring Boot"));
    }

    /*
     * Verifies overdue task retrieval.
     *
     */
    @Test
    void shouldReturnOverdueTasks() throws Exception {
        TaskResponseDto task = new TaskResponseDto(
                1L,
                "Late Task",
                false,
                Priority.HIGH,
                LocalDate.of(2026, 4, 1),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(taskService.getOverdueTasks()).thenReturn(List.of(task));

        mockMvc.perform(get("/tasks/overdue"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].title").value("Late Task"))
                .andExpect(jsonPath("$.data[0].completed").value(false));
    }

    /*
     * Verifies tasks sorted by due date.
     *
     */
    @Test
    void shouldReturnTasksSortedByDueDate() throws Exception {
        TaskResponseDto task = new TaskResponseDto(
                1L,
                "First Task",
                false,
                Priority.HIGH,
                LocalDate.of(2026, 4, 20),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(taskService.getTasksSortedByDueDate()).thenReturn(List.of(task));

        mockMvc.perform(get("/tasks/sorted-by-due-date"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].title").value("First Task"));
    }

    /*
     * Verifies priority filter endpoint.
     *
     */
    @Test
    void shouldReturnTasksByPriority() throws Exception {
        TaskResponseDto task = new TaskResponseDto(
                1L,
                "Important Task",
                false,
                Priority.HIGH,
                LocalDate.of(2026, 4, 20),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(taskService.getTasksByPriority(Priority.HIGH)).thenReturn(List.of(task));

        mockMvc.perform(get("/tasks/priority").param("level", "HIGH"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].priority").value("HIGH"));
    }

    /*
     * Verifies advanced filtering with pagination and sorting.
     */
    @Test
    void shouldReturnAdvancedFilteredTasks() throws Exception {
        TaskResponseDto task = new TaskResponseDto(
                1L,
                "Advanced Task",
                false,
                Priority.HIGH,
                LocalDate.of(2026, 4, 20),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        PaginatedResponseDto<TaskResponseDto> page =
                new PaginatedResponseDto<>(List.of(task), 0, 10, 1, 1);

        when(taskService.getTasksAdvanced(
                eq(0),
                eq(10),
                eq("advanced"),
                eq(Priority.HIGH),
                eq(false),
                eq("dueDate"),
                eq("asc")
        )).thenReturn(page);

        mockMvc.perform(get("/tasks/advanced")
                        .param("page", "0")
                        .param("size", "10")
                        .param("title", "advanced")
                        .param("priority", "HIGH")
                        .param("completed", "false")
                        .param("sortBy", "dueDate")
                        .param("direction", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.data[0].title").value("Advanced Task"))
                .andExpect(jsonPath("$.data.totalElements").value(1));
    }

    /*
     * Verifies partial task update with PATCH.
     */
    @Test
    void shouldPatchTask() throws Exception {
        TaskPatchDto patchDto = new TaskPatchDto();
        patchDto.setTitle("Patched Task");
        patchDto.setCompleted(true);

        TaskResponseDto response = new TaskResponseDto(
                1L,
                "Patched Task",
                true,
                Priority.HIGH,
                LocalDate.of(2026, 4, 20),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(taskService.patchTask(eq(1L), any(TaskPatchDto.class))).thenReturn(response);

        mockMvc.perform(patch("/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Task patched"))
                .andExpect(jsonPath("$.data.title").value("Patched Task"))
                .andExpect(jsonPath("$.data.completed").value(true));
    }
}

/*
TaskControllerTest should test the HTTP/controller layer.
It checks things like:
does POST /tasks return the right status?
does validation work?
does JSON response shape look right?
does the controller call the service?
It does not test the real database.
1. Web layer testing
Uses MockMvc to fake HTTP requests without running the full server.
2. Mocked service
TaskService should be mocked so you test only the controller.
3. JSON response testing
Uses jsonPath(...) to check response fields.
4. Security-aware controller testing
Since /tasks/** is protected, controller tests need mock authentication or security disabled for the test.
*/