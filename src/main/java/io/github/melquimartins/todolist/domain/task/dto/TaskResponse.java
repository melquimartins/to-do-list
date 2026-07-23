package io.github.melquimartins.todolist.domain.task.dto;

import io.github.melquimartins.todolist.domain.task.enums.PriorityLevel;

public record TaskResponse(
        Long id,
        String title,
        String description,
        Boolean completed,
        PriorityLevel priorityLevel
) {
}
