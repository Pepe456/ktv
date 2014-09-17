package de.andrena.ktv.core.fixtures;

import java.util.UUID;

import de.andrena.ktv.core.domain.Team;
import de.andrena.ktv.core.domain.TeamDetails;
import de.andrena.ktv.persistence.domain.RepositoryTeam;

public class Fixtures {

	public static final String TEAM_NAME_GAUL = "Gaul";
	public static final String TEAM_NAME_ROMAN = "Roman";
	public static final String PLAYER1_ASTERIX = "Asterix";
	public static final String PLAYER2_OBELIX = "Obelix";
	public static final String STANDARD_KEY_STRING = "f3512d26-72f6-4290-9265-63ad69eccc13";
	public static final UUID STANDARD_KEY = UUID.fromString(STANDARD_KEY_STRING);

	public static Team standardTeamWithoutKey() {
		return new Team(TEAM_NAME_GAUL, PLAYER1_ASTERIX, PLAYER2_OBELIX);
	}

	public static Team standardTeamWithKey() {
		return new Team(STANDARD_KEY, TEAM_NAME_GAUL, PLAYER1_ASTERIX, PLAYER2_OBELIX);
	}

	public static Team standardTeamWithKey(UUID key) {
		return new Team(key, TEAM_NAME_GAUL, PLAYER1_ASTERIX, PLAYER2_OBELIX);
	}

	public static TeamDetails standardTeamDetailsWithoutKey() {
		return standardTeamWithoutKey().toTeamDetails();
	}

	public static TeamDetails standardTeamDetailsWithKey() {
		return standardTeamWithKey().toTeamDetails();
	}

	public static TeamDetails standardTeamDetailsWithKey(UUID key) {
		return standardTeamWithKey(key).toTeamDetails();
	}

	public static RepositoryTeam standardRepositoryTeamWithKey() {
		RepositoryTeam team = new RepositoryTeam();
		team.setId(STANDARD_KEY_STRING);
		team.setName(TEAM_NAME_GAUL);
		team.setPlayer1(PLAYER1_ASTERIX);
		team.setPlayer2(PLAYER2_OBELIX);
		return team;
	}

	public static RepositoryTeam standardRepositoryTeamWithKey(String key) {
		RepositoryTeam team = new RepositoryTeam();
		team.setId(key);
		team.setName(TEAM_NAME_GAUL);
		team.setPlayer1(PLAYER1_ASTERIX);
		team.setPlayer2(PLAYER2_OBELIX);
		return team;
	}

}
