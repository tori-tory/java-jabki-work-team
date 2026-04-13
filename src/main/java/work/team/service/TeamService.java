package work.team.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import work.team.client.TaskClient;
import work.team.exception.NotFoundException;
import work.team.exception.TeamException;
import work.team.model.Team;
import work.team.model.dto.TaskClientReportParams;
import work.team.model.dto.TaskClientResponse;
import work.team.model.dto.TeamReportParams;
import work.team.model.dto.TeamReportResponse;
import work.team.model.dto.TeamRequest;
import work.team.model.dto.TeamResponse;
import work.team.repository.TeamMemberRepository;
import work.team.repository.TeamRepository;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final UserClientService userClientService;
    private final TaskClient taskClient;

    @Transactional
    public TeamResponse create(final TeamRequest teamRequest) {
        validateCreate(teamRequest);
        Team team = Team.builder()
                .title(teamRequest.title())
                .ownerId(teamRequest.ownerId())
                .authorId(teamRequest.authorId())
                .build();

        return new TeamResponse(
                teamRepository.insert(team),
                teamRequest.title(),
                teamRequest.ownerId(),
                teamRequest.authorId());
    }

    @Transactional(readOnly = true)
    public List<TaskClientResponse> getTasksById(Long id) {
        existsById(id);

        List<Long> userIds = teamMemberRepository.getUsersByTeam(id);

        if (userIds.isEmpty()) {
            return Collections.emptyList();
        }

        return taskClient.getTasksByAssigneeIds(userIds);
    }

    @Transactional(readOnly = true)
    public TeamReportResponse teamReport(TeamReportParams params) {
        existsById(params.id());

        List<Long> userIds = teamMemberRepository.getUsersByTeam(params.id());

        if (userIds.isEmpty()) {
            return new TeamReportResponse(
                    0L,
                    Collections.emptyMap(),
                    Collections.emptyMap(),
                    0.0
            );
        }

        return taskClient.teamReport(new TaskClientReportParams(userIds, params.dateFrom(), params.dateTo()));
    }

    private void validateCreate(final TeamRequest teamRequest) {
        validateTitle(teamRequest.title());
        userClientService.checkUser(teamRequest.authorId(), "Автор");
        userClientService.checkRoleManager(teamRequest.authorId(), "Автор");
        userClientService.checkUser(teamRequest.ownerId(), "Владелец команды");
        userClientService.checkRoleManager(teamRequest.ownerId(), "Владелец команды");
    }

    private void validateTitle(final String title) {
        if ((title == null) || title.isBlank()) {
            throw new TeamException("Заголовок не может быть пустым");
        }
    }

    public void existsById(Long id) {
        if (id == null) {
            throw new TeamException("Значение id не задано");
        }
        if (!teamRepository.existsById(id)) {
            throw new NotFoundException(String.format("Команда %s не найдена", id));
        }
    }
}