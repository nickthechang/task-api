package com.nick.taskapp.specification;

import com.nick.taskapp.model.Priority;
import com.nick.taskapp.model.Task;
import org.springframework.data.jpa.domain.Specification;
/*
 * Builds reusable query filters for Task searches.
 *
 * Used with JpaSpecificationExecutor to support optional
 * filters in advanced search endpoints.
 */
public class TaskSpecification {
    /*
     * Filters tasks whose title contains the given text.
     * Returns null when no title filter is provided.
     */    
    public static Specification<Task> hasTitle(String title) {
        return (root, query, cb) ->
                title == null || title.isBlank()
                        ? null
                        : cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }
    
    /*
     * Filters tasks by priority.
     * Returns null when no priority filter is provided.
     */
    public static Specification<Task> hasPriority(Priority priority) {
        return (root, query, cb) ->
                priority == null
                        ? null
                        : cb.equal(root.get("priority"), priority);
    }
    
    /*
     * Filters tasks by completion status.
     * Returns null when no completed filter is provided.
     */
    public static Specification<Task> isCompleted(Boolean completed) {
        return (root, query, cb) ->
                completed == null
                        ? null
                        : cb.equal(root.get("completed"), completed);
    }
}

/*
TaskSpecification builds optional database filters for tasks.
It lets your advanced endpoint filter by:
title
priority
completed
without needing a separate repository method for every possible combination.
So instead of making methods like:
findByTitleAndPriorityAndCompleted(...)
findByPriorityAndCompleted(...)
findByTitleAndCompleted(...)
findByTitleAndPriority(...)
you build small filters and combine them.


1. Specification Pattern
Each method returns a reusable filter condition.
Example:
hasTitle(title)
means:
“filter tasks where title contains this text”
2. Dynamic Query Building
If a value is null, the filter is ignored.
So this works:
/tasks/advanced?priority=HIGH
and this also works:
/tasks/advanced?title=study&completed=false
Same endpoint, different filters.
3. Criteria API
Inside the lambda:
(root, query, cb) -> ...
Spring/JPA uses Criteria Builder to create SQL conditions programmatically.
Very simply:
root = the Task table/entity
cb = tool that builds SQL conditions
query = the full query being built
*/