package work.team.model.dto;

import work.team.model.TaskStatus;

import java.util.Map;

public record TeamReportResponse(
        Long totalTasks,
        Map<TaskStatus, Long> taskByStatus,
        Map<Long, Long> taskByAssignee,
        Double avgDays) {
}