package de.andrena.ktv.persistence.integration;

import static de.andrena.ktv.core.fixtures.Fixtures.STANDARD_KEY;
import static de.andrena.ktv.core.fixtures.Fixtures.TEAM_NAME_GAUL;
import static de.andrena.ktv.core.fixtures.Fixtures.TEAM_NAME_ROMAN;
import static de.andrena.ktv.core.fixtures.Fixtures.standardTeamDetailsWithKey;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.UUID;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import de.andrena.ktv.config.PersistenceConfig;
import de.andrena.ktv.core.domain.TeamDetails;
import de.andrena.ktv.core.events.teams.CreateTeamEvent;
import de.andrena.ktv.core.events.teams.CreatedTeamEvent;
import de.andrena.ktv.core.events.teams.DeleteTeamEvent;
import de.andrena.ktv.core.events.teams.DeletedTeamEvent;
import de.andrena.ktv.core.events.teams.ReadAllTeamDetailsEvent;
import de.andrena.ktv.core.events.teams.ReadTeamDetailsEvent;
import de.andrena.ktv.core.events.teams.ReadedAllTeamDetailsEvent;
import de.andrena.ktv.core.events.teams.ReadedTeamDetailsEvent;
import de.andrena.ktv.core.events.teams.UpdateTeamEvent;
import de.andrena.ktv.core.events.teams.UpdatedTeamEvent;
import de.andrena.ktv.persistence.service.TeamPersistenceService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { PersistenceConfig.class })
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class ServiceAndRepositoryTest {

	@Autowired
	EntityManager entityManager;

	@Autowired
	private TeamPersistenceService teamPersistenceService;

	@Before
	public void setUp() {
	}

	@Test
	public void thatCreateOrderWorks() {
		CreatedTeamEvent createdTeamEvent = this.teamPersistenceService.createTeam(new CreateTeamEvent(standardTeamDetailsWithKey()));

		assertThat(createdTeamEvent.getTeamDetails(), is(standardTeamDetailsWithKey()));
	}

	@Test
	public void thatReadAllTeamsWorks() {
		for (int i = 0; i < 3; i++) {
			this.teamPersistenceService.createTeam(new CreateTeamEvent(standardTeamDetailsWithKey(UUID.randomUUID())));
		}

		ReadedAllTeamDetailsEvent readedAllTeamDetailsEvent = this.teamPersistenceService.readAllTeams(new ReadAllTeamDetailsEvent());
		assertThat(readedAllTeamDetailsEvent.getTeamDetails().size(), is(3));
	}

	@Test
	public void thatReadTeamWorks() {
		this.teamPersistenceService.createTeam(new CreateTeamEvent(standardTeamDetailsWithKey()));

		ReadedTeamDetailsEvent readedTeamDetailsEvent = this.teamPersistenceService.readTeamDetails(new ReadTeamDetailsEvent(STANDARD_KEY));
		assertThat(readedTeamDetailsEvent.getTeamDetails(), is(standardTeamDetailsWithKey()));
		assertThat(readedTeamDetailsEvent.isEntityFound(), is(true));
	}

	@Test
	public void thatReadTeamUsesNotFound() {
		ReadedTeamDetailsEvent readedTeamDetailsEvent = this.teamPersistenceService.readTeamDetails(new ReadTeamDetailsEvent(UUID.randomUUID()));
		assertThat(readedTeamDetailsEvent.isEntityFound(), is(false));
	}

	@Test
	public void thatUpdateTeamWorks() {
		TeamDetails teamDetails = standardTeamDetailsWithKey();
		this.teamPersistenceService.createTeam(new CreateTeamEvent(teamDetails));

		teamDetails = this.teamPersistenceService.createTeam(new CreateTeamEvent(teamDetails)).getTeamDetails();
		assertThat(teamDetails.getName(), is(TEAM_NAME_GAUL));

		teamDetails.setName(TEAM_NAME_ROMAN);
		this.teamPersistenceService.updateTeam(new UpdateTeamEvent(teamDetails.getKey(), teamDetails));

		teamDetails = this.teamPersistenceService.readTeamDetails(new ReadTeamDetailsEvent(teamDetails.getKey())).getTeamDetails();
		assertThat(teamDetails.getName(), is(TEAM_NAME_ROMAN));

	}

	@Test
	public void thatUpdateTeamUsesNotFound() {
		UpdateTeamEvent event = new UpdateTeamEvent(STANDARD_KEY, standardTeamDetailsWithKey());
		UpdatedTeamEvent updatedTeamDetailsEvent = this.teamPersistenceService.updateTeam(event);
		assertThat(updatedTeamDetailsEvent.isEntityFound(), is(false));
	}

	@Test
	public void thatDeleteTeamWorks() {
		this.teamPersistenceService.createTeam(new CreateTeamEvent(standardTeamDetailsWithKey()));
		ReadedAllTeamDetailsEvent before = this.teamPersistenceService.readAllTeams(new ReadAllTeamDetailsEvent());
		assertThat(before.getTeamDetails().size(), is(1));

		this.teamPersistenceService.deleteTeam(new DeleteTeamEvent(STANDARD_KEY));

		ReadedAllTeamDetailsEvent after = this.teamPersistenceService.readAllTeams(new ReadAllTeamDetailsEvent());
		assertThat(after.getTeamDetails().size(), is(0));
	}

	@Test
	public void thatDeleteUsesNotFound() {
		DeletedTeamEvent deletedTeamEvent = this.teamPersistenceService.deleteTeam(new DeleteTeamEvent(STANDARD_KEY));
		assertThat(deletedTeamEvent.isEntityFound(), is(false));
		assertThat(deletedTeamEvent.getKey(), is(STANDARD_KEY));
	}
}
