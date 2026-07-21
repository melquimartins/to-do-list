package io.github.melquimartins.todolist.domain.task;

import io.github.melquimartins.todolist.domain.task.shared.dto.ApiResponse;
import io.github.melquimartins.todolist.domain.task.dto.CreateTaskRequest;
import io.github.melquimartins.todolist.domain.task.dto.TaskResponse;
import io.github.melquimartins.todolist.domain.task.dto.UpdateTaskRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

  private final TaskService service;

  public TaskController(TaskService service) {
    this.service = service;
  }

  @PostMapping
  public ResponseEntity<ApiResponse<TaskResponse>> create(
        @Valid @RequestBody CreateTaskRequest request
  ) {
    TaskResponse task = service.create(request);

    ApiResponse<TaskResponse> response = new ApiResponse<>(
          "Tarefa criada com sucesso.",
          task
    );

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PatchMapping("/{taskId}")
  public ResponseEntity<ApiResponse<TaskResponse>> update(
        @PathVariable Long taskId,
        @RequestBody UpdateTaskRequest request
  ) {
    TaskResponse task = service.update(taskId, request);

    ApiResponse<TaskResponse> response = new ApiResponse<>(
          "Tarefa atualizada com sucesso.",
          task
    );

    return ResponseEntity.ok().body(response);
  }

  @GetMapping("/{taskId}")
  public ResponseEntity<ApiResponse<TaskResponse>> get(
        @PathVariable Long taskId
  ) {
    TaskResponse task = service.get(taskId);

    ApiResponse<TaskResponse> response = new ApiResponse<>(
          "Tarefa obtida com sucesso.",
          task
    );

    return ResponseEntity.ok().body(response);
  }

  @GetMapping
  public ResponseEntity<ApiResponse<List<TaskResponse>>> getAll() {
    List<TaskResponse> tasks = service.getAll();

    ApiResponse<List<TaskResponse>> response = new ApiResponse<>(
          "Tarefas obtidas com sucesso.",
          tasks
    );

    return ResponseEntity.ok().body(response);
  }

  @DeleteMapping("/{taskId}")
  public ResponseEntity<String> delete(@PathVariable Long taskId) {
    service.delete(taskId);

    return ResponseEntity.ok().body("Tarefa removida com sucesso.");
  }

}
