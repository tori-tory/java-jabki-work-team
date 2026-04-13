package work.team.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import work.team.model.dto.TaskClientResponse;
import work.team.model.dto.TeamReportParams;
import work.team.model.dto.TeamReportResponse;
import work.team.model.dto.TeamRequest;
import work.team.model.dto.TeamResponse;
import work.team.service.TeamService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/team")
@Tag(name = "Команды")
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    @Operation(summary = "Создать команду")
    public TeamResponse create(@RequestBody TeamRequest userRequest) {
        return teamService.create(userRequest);
    }

    @GetMapping("tasks/{id}")
    @Operation(summary = "Получить список задач команды")
     public List<TaskClientResponse> getTasksById(@PathVariable("id") Long id) {
        return teamService.getTasksById(id);
    }

    @PostMapping("/report")
    @Operation(summary = "Получить отчет по команде за период")
    public TeamReportResponse getReport( @RequestBody TeamReportParams params) {
        return teamService.teamReport(params);
    }
}