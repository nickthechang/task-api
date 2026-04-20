package com.nick.taskapp.specification;

import com.nick.taskapp.model.Priority;
import com.nick.taskapp.model.Task;
import org.springframework.data.jpa.domain.Specification;

public class TaskSpecification {

    public static Specification<Task> hasTitle(String title) {
        return (root, query, cb) ->
                title == null || title.isBlank()
                        ? null
                        : cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    public static Specification<Task> hasPriority(Priority priority) {
        return (root, query, cb) ->
                priority == null
                        ? null
                        : cb.equal(root.get("priority"), priority);
    }

    public static Specification<Task> isCompleted(Boolean completed) {
        return (root, query, cb) ->
                completed == null
                        ? null
                        : cb.equal(root.get("completed"), completed);
    }
}