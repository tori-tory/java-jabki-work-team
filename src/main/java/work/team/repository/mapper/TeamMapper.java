package work.team.repository.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import work.team.model.Team;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TeamMapper implements RowMapper<Team> {

    @Override
    public Team mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Team.builder()
                .id(rs.getLong("id"))
                .title(rs.getString("title"))
                .ownerId(rs.getLong("owner_id"))
                .authorId(rs.getLong("author_id"))
                .build();
    }
}