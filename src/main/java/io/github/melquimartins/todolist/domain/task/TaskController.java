package io.github.melquimartins.todolist.domain.task;

import io.github.melquimartins.todolist.domain.task.dto.CreateTaskRequest;
import io.github.melquimartins.todolist.domain.task.dto.TaskResponse;
import io.github.melquimartins.todolist.domain.task.dto.UpdateTaskRequest;
import io.github.melquimartins.todolist.shared.dto.ResponseEnvelope;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Tarefas",
        description = "Endpoints para gerenciamento de tarefas"
)
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(
            summary = "Cria uma nova tarefa",
            description = "Cria uma tarefa a partir dos dados informados."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Tarefa criada com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados de requisição inválidos"
            )
    })
    public ResponseEntity<ResponseEnvelope<TaskResponse>> create(
            @Valid @RequestBody CreateTaskRequest request
    ) {
        TaskResponse task = service.create(request);

        ResponseEnvelope<TaskResponse> response = new ResponseEnvelope<>(
                "Tarefa criada com sucesso.",
                task
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Lista todas as tarefas",
            description = "Retorna uma lista com todas as tarefas cadastradas.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Tarefas obtidas com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Nenhuma tarefa encontrada"
            )
    })
    public ResponseEntity<ResponseEnvelope<List<TaskResponse>>> getAll() {
        List<TaskResponse> tasks = service.getAll();

        ResponseEnvelope<List<TaskResponse>> response = new ResponseEnvelope<>(
                "Tarefas obtidas com sucesso.",
                tasks
        );

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{taskId}")
    @Operation(
            summary = "Busca uma tarefa por ID",
            description = "Retorna os detalhes de uma tarefa específica a partir do seu ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Tarefa obtida com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Tarefa não encontrada"
            )
    })
    public ResponseEntity<ResponseEnvelope<TaskResponse>> get(
            @PathVariable Long taskId
    ) {
        TaskResponse task = service.get(taskId);

        ResponseEnvelope<TaskResponse> response = new ResponseEnvelope<>(
                "Tarefa obtida com sucesso.",
                task
        );

        return ResponseEntity.ok().body(response);
    }


    @PatchMapping("/{taskId}")
    @Operation(
            summary = "Atualiza uma tarefa existente",
            description = "Atualiza os campos informados de uma tarefa específica pelo ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Tarefa atualizada com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Tarefa não encontrada"
            )
    })
    public ResponseEntity<ResponseEnvelope<TaskResponse>> update(
            @PathVariable Long taskId,
            @RequestBody UpdateTaskRequest request
    ) {
        TaskResponse task = service.update(taskId, request);

        ResponseEnvelope<TaskResponse> response = new ResponseEnvelope<>("Tarefa atualizada com sucesso.", task);

        return ResponseEntity.ok().body(response);
    }


    @DeleteMapping("/{taskId}")
    @Operation(
            summary = "Remove uma tarefa",
            description = "Remove uma tarefa existente pelo seu ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Tarefa removida com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Tarefa não encontrada"
            )
    })
    public ResponseEntity<String> delete(@PathVariable Long taskId) {
        service.delete(taskId);

        return ResponseEntity.ok().body("Tarefa removida com sucesso.");
    }

}
