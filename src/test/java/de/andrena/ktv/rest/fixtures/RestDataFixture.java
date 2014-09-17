package de.andrena.ktv.rest.fixtures;

import static de.andrena.ktv.core.fixtures.Fixtures.PLAYER2_OBELIX;
import static de.andrena.ktv.core.fixtures.Fixtures.PLAYER1_ASTERIX;
import static de.andrena.ktv.core.fixtures.Fixtures.STANDARD_KEY_STRING;
import static de.andrena.ktv.core.fixtures.Fixtures.TEAM_NAME_GAUL;
import static de.andrena.ktv.core.fixtures.Fixtures.standardTeamDetailsWithoutKey;

import java.util.ArrayList;
import java.util.List;

import de.andrena.ktv.core.domain.TeamDetails;
import de.andrena.ktv.core.events.teams.ReadedAllTeamDetailsEvent;
import de.andrena.ktv.rest.domain.RestTeam;

public class RestDataFixture {

	public static RestTeam standardRestTeam() {
		RestTeam team = new RestTeam();
		team.setName(TEAM_NAME_GAUL);
		team.setPlayer1(PLAYER1_ASTERIX);
		team.setPlayer2(PLAYER2_OBELIX);
		return team;
	}

	public static ReadedAllTeamDetailsEvent allTeams() {
		List<TeamDetails> teamsDetails = new ArrayList<>();

		teamsDetails.add(standardTeamDetailsWithoutKey());
		teamsDetails.add(standardTeamDetailsWithoutKey());
		teamsDetails.add(standardTeamDetailsWithoutKey());

		return new ReadedAllTeamDetailsEvent(teamsDetails);
	}

	public static String standardTeamJSON() {
		return ""//
				+ "{"//
				+ "\"name\": \"" + TEAM_NAME_GAUL + "\"," //
				+ "\"player1\": \"" + PLAYER1_ASTERIX + "\"," //
				+ "\"player2\": \"" + PLAYER2_OBELIX + "\"" //
				+ "}";
	}

	public static String standardTeamJSONWithKey() {
		return ""//
				+ "{"//
				+ "\"key\": \"" + STANDARD_KEY_STRING + "\"," //
				+ standardTeamJSON().substring(1);
	}
}
