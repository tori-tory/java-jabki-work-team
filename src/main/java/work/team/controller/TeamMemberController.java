package work.team.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import work.team.model.dto.ApiSuccess;
import work.team.model.dto.TeamMemberRequest;
import work.team.model.dto.TeamMemberResponse;
import work.team.service.TeamMemberService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/member")
@Tag(name = "Состав команды")
public class TeamMemberController {

    private final TeamMemberService teamMemberService;

    @PostMapping
    @Operation(summary = "Добавить пользователя в команду")
    public TeamMemberResponse create(@RequestBody TeamMemberRequest teamMemberRequest) {
        return teamMemberService.create(teamMemberRequest);
    }

    @DeleteMapping
    @Operation(summary = "Удалить пользователя из команды")
    public ApiSuccess delete(@RequestBody TeamMemberRequest teamMemberRequest) {
        return teamMemberService.delete(teamMemberRequest);
    }
}