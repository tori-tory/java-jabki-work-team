package work.team.repository;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import work.team.model.Team;
import work.team.repository.mapper.TeamMapper;

@Repository
@AllArgsConstructor
public class TeamRepository {
    private static final String INSERT = """
            INSERT INTO work_team.team(title, owner_id, author_id)
            VALUES (:title, :owner_id, :author_id)
            RETURNING id
            """;

    private static final String EXISTS_BY_ID =  """
            SELECT EXISTS (
                SELECT 1
                FROM work_team.team
                WHERE id = :id
                )
            """;

    private final TeamMapper teamMapper;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public Long insert(final Team team){
        return jdbcTemplate.queryForObject(INSERT, teamToSql(team), Long.class);
    }

    public boolean existsById(final Long id) {
        return Boolean.TRUE.equals(
                jdbcTemplate.queryForObject(EXISTS_BY_ID, new MapSqlParameterSource("id", id), Boolean.class));
    }

    private MapSqlParameterSource teamToSql(final Team team) {
        final MapSqlParameterSource params = new MapSqlParameterSource();

        params.addValue("title", team.getTitle());
        params.addValue("owner_id", team.getOwnerId());
        params.addValue("author_id", team.getAuthorId());
        return params;
    }
}