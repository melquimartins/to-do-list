package io.github.melquimartins.todolist.domain.task.dto;

import io.github.melquimartins.todolist.domain.task.enums.PriorityLevel;
import io.github.melquimartins.todolist.shared.validation.annotation.NotBlankIfPresent;
import jakarta.validation.constraints.Size;

public record UpdateTaskRequest(
        @NotBlankIfPresent
        @Size(
                min = 4,
                max = 32,
                message = "O título deve ter entre 4 e 32 caracteres."
        )
        String title,

        @NotBlankIfPresent
        @Size(
                min = 4,
                max = 120,
                message = "A descrição deve ter entre 4 e 120 caracteres."
        )
        String description,

        Boolean completed,
        PriorityLevel priorityLevel
) {
}
