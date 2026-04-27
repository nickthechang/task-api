package com.nick.taskapp.exception;
/*
 * Exception thrown when a requested task cannot be found.
 *
 * Handled globally to return a 404 Not Found response.
 */
public class TaskNotFoundException extends RuntimeException{
    public TaskNotFoundException(String message) {
        super(message);
    }
}

/*
TaskNotFoundException is a custom exception for when a task does not exist.
Instead of throwing a generic error like:
throw new RuntimeException("Task not found");
you throw a more specific exception:
throw new TaskNotFoundException("Task not found");
That makes your error flow cleaner and easier to handle.

1. Custom Exception
Creates a specific error type for a specific app problem.
2. Exception-driven error handling
Instead of manually checking errors in every controller method, the service throws an exception and the global handler formats the response.
 */