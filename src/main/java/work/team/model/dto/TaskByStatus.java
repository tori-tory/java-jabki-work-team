package work.team.model.dto;

import work.team.model.TaskStatus;

public record TaskByStatus(TaskStatus taskStatus, Long total) {
}