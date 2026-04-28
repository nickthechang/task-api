package com.nick.taskapp.service;
import java.time.LocalDate;
import java.util.List;
//for logging to see what broke
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
// import org.springframework.web.bind.annotation.PathVariable;
import com.nick.taskapp.dto.PaginatedResponseDto;
import com.nick.taskapp.dto.TaskPatchDto;
import com.nick.taskapp.dto.TaskRequestDto;
import com.nick.taskapp.dto.TaskResponseDto;
import com.nick.taskapp.exception.TaskNotFoundException;
import com.nick.taskapp.mapper.TaskMapper;
import com.nick.taskapp.model.Priority;
import com.nick.taskapp.model.Task;
import com.nick.taskapp.repository.TaskRepository;
import com.nick.taskapp.specification.TaskSpecification;

/*
 * Handles business logic for task operations.
 *
 * Coordinates repositories, mapping, filtering, pagination,
 * and exception handling for the task domain.
 */
@Service
public class TaskService {
    /*
     * Logger used to record task operations and failures.
     */
    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);
    /*
     * Repository used for database access.
     */
    private final TaskRepository taskRepository;
    /*
     * Mapper used to convert between entities and DTOs.
     */
    private final TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    //DTO
    /*
     * Returns all tasks mapped to response DTOs.
     */
    public List<TaskResponseDto> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(taskMapper::toResponseDto)
                .toList();
    }

    /*
     * Creates a new task from the request DTO and returns the saved result.
     */
    public TaskResponseDto createTask(TaskRequestDto dto) {
        logger.info("Creating task with title: {}", dto.getTitle());
        Task savedTask = taskRepository.save(taskMapper.toEntity(dto)); // using mapper
        logger.info("Task created with id: {}", savedTask.getId());
        return taskMapper.toResponseDto(savedTask); // FIXED
        // return mapToResponseDto(savedTask); // OLD
    }

    /*
     * Returns a task by id.
     * Throws TaskNotFoundException if the task does not exist.
     */
    public TaskResponseDto getTaskById(Long id) {
        logger.info("Fetching task with id: {}", id);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Task not found with id: {}", id);
                    return new TaskNotFoundException("Task not found");
                });
        return taskMapper.toResponseDto(task);
    }

    /*
     * Updates all fields of an existing task.
     * Throws TaskNotFoundException if the task does not exist.
     */
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
        return taskMapper.toResponseDto(savedTask);
    }

    /*
     * Deletes a task by id.
     * Throws TaskNotFoundException if the task does not exist.
     */
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


    public List<TaskResponseDto> getCompletedTasks() {
        return taskRepository.findByCompleted(true)
                .stream()
                .map(taskMapper::toResponseDto)
                .toList();
    }

    public List<TaskResponseDto> searchTasksByTitle(String title) {
        return taskRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(taskMapper::toResponseDto)
                .toList();
    }

    public List<TaskResponseDto> getOverdueTasks() {
        return taskRepository.findByDueDateBeforeAndCompletedFalse(LocalDate.now())
                .stream()
                .map(taskMapper::toResponseDto)
                .toList();
    }

    public List<TaskResponseDto> getTasksSortedByDueDate() {
        return taskRepository.findAllByOrderByDueDateAsc()
                .stream()
                .map(taskMapper::toResponseDto)
                .toList();
    }

    public List<TaskResponseDto> getTasksByPriority(Priority priority) {
        return taskRepository.findByPriorityIgnoreCase(priority)
                .stream()
                .map(taskMapper::toResponseDto)
                .toList();
    }

    /*
     * Returns tasks in paginated form.
     */
    public PaginatedResponseDto<TaskResponseDto> getTasksPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Task> taskPage = taskRepository.findAll(pageable);

        List<TaskResponseDto> data = taskPage
                .getContent()
                .stream()
                .map(taskMapper::toResponseDto)
                .toList();

        return new PaginatedResponseDto<>(
                data,
                taskPage.getNumber(),
                taskPage.getSize(),
                taskPage.getTotalElements(),
                taskPage.getTotalPages()
        );
    }

    /*
     * Returns tasks using dynamic filtering, sorting, and pagination.
     */
    public PaginatedResponseDto<TaskResponseDto> getTasksAdvanced(
            int page,
            int size,
            String title,
            Priority priority,
            Boolean completed,
            String sortBy,
            String direction
    ) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Task> spec = Specification
                .where(TaskSpecification.hasTitle(title))
                .and(TaskSpecification.hasPriority(priority))
                .and(TaskSpecification.isCompleted(completed));

        Page<Task> taskPage = taskRepository.findAll(spec, pageable);

        List<TaskResponseDto> data = taskPage.getContent()
                .stream()
                .map(taskMapper::toResponseDto)
                .toList();

        return new PaginatedResponseDto<>(
                data,
                taskPage.getNumber(),
                taskPage.getSize(),
                taskPage.getTotalElements(),
                taskPage.getTotalPages()
        );
    }

    /*
     * Applies a partial update to an existing task.
     * Only non-null fields in the patch DTO are updated.
     */
    public TaskResponseDto patchTask(Long id, TaskPatchDto patchDto) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        if (patchDto.getTitle() != null) {
            existingTask.setTitle(patchDto.getTitle());
        }

        if (patchDto.getCompleted() != null) {
            existingTask.setCompleted(patchDto.getCompleted());
        }

        if (patchDto.getPriority() != null) {
            existingTask.setPriority(patchDto.getPriority());
        }

        if (patchDto.getDueDate() != null) {
            existingTask.setDueDate(patchDto.getDueDate());
        }

        Task savedTask = taskRepository.save(existingTask);
        return taskMapper.toResponseDto(savedTask);
    }

}

/*
TaskService is the business logic layer for tasks.
This is the file that decides:
how tasks are created
how tasks are updated
what happens if a task is missing
how lists are filtered, paginated, sorted, and patched
when repository methods should be called
when mapping should happen
when logs should be written

1. Service layer pattern
Moves business logic out of the controller and keeps the controller focused on HTTP concerns.
2. Dependency injection
TaskRepository and TaskMapper are injected through the constructor, so Spring provides the dependencies automatically.
3. Logging
The service uses Logger / LoggerFactory to record important operations and errors.
4. Exception-driven flow
When a task is not found, the service throws TaskNotFoundException, which is later handled by the global exception handler.
5. DTO mapping
The service uses TaskMapper to convert:
request DTO → entity
entity → response DTO.
6. Pagination
Uses PageRequest, Pageable, and Page<Task> to return paginated results.
7. Dynamic filtering with specifications
Uses TaskSpecification with JpaSpecificationExecutor to build advanced filters dynamically.
8. Partial update logic
Uses null checks on TaskPatchDto fields so only provided fields are updated.
*/