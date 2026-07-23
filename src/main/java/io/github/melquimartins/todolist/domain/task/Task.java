package io.github.melquimartins.todolist.domain.task;

import io.github.melquimartins.todolist.domain.task.enums.PriorityLevel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@DynamicUpdate
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(nullable = false)
    private Boolean completed = false;

    @Column(nullable = false)
    private PriorityLevel priorityLevel = PriorityLevel.LOW;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Task() {
    }

    public Task(
            String title,
            String description,
            Boolean completed,
            PriorityLevel priorityLevel
    ) {
        this.title = title;
        this.description = description;
        this.completed = completed;
        this.priorityLevel = priorityLevel;
    }

}
