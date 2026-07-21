package io.github.melquimartins.todolist.domain.task;

import io.github.melquimartins.todolist.domain.task.dto.CreateTaskRequest;
import io.github.melquimartins.todolist.domain.task.dto.TaskResponse;
import io.github.melquimartins.todolist.domain.task.dto.UpdateTaskRequest;
import io.github.melquimartins.todolist.domain.task.enums.PriorityLevel;
import io.github.melquimartins.todolist.domain.task.mapper.TaskMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

  @Mock
  private TaskRepository repository;

  @Mock
  private TaskMapper mapper;

  @InjectMocks
  private TaskService service;

  @Test
  @DisplayName("deve criar uma tarefa com sucesso")
  void shouldSuccessfullyCreateTask() {
    CreateTaskRequest request = new CreateTaskRequest(
          "Tarefa",
          "Descrição",
          null,
          null
    );

    Task task = new Task(
          request.title(),
          request.description(),
          request.completed(),
          request.priorityLevel()
    );

    TaskResponse expectedResponse = new TaskResponse(
          1L,
          task.getTitle(),
          task.getDescription(),
          task.getCompleted(),
          task.getPriorityLevel()
    );

    when(repository.save(any(Task.class))).thenReturn(task);
    when(mapper.toResponse(any(Task.class))).thenReturn(expectedResponse);

    TaskResponse response = service.create(request);

    assertNotNull(response);
    assertEquals(1L, response.id());
    assertEquals("Tarefa", response.title());
    verify(repository).save(any(Task.class));
    verify(mapper).toResponse(any(Task.class));
  }

  @Test
  @DisplayName("Deve obter todas as tarefas")
  void shouldFetchAllTasks() {
    Task t1 = new Task("Tarefa 1", "Descrição 1", false, PriorityLevel.LOW);
    Task t2 = new Task("Tarefa 2", "Descrição 2", false, PriorityLevel.LOW);

    List<Task> tasks = List.of(t1, t2);

    TaskResponse r1 = new TaskResponse(
          1L,
          t1.getTitle(),
          t1.getDescription(),
          t1.getCompleted(),
          t1.getPriorityLevel()
    );
    TaskResponse r2 = new TaskResponse(
          2L,
          t2.getTitle(),
          t2.getDescription(),
          t2.getCompleted(),
          t2.getPriorityLevel()
    );

    List<TaskResponse> expectedResponse = List.of(r1, r2);

    when(repository.findAll()).thenReturn(tasks);
    when(mapper.toResponseList(anyList())).thenReturn(expectedResponse);

    List<TaskResponse> response = service.getAll();

    assertNotNull(response);
    assertEquals(1L, response.getFirst().id());
    assertEquals("Tarefa 1", response.getFirst().title());
    verify(repository).findAll();
    verify(mapper).toResponseList(anyList());
  }

  @Test
  @DisplayName("Deve obter uma tarefa por ID com sucesso")
  void shouldFetchTaskById() {
    Long taskId = 1L;
    Task task = new Task("Tarefa 1", "Descrição 1", false, PriorityLevel.LOW);

    TaskResponse expectedResponse = new TaskResponse(
          taskId,
          task.getTitle(),
          task.getDescription(),
          task.getCompleted(),
          task.getPriorityLevel()
    );

    when(repository.findById(taskId)).thenReturn(Optional.of(task));
    when(mapper.toResponse(task)).thenReturn(expectedResponse);

    TaskResponse response = service.get(taskId);

    assertNotNull(response);
    assertEquals(taskId, response.id());
    assertEquals("Tarefa 1", response.title());
    verify(repository).findById(taskId);
    verify(mapper).toResponse(task);
  }

  @Test
  @DisplayName("Deve atualizar uma tarefa com sucesso")
  void shouldSuccessfullyUpdateTask() {
    Long taskId = 1L;
    Task task = new Task("Tarefa Antiga", "Descrição Antiga", false, PriorityLevel.LOW);
    UpdateTaskRequest request = new UpdateTaskRequest(
          "Tarefa Atualizada",
          "Descrição Atualizada",
          true,
          PriorityLevel.HIGH
    );

    TaskResponse expectedResponse = new TaskResponse(
          taskId,
          "Tarefa Atualizada",
          "Descrição Atualizada",
          true,
          PriorityLevel.HIGH
    );

    when(repository.findById(taskId)).thenReturn(Optional.of(task));
    when(repository.save(any(Task.class))).thenReturn(task);
    when(mapper.toResponse(any(Task.class))).thenReturn(expectedResponse);

    TaskResponse response = service.update(taskId, request);

    assertNotNull(response);
    assertEquals("Tarefa Atualizada", response.title());
    assertTrue(response.completed());
    assertEquals(PriorityLevel.HIGH, response.priorityLevel());

    verify(repository).findById(taskId);
    verify(repository).save(task);
    verify(mapper).toResponse(task);
  }

  @Test
  @DisplayName("Deve remover uma tarefa por ID com sucesso")
  void shouldSuccessfullyDeleteTask() {
    Long taskId = 1L;

    when(repository.existsById(taskId)).thenReturn(true);
    doNothing().when(repository).deleteById(taskId);

    assertDoesNotThrow(() -> service.delete(taskId));

    verify(repository).existsById(taskId);
    verify(repository).deleteById(taskId);
  }

  @Test
  @DisplayName("Deve lançar ResponseStatusException ao tentar obter todas as tarefas e nenhuma for encontrada")
  void shouldThrowNotFoundWhenNoTasksFound() {
    when(repository.findAll()).thenReturn(List.of());

    ResponseStatusException exception = assertThrows(
          ResponseStatusException.class,
          () -> service.getAll()
    );

    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    assertEquals("Nenhuma tarefa não encontrada.", exception.getReason());
    verify(repository).findAll();
    verifyNoInteractions(mapper);
  }

  @Test
  @DisplayName("Deve lançar ResponseStatusException ao tentar obter uma tarefa por ID inexistente")
  void shouldThrowNotFoundWhenTaskByIdDoesNotExist() {
    Long taskId = 999L;
    when(repository.findById(taskId)).thenReturn(Optional.empty());

    ResponseStatusException exception = assertThrows(
          ResponseStatusException.class,
          () -> service.get(taskId)
    );

    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    assertEquals("Tarefa não encontrada.", exception.getReason());
    verify(repository).findById(taskId);
    verifyNoInteractions(mapper);
  }

  @Test
  @DisplayName("Deve lançar ResponseStatusException ao tentar atualizar uma tarefa inexistente")
  void shouldThrowNotFoundWhenUpdatingNonExistentTask() {
    Long taskId = 999L;
    UpdateTaskRequest request = new UpdateTaskRequest("Título", "Descrição", true, PriorityLevel.LOW);

    when(repository.findById(taskId)).thenReturn(Optional.empty());

    ResponseStatusException exception = assertThrows(
          ResponseStatusException.class,
          () -> service.update(taskId, request)
    );

    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    assertEquals("Tarefa não encontrada.", exception.getReason());
    verify(repository).findById(taskId);
    verifyNoMoreInteractions(repository);
    verifyNoInteractions(mapper);
  }

  @Test
  @DisplayName("Deve lançar ResponseStatusException ao tentar remover uma tarefa inexistente")
  void shouldThrowNotFoundWhenDeletingNonExistentTask() {
    Long taskId = 999L;

    when(repository.existsById(taskId)).thenReturn(false);

    ResponseStatusException exception = assertThrows(
          ResponseStatusException.class,
          () -> service.delete(taskId)
    );

    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    assertEquals("Tarefa não encontrada.", exception.getReason());
    verify(repository).existsById(taskId);
    verify(repository, never()).deleteById(taskId);
  }

}