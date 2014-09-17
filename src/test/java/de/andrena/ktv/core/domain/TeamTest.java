package de.andrena.ktv.core.domain;

import static de.andrena.ktv.core.fixtures.Fixtures.PLAYER2_OBELIX;
import static de.andrena.ktv.core.fixtures.Fixtures.PLAYER1_ASTERIX;
import static de.andrena.ktv.core.fixtures.Fixtures.STANDARD_KEY;
import static de.andrena.ktv.core.fixtures.Fixtures.TEAM_NAME_GAUL;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.junit.Test;

public class TeamTest {

	@Test
	public void thatNewTeamHasCorrectValues() throws Exception {
		Team team = new Team(TEAM_NAME_GAUL, PLAYER2_OBELIX, PLAYER1_ASTERIX);
		assertThat(team.getName(), is(equalTo(TEAM_NAME_GAUL)));
		assertThat(team.getPlayer1(), is(equalTo(PLAYER2_OBELIX)));
		assertThat(team.getPlayer2(), is(equalTo(PLAYER1_ASTERIX)));
		assertThat(team.cannotBeDeleted(), is(false));
		assertThat(team.cannotBeModified(), is(false));
	}

	@Test
	public void that10NewTeamsHaveDifferentKeys() throws Exception {
		Set<UUID> keys = new HashSet<>();
		for (int i = 0; i < 10; i++) {
			keys.add(new Team("", "", "").getKey());
		}
		assertThat(keys.size(), is(10));
	}

	@Test
	public void thatToTeamDetailsConvertsCorrectly() throws Exception {
		Team team = new Team(TEAM_NAME_GAUL, PLAYER2_OBELIX, PLAYER1_ASTERIX);
		TeamDetails teamDetails = team.toTeamDetails();
		assertThat(teamDetails.getKey(), is(team.getKey()));
		assertThat(teamDetails.getName(), is(equalTo(TEAM_NAME_GAUL)));
		assertThat(teamDetails.getPlayer1(), is(equalTo(PLAYER2_OBELIX)));
		assertThat(teamDetails.getPlayer2(), is(equalTo(PLAYER1_ASTERIX)));
	}

	@Test
	public void thatFromTeamDetailsWithKeyCreatesCorrectTeamWithKey() throws Exception {
		TeamDetails teamDetails = new TeamDetails();
		teamDetails.setKey(STANDARD_KEY);
		teamDetails.setName(TEAM_NAME_GAUL);
		teamDetails.setPlayer1(PLAYER2_OBELIX);
		teamDetails.setPlayer2(PLAYER1_ASTERIX);

		Team team = Team.fromTeamDetails(teamDetails);

		assertThat(team.getKey(), is(equalTo(STANDARD_KEY)));
		assertThat(team.getName(), is(equalTo(TEAM_NAME_GAUL)));
		assertThat(team.getPlayer1(), is(equalTo(PLAYER2_OBELIX)));
		assertThat(team.getPlayer2(), is(equalTo(PLAYER1_ASTERIX)));
	}

	@Test
	public void thatFromTeamDetailsWithoutKeyCreatesCorrectTeamWithNewKey() throws Exception {
		TeamDetails teamDetails = new TeamDetails();
		teamDetails.setName(TEAM_NAME_GAUL);
		teamDetails.setPlayer1(PLAYER2_OBELIX);
		teamDetails.setPlayer2(PLAYER1_ASTERIX);

		Team team = Team.fromTeamDetails(teamDetails);

		assertThat(teamDetails.getKey(), is(nullValue()));
		assertThat(team.getKey(), is(instanceOf(UUID.class)));
		assertThat(team.getName(), is(equalTo(TEAM_NAME_GAUL)));
		assertThat(team.getPlayer1(), is(equalTo(PLAYER2_OBELIX)));
		assertThat(team.getPlayer2(), is(equalTo(PLAYER1_ASTERIX)));
	}

}
