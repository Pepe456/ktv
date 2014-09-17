package de.andrena.ktv.persistence.domain;

import static de.andrena.ktv.core.fixtures.Fixtures.PLAYER1_ASTERIX;
import static de.andrena.ktv.core.fixtures.Fixtures.PLAYER2_OBELIX;
import static de.andrena.ktv.core.fixtures.Fixtures.STANDARD_KEY_STRING;
import static de.andrena.ktv.core.fixtures.Fixtures.TEAM_NAME_GAUL;
import static de.andrena.ktv.core.fixtures.Fixtures.standardRepositoryTeamWithKey;
import static de.andrena.ktv.core.fixtures.Fixtures.standardTeamDetailsWithKey;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import de.andrena.ktv.core.domain.TeamDetails;
import de.andrena.ktv.persistence.domain.RepositoryTeam;

public class RepositoryTeamTest {

	@Test
	public void thatFromRepositoryTeamCreatesCorrectTeamDetails() throws Exception {
		TeamDetails teamDetails = standardRepositoryTeamWithKey().toTeamDetails();
		assertThat(teamDetails.getKey().toString(), is(STANDARD_KEY_STRING));
		assertThat(teamDetails.getName(), is(TEAM_NAME_GAUL));
		assertThat(teamDetails.getPlayer1(), is(PLAYER1_ASTERIX));
		assertThat(teamDetails.getPlayer2(), is(PLAYER2_OBELIX));
	}

	@Test
	public void thatFromTeamDetailsCreatesCorrectTeam() throws Exception {
		RepositoryTeam team = RepositoryTeam.fromTeamDetails(standardTeamDetailsWithKey());
		assertThat(team.getId(), is(STANDARD_KEY_STRING));
		assertThat(team.getName(), is(TEAM_NAME_GAUL));
		assertThat(team.getPlayer1(), is(PLAYER1_ASTERIX));
		assertThat(team.getPlayer2(), is(PLAYER2_OBELIX));
	}

}
