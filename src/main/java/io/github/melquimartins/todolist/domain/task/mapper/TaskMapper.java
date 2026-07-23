package io.github.melquimartins.todolist.domain.task.mapper;

import io.github.melquimartins.todolist.domain.task.Task;
import io.github.melquimartins.todolist.domain.task.dto.TaskResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public record TaskMapper() {
    
    public TaskResponse toResponse(Task entity) {
        return new TaskResponse(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getCompleted(),
                entity.getPriorityLevel()
        );
    }

    public List<TaskResponse> toResponseList(List<Task> entities) {
        return entities.stream().map(entity -> {
            return new TaskResponse(
                    entity.getId(),
                    entity.getTitle(),
                    entity.getDescription(),
                    entity.getCompleted(),
                    entity.getPriorityLevel()
            );
        }).toList();
    }

}
