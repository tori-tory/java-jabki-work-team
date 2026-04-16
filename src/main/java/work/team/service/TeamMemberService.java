package work.team.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import work.team.exception.NotFoundException;
import work.team.model.TeamMember;
import work.team.model.dto.ApiSuccess;
import work.team.model.dto.TeamMemberRequest;
import work.team.model.dto.TeamMemberResponse;
import work.team.repository.TeamMemberRepository;

@Service
@RequiredArgsConstructor
public class TeamMemberService {
    private final TeamMemberRepository teamMemberRepository;
    private final TeamService teamService;
    private final UserClientService userClientService;

    @Transactional
    public TeamMemberResponse create(final TeamMemberRequest teamMemberRequest) {
        userClientService.checkUser(teamMemberRequest.editorId(), "Редактор");
        userClientService.checkRoleManager(teamMemberRequest.editorId(), "Редактор");
        userClientService.checkUser(teamMemberRequest.userId(), "Пользователь");
        validateTeam(teamMemberRequest.teamId());
        TeamMember teamMember = TeamMember.builder()
                .teamId(teamMemberRequest.teamId())
                .userId(teamMemberRequest.userId())
                .build();
        TeamMember newTeamMember = teamMemberRepository.insert(teamMember);

        return new TeamMemberResponse(
                newTeamMember.getTeamId(),
                newTeamMember.getUserId());
    }

    @Transactional
    public ApiSuccess delete(final TeamMemberRequest teamMemberRequest) {
        validateTeam(teamMemberRequest.teamId());
        userClientService.checkUser(teamMemberRequest.editorId(), "Редактор");
        userClientService.checkRoleManager(teamMemberRequest.editorId(), "Редактор");

        TeamMember teamMember = TeamMember.builder()
                .teamId(teamMemberRequest.teamId())
                .userId(teamMemberRequest.userId())
                .build();
        int rowsDeleted = teamMemberRepository.delete(teamMember);

        if (rowsDeleted == 0) {
            throw new NotFoundException(
                    String.format("Удаление не выполнено по параметрам: Команда = %s, Пользователь = %s",
                    teamMemberRequest.teamId(),
                    teamMemberRequest.userId()));
        }

        return new ApiSuccess(true);
    }

    private void validateTeam(Long id) {
        teamService.existsById(id);
    }
}