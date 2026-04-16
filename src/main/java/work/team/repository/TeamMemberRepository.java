package work.team.repository;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import work.team.model.TeamMember;
import work.team.repository.mapper.TeamMemberMapper;

import java.util.List;

@Repository
@AllArgsConstructor
public class TeamMemberRepository {

    private static final String INSERT = """
            INSERT INTO work_team.team_member(team_id, user_id)
            VALUES (:team_id, :user_id)
            RETURNING *
            """;

    private static final String DELETE = """
            DELETE FROM  work_team.team_member
            WHERE team_id = :team_id
            AND user_id = :user_id
            """;

    private static final String GET_USER_BY_TEAM = """
            SELECT user_id
            FROM work_team.team_member
            WHERE team_id = :team_id
            """;

    private final TeamMemberMapper teamMemberMapper;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public TeamMember insert(final TeamMember teamMember){
        return jdbcTemplate.queryForObject(INSERT, teamMemberToSql(teamMember), teamMemberMapper);
    }

    public int delete(final TeamMember teamMember) {
        return jdbcTemplate.update(DELETE, teamMemberToSql(teamMember));
    }

    public List<Long> getUsersByTeam(final Long id){
        return jdbcTemplate.queryForList(GET_USER_BY_TEAM, new MapSqlParameterSource("team_id", id), Long.class);
    }

    private MapSqlParameterSource teamMemberToSql(final TeamMember teamMember) {
        final MapSqlParameterSource params = new MapSqlParameterSource();

        params.addValue("team_id", teamMember.getTeamId());
        params.addValue("user_id", teamMember.getUserId());
        return params;
    }
}