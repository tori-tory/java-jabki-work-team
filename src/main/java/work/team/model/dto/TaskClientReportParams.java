package work.team.model.dto;

import java.time.LocalDate;
import java.util.List;

public record TaskClientReportParams(List<Long> assigneeIds, LocalDate dateFrom, LocalDate dateTo) {
}