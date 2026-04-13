CREATE SCHEMA IF NOT EXISTS work_team;

CREATE TABLE IF NOT EXISTS work_team.team
(   id SERIAL PRIMARY KEY,
    title VARCHAR NOT NULL,
	owner_id bigint NOT NULL,
	author_id bigint NOT NULL,
    created_at TIMESTAMP without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP without time zone
);

CREATE TABLE IF NOT EXISTS work_team.team_member
(   team_id bigint NOT NULL,
	user_id bigint NOT NULL,

	PRIMARY KEY (team_id, user_id),

	CONSTRAINT fk_team
          FOREIGN KEY(team_id)
          REFERENCES work_team.team(id) ON DELETE CASCADE
);