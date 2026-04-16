package work.team.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import work.team.client.TaskClient;
import work.team.exception.NotFoundException;
import work.team.exception.TeamException;
import work.team.model.TaskStatus;
import work.team.model.Team;
import work.team.model.dto.ActiveMember;
import work.team.model.dto.TaskByStatus;
import work.team.model.dto.TaskClientResponse;
import work.team.model.dto.TeamReportResponse;
import work.team.model.dto.TeamRequest;
import work.team.model.dto.TeamResponse;
import work.team.repository.TeamMemberRepository;
import work.team.repository.TeamRepository;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public TeamReportResponse teamReport(final Long id, final LocalDate dateFrom, final LocalDate dateTo) {
        existsById(id);
        validatePeriod(dateFrom, dateTo);

        List<Long> userIds = teamMemberRepository.getUsersByTeam(id);

        if (userIds.isEmpty()) {
            return new TeamReportResponse(
                    0,
                    List.of(),
                    List.of(),
                    0.0);
        }

        List<TaskClientResponse> tasks = taskClient.getTasksByAssigneeIds(userIds);

        List<TaskClientResponse> tasksByPeriod = tasks.stream()
                .filter(task -> {
                    LocalDate deadLine = task.deadLine();
                    return deadLine != null
                            && !deadLine.isBefore(dateFrom)
                            && !deadLine.isAfter(dateTo);
                })
                .toList();

        Double avgDays = tasksByPeriod.stream()
                .filter(task -> TaskStatus.DONE.equals(task.status()))
                .mapToDouble(task -> java.time.Duration.between(task.createdAt(), task.updatedAt())
                        .toHours() / 24.0)
                . average()
                .orElse(0.0);

        Map<TaskStatus, Long> mapByStatus = tasksByPeriod.stream()
                .collect(Collectors.groupingBy(
                        TaskClientResponse::status,
                        Collectors.counting()
                ));

        List<TaskByStatus> taskByStatus = mapByStatus.entrySet()
                .stream()
                .map(e -> new TaskByStatus(e.getKey(), e.getValue()))
                .toList();

        Map<Long, Long> totalByUser = tasksByPeriod.stream()
                .filter(task -> task.assignee() != null)
                .collect(Collectors.groupingBy(
                        TaskClientResponse::assignee,
                        Collectors.counting()
                ));

        List<ActiveMember> sortedListMember = totalByUser.entrySet()
                .stream()
                .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                .limit(5)
                .map(e -> new ActiveMember(e.getKey(), e.getValue()))
                .toList();

        return new TeamReportResponse(
                tasksByPeriod.size(),
                taskByStatus,
                sortedListMember,
                avgDays
        );
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

    private void validatePeriod(final LocalDate dateFrom, final LocalDate dateTo) {
        if ((dateFrom == null) || (dateTo == null)) {
            throw new TeamException("Задайте корректный период поиска");
        }
        if (dateFrom.isAfter(dateTo)) {
            throw new TeamException("Дата начала не может быть позже даты окончания");
        }
    }
}