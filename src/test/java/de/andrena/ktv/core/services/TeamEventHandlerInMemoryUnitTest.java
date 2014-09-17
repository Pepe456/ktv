package de.andrena.ktv.core.services;

import static de.andrena.ktv.core.fixtures.Fixtures.STANDARD_KEY;
import static de.andrena.ktv.core.fixtures.Fixtures.standardTeamWithoutKey;
import static de.andrena.ktv.core.fixtures.Fixtures.standardTeamDetailsWithKey;
import static de.andrena.ktv.core.fixtures.Fixtures.standardTeamWithKey;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.andrena.ktv.core.domain.Team;
import de.andrena.ktv.core.domain.TeamDetails;
import de.andrena.ktv.core.events.teams.CreateTeamEvent;
import de.andrena.ktv.core.events.teams.DeleteTeamEvent;
import de.andrena.ktv.core.events.teams.DeletedTeamEvent;
import de.andrena.ktv.core.events.teams.ReadAllTeamDetailsEvent;
import de.andrena.ktv.core.events.teams.ReadTeamDetailsEvent;
import de.andrena.ktv.core.events.teams.ReadedAllTeamDetailsEvent;
import de.andrena.ktv.core.events.teams.ReadedTeamDetailsEvent;
import de.andrena.ktv.core.events.teams.UpdateTeamEvent;
import de.andrena.ktv.core.events.teams.UpdatedTeamEvent;
import de.andrena.ktv.core.fixtures.Fixtures;
import de.andrena.ktv.core.repository.TeamsMemoryRepository;
import de.andrena.ktv.core.services.TeamEventHandlerInMemory;

public class TeamEventHandlerInMemoryUnitTest {
	private TeamEventHandlerInMemory uut;

	@Mock
	private final TeamsMemoryRepository teamsRepository = new TeamsMemoryRepository(new HashMap<UUID, Team>());

	@Before
	public void setUpUnitUnderTest() {
		MockitoAnnotations.initMocks(this);
		this.uut = new TeamEventHandlerInMemory(this.teamsRepository);
	}

	@Test
	public void addNewTeamToTheSystem() {
		when(this.teamsRepository.save(any(Team.class))).thenReturn(Fixtures.standardTeamWithoutKey());
		CreateTeamEvent event = new CreateTeamEvent(new TeamDetails());
		this.uut.createTeam(event);
		verify(this.teamsRepository).save(any(Team.class));
		verifyNoMoreInteractions(this.teamsRepository);
	}

	@Test
	public void addTwoNewTeamsToTheSystem() {

		when(this.teamsRepository.save(any(Team.class))).thenReturn(Fixtures.standardTeamWithoutKey());

		CreateTeamEvent event = new CreateTeamEvent(new TeamDetails());
		this.uut.createTeam(event);
		this.uut.createTeam(event);

		verify(this.teamsRepository, times(2)).save(any(Team.class));
		verifyNoMoreInteractions(this.teamsRepository);
	}

	@Test
	public void removeATeamFromTheSystemFailsIfNotPresent() {
		UUID key = UUID.randomUUID();
		when(this.teamsRepository.findById(key)).thenReturn(null);

		DeleteTeamEvent deleteTeamEvent = new DeleteTeamEvent(key);
		DeletedTeamEvent teamDeletedEvent = this.uut.deleteTeam(deleteTeamEvent);

		verify(this.teamsRepository, never()).delete(any(UUID.class));

		assertThat(teamDeletedEvent.isEntityFound(), is(false));
		assertThat(teamDeletedEvent.isDeletionCompleted(), is(false));
	}

	@Test
	public void removeATeamFromTheSystemFailsIfNotPermitted() {
		UUID key = UUID.randomUUID();
		Team team = Fixtures.standardTeamWithoutKey();
		team.setCanBeDeleted(false);

		when(this.teamsRepository.findById(key)).thenReturn(team);

		DeleteTeamEvent deleteTeamEvent = new DeleteTeamEvent(key);
		DeletedTeamEvent teamDeletedEvent = this.uut.deleteTeam(deleteTeamEvent);

		verify(this.teamsRepository, never()).delete(deleteTeamEvent.getKey());

		assertThat(teamDeletedEvent.isEntityFound(), is(true));
		assertThat(teamDeletedEvent.isDeletionCompleted(), is(false));
		assertThat(teamDeletedEvent.getTeamDetails().getName(), is(equalTo(team.getName())));
	}

	@Test
	public void removeATeamFromTheSystemWorksIfExists() {
		UUID key = UUID.randomUUID();
		Team team = Fixtures.standardTeamWithoutKey();

		when(this.teamsRepository.findById(key)).thenReturn(team);

		DeleteTeamEvent deleteTeamEvent = new DeleteTeamEvent(key);

		DeletedTeamEvent teamDeletedEvent = this.uut.deleteTeam(deleteTeamEvent);

		verify(this.teamsRepository).delete(deleteTeamEvent.getKey());

		assertThat(teamDeletedEvent.isEntityFound(), is(true));
		assertThat(teamDeletedEvent.isDeletionCompleted(), is(true));
		assertThat(teamDeletedEvent.getTeamDetails().getName(), is(equalTo(team.getName())));
	}

	@Test
	public void updateNameWorksIfExists() {
		Team team = Fixtures.standardTeamWithKey();
		when(this.teamsRepository.findById((any(UUID.class)))).thenReturn(team);
		when(this.teamsRepository.updateTeam(any(Team.class))).thenReturn(team);

		UpdateTeamEvent updateTeamEvent = new UpdateTeamEvent(team.getKey(), team.toTeamDetails());
		UpdatedTeamEvent updatedTeamEvent = this.uut.updateTeam(updateTeamEvent);

		verify(this.teamsRepository).updateTeam(any(Team.class));
		assertThat(updatedTeamEvent.isUpdateCompleted(), is(true));
		assertThat(updatedTeamEvent.getTeamDetails().getName(), is(equalTo(team.getName())));
	}

	@Test
	public void updateNameReturnsNotFoundIfNotExists() {
		UUID key = UUID.randomUUID();
		when(this.teamsRepository.findById(key)).thenReturn(null);

		UpdateTeamEvent updateTeamEvent = new UpdateTeamEvent(key, standardTeamDetailsWithKey(key));
		UpdatedTeamEvent teamupdatedEvent = this.uut.updateTeam(updateTeamEvent);

		verify(this.teamsRepository, never()).updateTeam(any(Team.class));
		assertThat(teamupdatedEvent.isEntityFound(), is(false));
		assertThat(teamupdatedEvent.isUpdateCompleted(), is(false));
	}

	@Test
	public void updateNameReturnsForbiddenIfNotModified() {
		Team team = standardTeamWithKey();
		team.setCanBeModified(false);
		when(this.teamsRepository.findById(STANDARD_KEY)).thenReturn(team);

		UpdateTeamEvent updateTeamEvent = new UpdateTeamEvent(STANDARD_KEY, standardTeamDetailsWithKey());
		UpdatedTeamEvent teamupdatedEvent = this.uut.updateTeam(updateTeamEvent);

		verify(this.teamsRepository).updateTeam(any(Team.class));
		assertThat(teamupdatedEvent.isEntityFound(), is(true));
		assertThat(teamupdatedEvent.isUpdateCompleted(), is(false));
	}

	@Test
	public void readTeam() {
		when(this.teamsRepository.findById(STANDARD_KEY)).thenReturn(standardTeamWithKey());

		ReadTeamDetailsEvent readTeamDetailsEvent = new ReadTeamDetailsEvent(STANDARD_KEY);
		ReadedTeamDetailsEvent readedTeamDetailsEvent = this.uut.readTeamDetails(readTeamDetailsEvent);

		verify(this.teamsRepository).findById(STANDARD_KEY);
		assertThat(readedTeamDetailsEvent.getTeamDetails(), is(equalTo(standardTeamDetailsWithKey())));
		assertThat(readedTeamDetailsEvent.getKey(), is(Fixtures.STANDARD_KEY));
		assertThat(readedTeamDetailsEvent.isEntityFound(), is(true));
	}

	@Test
	public void readAllTeams() {
		when(this.teamsRepository.findAll()).thenReturn(Arrays.asList(standardTeamWithoutKey(), standardTeamWithoutKey(), standardTeamWithoutKey()));
		ReadedAllTeamDetailsEvent readedAllTeamDetailsEvent = this.uut.readAllTeams(new ReadAllTeamDetailsEvent());

		verify(this.teamsRepository).findAll();
		assertThat(readedAllTeamDetailsEvent.isEntityFound(), is(true));
		assertThat(readedAllTeamDetailsEvent.getTeamDetails().size(), is(3));
	}

}
