package work.team.model.dto;

import java.util.List;

public record TeamReportResponse(
        int totalTasks,
        List<TaskByStatus> taskByStatus,
        List<ActiveMember> activeMembers,
        Double avgDays) {
}