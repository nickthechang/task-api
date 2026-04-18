package com.nick.taskapp.repository;

import com.nick.taskapp.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByCompleted(boolean completed);

    List<Task> findByTitleContainingIgnoreCase(String title);

    List<Task> findByDueDateBeforeAndCompletedFalse(LocalDate date);

    List<Task> findAllByOrderByDueDateAsc();

    List<Task> findByPriorityIgnoreCase(String priority);
    
}