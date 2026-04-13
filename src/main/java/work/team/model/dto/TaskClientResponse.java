package work.team.model.dto;

import work.team.model.TaskStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record TaskClientResponse(
        Long id,
        String title,
        String description,
        TaskStatus status,
        LocalDate deadLine,
        Long author,
        Long assignee,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}