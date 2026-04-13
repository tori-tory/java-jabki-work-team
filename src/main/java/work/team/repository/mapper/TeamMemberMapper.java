package work.team.repository.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import work.team.model.TeamMember;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TeamMemberMapper implements RowMapper<TeamMember> {

    @Override
    public TeamMember mapRow(ResultSet rs, int rowNum) throws SQLException {
        return TeamMember.builder()
                .teamId(rs.getLong("team_id"))
                .userId(rs.getLong("user_id"))
                .build();
    }
}