package io.github.melquimartins.todolist.domain.task.shared.dto;

public record ApiResponse<T>(String message, T data) {}
