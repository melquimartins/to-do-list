package io.github.melquimartins.todolist.shared.dto;

public record ResponseEnvelope<T>(String message, T data) {
}
