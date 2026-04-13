package work.team.model.dto;

import java.time.LocalDate;

public record TeamReportParams(Long id, LocalDate dateFrom, LocalDate dateTo) {
}