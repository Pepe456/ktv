package de.andrena.ktv.core.services;

import static de.andrena.ktv.core.fixtures.Fixtures.PLAYER1_ASTERIX;
import static de.andrena.ktv.core.fixtures.Fixtures.PLAYER2_OBELIX;
import static de.andrena.ktv.core.fixtures.Fixtures.STANDARD_KEY;
import static de.andrena.ktv.core.fixtures.Fixtures.TEAM_NAME_GAUL;
import static de.andrena.ktv.core.fixtures.Fixtures.standardTeamDetailsWithKey;
import static de.andrena.ktv.core.fixtures.Fixtures.standardTeamDetailsWithoutKey;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

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

public class TeamEventHandlerTest {
	@Mock
	private TeamPersistenceService persistenceService;

	private TeamService uut;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		this.uut = new TeamEventHandler(this.persistenceService);
	}

	@Test
	public void createTeam() {
		when(this.persistenceService.createTeam(Mockito.any(CreateTeamEvent.class)))//
				.thenReturn(new CreatedTeamEvent(STANDARD_KEY, standardTeamDetailsWithKey()));

		CreateTeamEvent createEvent = new CreateTeamEvent(standardTeamDetailsWithoutKey());
		CreatedTeamEvent createdEvent = this.uut.createTeam(createEvent);

		assertThat(createdEvent.getNewTeamKey(), is(STANDARD_KEY));
		assertThat(createdEvent.getNewTeamKey(), is(equalTo(STANDARD_KEY)));
		assertThat(createdEvent.getTeamDetails().getName(), is(equalTo(TEAM_NAME_GAUL)));
		assertThat(createdEvent.getTeamDetails().getPlayer1(), is(equalTo(PLAYER1_ASTERIX)));
		assertThat(createdEvent.getTeamDetails().getPlayer2(), is(equalTo(PLAYER2_OBELIX)));
	}

	@Test
	public void getAllTeams() {
		List<TeamDetails> teamsDetails = Arrays.asList(//
				standardTeamDetailsWithKey(UUID.randomUUID()),//
				standardTeamDetailsWithKey(UUID.randomUUID()),//
				standardTeamDetailsWithKey(UUID.randomUUID()));
		when(this.persistenceService.readAllTeams(Mockito.any(ReadAllTeamDetailsEvent.class)))//
				.thenReturn(new ReadedAllTeamDetailsEvent(teamsDetails));

		ReadedAllTeamDetailsEvent readedAllEvent = this.uut.readAllTeams(new ReadAllTeamDetailsEvent());
		assertThat(readedAllEvent.getTeamDetails().size(), is(3));
	}

	@Test
	public void getTeam() {
		when(this.persistenceService.readTeamDetails(Mockito.any(ReadTeamDetailsEvent.class)))//
				.thenReturn(new ReadedTeamDetailsEvent(STANDARD_KEY, standardTeamDetailsWithKey()));

		ReadedTeamDetailsEvent readedTeamDetails = this.uut.readTeamDetails(new ReadTeamDetailsEvent(STANDARD_KEY));
		assertThat(readedTeamDetails.getKey(), is(STANDARD_KEY));
		assertThat(readedTeamDetails.getTeamDetails().getKey(), is(STANDARD_KEY));
	}

	@Test
	public void updateTeam() {
		when(this.persistenceService.updateTeam(Mockito.any(UpdateTeamEvent.class)))//
				.thenReturn(new UpdatedTeamEvent(STANDARD_KEY, standardTeamDetailsWithKey()));
		UpdatedTeamEvent updatedEvent = this.uut.updateTeam(new UpdateTeamEvent(STANDARD_KEY, standardTeamDetailsWithKey()));

		assertThat(updatedEvent.getKey(), is(STANDARD_KEY));
		assertThat(updatedEvent.getTeamDetails().getKey(), is(STANDARD_KEY));
	}

	@Test
	public void deleteTeam() {
		when(this.persistenceService.deleteTeam(Mockito.any(DeleteTeamEvent.class)))//
				.thenReturn(new DeletedTeamEvent(STANDARD_KEY));
		DeletedTeamEvent deletedTeamEvent = this.uut.deleteTeam(new DeleteTeamEvent(STANDARD_KEY));
		assertThat(deletedTeamEvent.getKey(), is(STANDARD_KEY));
	}
}
