package com.nick.taskapp.repository;

import com.nick.taskapp.model.Task;
import com.nick.taskapp.model.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;


//Added JPASpecificationExecutor
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {

    List<Task> findByCompleted(boolean completed);

    List<Task> findByTitleContainingIgnoreCase(String title);

    List<Task> findByDueDateBeforeAndCompletedFalse(LocalDate date);

    List<Task> findAllByOrderByDueDateAsc();

    List<Task> findByPriorityIgnoreCase(Priority priority);
    
}