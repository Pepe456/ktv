package de.andrena.ktv.config;

import static de.andrena.ktv.core.fixtures.Fixtures.PLAYER1_ASTERIX;
import static de.andrena.ktv.core.fixtures.Fixtures.PLAYER2_OBELIX;
import static de.andrena.ktv.core.fixtures.Fixtures.TEAM_NAME_GAUL;
import static de.andrena.ktv.core.fixtures.Fixtures.TEAM_NAME_ROMAN;
import static de.andrena.ktv.core.fixtures.Fixtures.standardTeamDetailsWithoutKey;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.andrena.ktv.core.domain.TeamDetails;
import de.andrena.ktv.core.events.teams.CreateTeamEvent;
import de.andrena.ktv.core.events.teams.CreatedTeamEvent;
import de.andrena.ktv.core.events.teams.DeleteTeamEvent;
import de.andrena.ktv.core.events.teams.ReadAllTeamDetailsEvent;
import de.andrena.ktv.core.events.teams.ReadTeamDetailsEvent;
import de.andrena.ktv.core.events.teams.ReadedTeamDetailsEvent;
import de.andrena.ktv.core.events.teams.UpdateTeamEvent;
import de.andrena.ktv.core.services.TeamService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CoreConfig.class, PersistenceConfig.class })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class CoreIntegrationTest {

	@Autowired
	TeamService teamService;

	@DirtiesContext
	@Test
	public void addNewTeamToTheSystem() throws Exception {
		assertThat(this.teamService.readAllTeams(new ReadAllTeamDetailsEvent()).getTeamDetails().size(), is(equalTo(0)));
		CreateTeamEvent createTeamEvent = new CreateTeamEvent(standardTeamDetailsWithoutKey());
		CreatedTeamEvent createdTeamEvent = this.teamService.createTeam(createTeamEvent);

		assertThat(createdTeamEvent.getTeamDetails().getName(), is(equalTo(TEAM_NAME_GAUL)));
		assertThat(createdTeamEvent.getTeamDetails().getKey(), is(notNullValue()));
		assertThat(createdTeamEvent.getTeamDetails().getKey(), is(equalTo(createdTeamEvent.getNewTeamKey())));
		assertThat(this.teamService.readAllTeams(new ReadAllTeamDetailsEvent()).getTeamDetails().size(), is(equalTo(1)));
	}

	@DirtiesContext
	@Test
	public void updateTeamInTheSystem() throws Exception {
		TeamDetails teamDetails = standardTeamDetailsWithoutKey();

		CreateTeamEvent createTeamEvent = new CreateTeamEvent(teamDetails);
		CreatedTeamEvent createdTeamEvent = this.teamService.createTeam(createTeamEvent);
		assertThat(createdTeamEvent.getTeamDetails().getName(), is(equalTo(TEAM_NAME_GAUL)));

		ReadTeamDetailsEvent readTeamDetailsEvent = new ReadTeamDetailsEvent(createdTeamEvent.getNewTeamKey());
		ReadedTeamDetailsEvent readedTeamDetailsEvent = this.teamService.readTeamDetails(readTeamDetailsEvent);
		assertThat(readedTeamDetailsEvent.getTeamDetails().getName(), is(equalTo(TEAM_NAME_GAUL)));

		teamDetails.setName(TEAM_NAME_ROMAN);

		UpdateTeamEvent updateTeamEvent = new UpdateTeamEvent(teamDetails.getKey(), teamDetails);
		teamDetails = this.teamService.updateTeam(updateTeamEvent).getTeamDetails();
		assertThat(teamDetails.getName(), is(equalTo(TEAM_NAME_ROMAN)));
	}

	@DirtiesContext
	@Test
	public void addTeamToTheSystemAndReadIt() throws Exception {
		CreateTeamEvent createTeamEvent = new CreateTeamEvent(standardTeamDetailsWithoutKey());
		CreatedTeamEvent createdTeamEvent = this.teamService.createTeam(createTeamEvent);

		ReadTeamDetailsEvent readTeamDetailsEvent = new ReadTeamDetailsEvent(createdTeamEvent.getNewTeamKey());
		ReadedTeamDetailsEvent readedTeamDetailsEvent = this.teamService.readTeamDetails(readTeamDetailsEvent);
		TeamDetails teamDetails = readedTeamDetailsEvent.getTeamDetails();

		assertThat(teamDetails.getKey(), is(equalTo(createdTeamEvent.getNewTeamKey())));
		assertThat(teamDetails.getName(), is(equalTo(TEAM_NAME_GAUL)));
		assertThat(teamDetails.getPlayer1(), is(equalTo(PLAYER1_ASTERIX)));
		assertThat(teamDetails.getPlayer2(), is(equalTo(PLAYER2_OBELIX)));
	}

	@DirtiesContext
	@Test
	public void add10TeamToTheSystemAndReadThem() throws Exception {
		assertThat(this.teamService.readAllTeams(new ReadAllTeamDetailsEvent()).getTeamDetails().size(), is(equalTo(0)));
		for (int i = 0; i < 10; i++) {
			CreateTeamEvent createTeamEvent = new CreateTeamEvent(standardTeamDetailsWithoutKey());
			this.teamService.createTeam(createTeamEvent);
		}
		assertThat(this.teamService.readAllTeams(new ReadAllTeamDetailsEvent()).getTeamDetails().size(), is(equalTo(10)));
	}

	@DirtiesContext
	@Test
	public void addTeamAndDeleteItAgain() throws Exception {
		assertThat(this.teamService.readAllTeams(new ReadAllTeamDetailsEvent()).getTeamDetails().size(), is(equalTo(0)));

		CreateTeamEvent createTeamEvent = new CreateTeamEvent(standardTeamDetailsWithoutKey());
		CreatedTeamEvent createdTeamEvent = this.teamService.createTeam(createTeamEvent);
		assertThat(this.teamService.readAllTeams(new ReadAllTeamDetailsEvent()).getTeamDetails().size(), is(equalTo(1)));

		DeleteTeamEvent deleteTeamEvent = new DeleteTeamEvent(createdTeamEvent.getNewTeamKey());
		this.teamService.deleteTeam(deleteTeamEvent);
		assertThat(this.teamService.readAllTeams(new ReadAllTeamDetailsEvent()).getTeamDetails().size(), is(equalTo(0)));
	}
}
