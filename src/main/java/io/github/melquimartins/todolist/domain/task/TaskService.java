package io.github.melquimartins.todolist.domain.task;

import io.github.melquimartins.todolist.domain.task.dto.CreateTaskRequest;
import io.github.melquimartins.todolist.domain.task.dto.TaskResponse;
import io.github.melquimartins.todolist.domain.task.dto.UpdateTaskRequest;
import io.github.melquimartins.todolist.domain.task.mapper.TaskMapper;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository repository;
    private final TaskMapper mapper;

    public TaskService(TaskRepository repository, TaskMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public TaskResponse create(CreateTaskRequest request) {
        Task task = new Task(
                request.title(),
                request.description(),
                request.completed(),
                request.priorityLevel()
        );

        repository.save(task);

        return mapper.toResponse(task);
    }

    public List<TaskResponse> getAll() {
        List<Task> tasks = repository.findAll();

        if (tasks.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Nenhuma tarefa não encontrada."
            );
        }

        return mapper.toResponseList(tasks);
    }

    public TaskResponse get(Long taskId) {
        Task task = repository.findById(taskId).orElseThrow(() ->
                new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Tarefa não encontrada."
                )
        );

        return mapper.toResponse(task);
    }

    public TaskResponse update(Long taskId, UpdateTaskRequest request) {
        Task task = repository.findById(taskId).orElseThrow(() ->
                new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Tarefa não encontrada."
                )
        );

        if (request.title() != null) {
            task.setTitle(request.title());
        }

        if (request.description() != null) {
            task.setDescription(request.description());
        }

        if (request.completed() != null) {
            task.setCompleted(request.completed());
        }

        if (request.priorityLevel() != null) {
            task.setPriorityLevel(request.priorityLevel());
        }

        repository.save(task);

        return mapper.toResponse(task);
    }

    @Transactional
    public void delete(Long taskId) {
        if (!repository.existsById(taskId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Tarefa não encontrada."
            );
        }

        repository.deleteById(taskId);
    }

}
