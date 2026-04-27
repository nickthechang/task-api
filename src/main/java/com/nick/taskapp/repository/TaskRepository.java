package com.nick.taskapp.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.nick.taskapp.model.Priority;
import com.nick.taskapp.model.Task;


/*
 * Repository for performing database operations on Task entities.
 *
 * Extends JpaRepository for standard CRUD operations and
 * JpaSpecificationExecutor for dynamic filtering queries.
 *  
 * Task = the entity/table this repository works with
 * Long = the type of the entity’s primary key (id)
 * JpaSpecificationExecutor<Task> This adds support for queries built dynamically at runtime.
 */
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {

    List<Task> findByCompleted(boolean completed);

    List<Task> findByTitleContainingIgnoreCase(String title);

    List<Task> findByDueDateBeforeAndCompletedFalse(LocalDate date);

    List<Task> findAllByOrderByDueDateAsc();

    List<Task> findByPriority(Priority priority);
    
}

/*
TaskRepository is your database access layer for Task entities.

It is the class interface that lets your service say things like:
save a task
find a task by id
get all tasks
search by title
filter by completed
filter by overdue
sort by due date
run advanced dynamic filtering
without writing SQL manually.

Repository pattern
This pattern separates database access from business logic.
Your service decides what should happen, and the repository handles how to fetch/store data.

Spring Data JPA
By extending JpaRepository<Task, Long>, you automatically get built-in CRUD operations like:
save()
findById()
findAll()
delete()
without writing them yourself.

Derived query methods
Methods like:
findByCompleted(boolean completed)
findByTitleContainingIgnoreCase(String title)
findByDueDateBeforeAndCompletedFalse(LocalDate date)
are parsed by Spring Data based on method names, and turned into queries automatically.
These methods are NOT calling something — they are being interpreted.

JPA Specification support
By extending JpaSpecificationExecutor<Task>, your repository can run dynamic queries built from Specification<Task>, which is what powers your advanced filtering endpoint.
*/