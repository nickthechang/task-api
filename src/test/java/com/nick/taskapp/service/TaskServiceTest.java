// package com.nick.taskapp.service;

// import com.nick.taskapp.exception.TaskNotFoundException;
// import com.nick.taskapp.model.Task;
// import com.nick.taskapp.repository.TaskRepository;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;

// import java.time.LocalDate;
// import java.util.List;
// import java.util.Optional;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.*;

// @ExtendWith(MockitoExtension.class)
// class TaskServiceTest {

//     @Mock
//     private TaskRepository taskRepository;

//     @InjectMocks
//     private TaskService taskService;

//     private Task task;

//     @BeforeEach
//     void setUp() {
//         task = new Task(1L, "Learn Spring Testing", false, "HIGH", LocalDate.of(2026, 4, 20));
//     }

//     @Test
//     void shouldCreateTask() {
//         when(taskRepository.save(task)).thenReturn(task);

//         Task savedTask = taskService.createTask(task);

//         assertNotNull(savedTask);
//         assertEquals("Learn Spring Testing", savedTask.getTitle());
//         verify(taskRepository, times(1)).save(task);
//     }

//     @Test
//     void shouldGetAllTasks() {
//         when(taskRepository.findAll()).thenReturn(List.of(task));

//         List<Task> tasks = taskService.getAllTasks();

//         assertEquals(1, tasks.size());
//         assertEquals("Learn Spring Testing", tasks.get(0).getTitle());
//         verify(taskRepository, times(1)).findAll();
//     }

//     @Test
//     void shouldGetTaskById() {
//         when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

//         Task foundTask = taskService.getTaskById(1L);

//         assertNotNull(foundTask);
//         assertEquals(1L, foundTask.getId());
//         verify(taskRepository, times(1)).findById(1L);
//     }

//     @Test
//     void shouldThrowWhenTaskNotFound() {
//         when(taskRepository.findById(99L)).thenReturn(Optional.empty());

//         assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(99L));

//         verify(taskRepository, times(1)).findById(99L);
//     }

//     @Test
//     void shouldUpdateTask() {
//         Task updatedTask = new Task(null, "Updated Title", true, "LOW", LocalDate.of(2026, 4, 20));

//         when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
//         when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

//         Task result = taskService.updateTask(1L, updatedTask);

//         assertEquals("Updated Title", result.getTitle());
//         assertTrue(result.isCompleted());
//         verify(taskRepository, times(1)).findById(1L);
//         verify(taskRepository, times(1)).save(task);
//     }

//     @Test
//     void shouldDeleteTask() {
//         when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
//         doNothing().when(taskRepository).delete(task);

//         taskService.deleteTaskById(1L);

//         verify(taskRepository, times(1)).findById(1L);
//         verify(taskRepository, times(1)).delete(task);
//     }

//     @Test
//     void shouldGetCompletedTasks() {
//         Task completedTask = new Task(1L, "Done Task", true, "HIGH", LocalDate.of(2026, 4, 20));
//         when(taskRepository.findByCompleted(true)).thenReturn(List.of(completedTask));

//         List<Task> result = taskService.getCompletedTasks();

//         assertEquals(1, result.size());
//         assertTrue(result.get(0).isCompleted());
//         verify(taskRepository, times(1)).findByCompleted(true);
//     }


//     @Test
//     void shouldSearchTasksByTitle() {
//         Task task1 = new Task(1L, "Learn Spring Boot", false, "HIGH", LocalDate.of(2026, 4, 20));
//         Task task2 = new Task(2L, "Spring project", true, "LOW", LocalDate.of(2026, 4, 20));

//         when(taskRepository.findByTitleContainingIgnoreCase("spring"))
//                 .thenReturn(List.of(task1, task2));

//         List<Task> result = taskService.searchTasksByTitle("spring");

//         assertEquals(2, result.size());
//         assertEquals("Learn Spring Boot", result.get(0).getTitle());
//         assertEquals("Spring project", result.get(1).getTitle());
//         verify(taskRepository, times(1)).findByTitleContainingIgnoreCase("spring");
//     }

//     @Test
//     void shouldUpdateTaskPriority() {
//         Task existingTask = new Task(1L, "Old Title", false, "LOW", LocalDate.of(2026, 4, 20));
//         Task updatedTask = new Task(null, "New Title", true, "HIGH", LocalDate.of(2026, 4, 20));

//         when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
//         when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

//         Task result = taskService.updateTask(1L, updatedTask);

//         assertEquals("HIGH", result.getPriority());
//         assertEquals("New Title", result.getTitle());
//         assertTrue(result.isCompleted());
//     }


//     @Test
//     void shouldUpdateTaskDueDate() {
//         Task existingTask = new Task(1L, "Old Title", false, "LOW", LocalDate.of(2026, 4, 20));
//         Task updatedTask = new Task(null, "New Title", true, "HIGH", LocalDate.of(2026, 4, 25));

//         when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
//         when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

//         Task result = taskService.updateTask(1L, updatedTask);

//         assertEquals(LocalDate.of(2026, 4, 25), result.getDueDate());
//         assertEquals("HIGH", result.getPriority());
//         assertEquals("New Title", result.getTitle());
//         assertTrue(result.isCompleted());
//     }   

//     @Test
//     void shouldGetOverdueTasks() {
//         Task overdueTask = new Task(1L, "Late task", false, "HIGH", LocalDate.of(2026, 4, 1));

//         when(taskRepository.findByDueDateBeforeAndCompletedFalse(any(LocalDate.class)))
//                 .thenReturn(List.of(overdueTask));

//         List<Task> result = taskService.getOverdueTasks();

//         assertEquals(1, result.size());
//         assertEquals("Late task", result.get(0).getTitle());
//         assertFalse(result.get(0).isCompleted());
//         verify(taskRepository, times(1)).findByDueDateBeforeAndCompletedFalse(any(LocalDate.class));
//     }

//     @Test
//     void shouldGetTasksSortedByDueDate() {
//         Task task1 = new Task(1L, "Second task", false, "MEDIUM", LocalDate.of(2026, 4, 30));
//         Task task2 = new Task(2L, "First task", false, "HIGH", LocalDate.of(2026, 4, 20));

//         when(taskRepository.findAllByOrderByDueDateAsc()).thenReturn(List.of(task2, task1));

//         List<Task> result = taskService.getTasksSortedByDueDate();

//         assertEquals(2, result.size());
//         assertEquals("First task", result.get(0).getTitle());
//         assertEquals(LocalDate.of(2026, 4, 20), result.get(0).getDueDate());
//         verify(taskRepository, times(1)).findAllByOrderByDueDateAsc();
//     }

//     @Test
//     void shouldGetTasksByPriority() {
//         Task task = new Task(1L, "Important task", false, "HIGH", LocalDate.of(2026, 4, 20));

//         when(taskRepository.findByPriorityIgnoreCase("HIGH")).thenReturn(List.of(task));

//         List<Task> result = taskService.getTasksByPriority("HIGH");

//         assertEquals(1, result.size());
//         assertEquals("HIGH", result.get(0).getPriority());
//         verify(taskRepository, times(1)).findByPriorityIgnoreCase("HIGH");
//     }
// }