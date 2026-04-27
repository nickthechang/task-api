package com.nick.taskapp.controller;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import com.nick.taskapp.dto.*;
import com.nick.taskapp.model.Priority;
import com.nick.taskapp.service.TaskService;

import jakarta.validation.Valid;

/*
 * REST controller for task-related API endpoints.
 *
 * Handles HTTP requests, validates incoming DTOs,
 * delegates task operations to the service layer,
 * and returns standardized API responses.
 */
@RestController
//All endpoints start with /tasks
@RequestMapping("/tasks")
public class TaskController {
    //DI: need TaskService injects for you.
    private final TaskService taskService;
    /*
     * Constructor injection allows Spring to provide the service dependency.
     */
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /*
     * Returns a paginated list of tasks.
     *
     * Example: GET /tasks?page=0&size=10
     */
    @GetMapping
    public ApiResponse<PaginatedResponseDto<TaskResponseDto>> getAllTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(taskService.getTasksPaginated(page, size));
    }

    /*
     * Creates a new task from the request body.
     *
     * @Valid triggers validation rules defined in TaskRequestDto.
     */
    @PostMapping
    public ApiResponse<TaskResponseDto> createTask(@Valid @RequestBody TaskRequestDto task) {
        return ApiResponse.success("Task created", taskService.createTask(task));
    }

    /*
     * Returns a single task by id.
     *
     * Example: GET /tasks/1
     */
    @GetMapping("/{id}")
    public ApiResponse<TaskResponseDto> getTaskById(@PathVariable Long id){
        return ApiResponse.success(taskService.getTaskById(id));
    }

    /*
     * Deletes a task by id.
     *
     * Returns a success message after deletion.
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteTaskById(@PathVariable Long id){
        taskService.deleteTaskById(id);
        return ApiResponse.success("Task deleted", null);
    }


    /*
     * Fully updates an existing task.
     *
     * PUT expects the full task request body.
     */
    @PutMapping("/{id}")
    public ApiResponse<TaskResponseDto> updateTask(@PathVariable Long id, @Valid @RequestBody TaskRequestDto updatedTask){
        return ApiResponse.success(taskService.updateTask(id, updatedTask));
    }

    @GetMapping("/completed")
    public ApiResponse<List<TaskResponseDto>> getCompletedTasks() {
        return ApiResponse.success(taskService.getCompletedTasks());
    }

    @GetMapping("/search")
    public ApiResponse<List<TaskResponseDto>> searchTasksByTitle(@RequestParam String title) {
        return ApiResponse.success(taskService.searchTasksByTitle(title));
    }

    @GetMapping("/overdue")
    public ApiResponse<List<TaskResponseDto>> getOverdueTasks() {
        return ApiResponse.success(taskService.getOverdueTasks());
    }

    @GetMapping("/sorted-by-due-date")
    public ApiResponse<List<TaskResponseDto>> getTasksSortedByDueDate() {
        return ApiResponse.success(taskService.getTasksSortedByDueDate());
    }

    @GetMapping("/priority")
    public ApiResponse<List<TaskResponseDto>> getTasksByPriority(@RequestParam Priority level) {
        return ApiResponse.success(taskService.getTasksByPriority(level));
    }

    /*
     * Returns tasks using optional filters, pagination, and sorting.
     *
     * Example:
     * GET /tasks/advanced?page=0&size=10&title=study&priority=HIGH&completed=false&sortBy=dueDate&direction=asc
     */
    @GetMapping("/advanced")
    public ApiResponse<PaginatedResponseDto<TaskResponseDto>> getTasksAdvanced(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Priority priority,
            @RequestParam(required = false) Boolean completed,
            @RequestParam(defaultValue = "dueDate") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        return ApiResponse.success(
                taskService.getTasksAdvanced(page, size, title, priority, completed, sortBy, direction)
        );
    }

    /*
     * Partially updates an existing task.
     *
     * Only non-null fields in TaskPatchDto are applied.
     */
    @PatchMapping("/{id}")
    public ApiResponse<TaskResponseDto> patchTask(
            @PathVariable Long id,
            @RequestBody TaskPatchDto patchDto) {
        return ApiResponse.success("Task patched", taskService.patchTask(id, patchDto));
    }
}


/*
The controller receives requests, pulls out request data, calls TaskService, and returns API responses.
It should not contain business logic or database logic.

TaskController is the HTTP/API layer for tasks.
Its job is to expose task features as endpoints like:
GET /tasks
POST /tasks
GET /tasks/{id}
PUT /tasks/{id}
PATCH /tasks/{id}
DELETE /tasks/{id}


1. REST Controller
@RestController
Tells Spring this class handles HTTP requests and returns response bodies as JSON.
2. Base Route Mapping
@RequestMapping("/tasks")
All endpoints in this controller start with /tasks.
3. HTTP Method Mapping
Uses:
@GetMapping
@PostMapping
@PutMapping
@PatchMapping
@DeleteMapping
These connect Java methods to HTTP actions.
4. Request Body Binding
@RequestBody
Tells Spring to convert incoming JSON into a Java DTO.
5. Validation Trigger
@Valid
Tells Spring to validate the DTO before the service runs.
6. Path Variables
@PathVariable Long id
Gets values from the URL path, like /tasks/5.
7. Query Parameters
@RequestParam
Gets values from the URL query string, like /tasks?page=0&size=10.
8. Response Wrapper
Uses ApiResponse<T> to return consistent JSON response shapes.
*/