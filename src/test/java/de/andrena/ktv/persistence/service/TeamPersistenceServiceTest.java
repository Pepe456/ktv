package de.andrena.ktv.persistence.service;

import static de.andrena.ktv.core.fixtures.Fixtures.STANDARD_KEY;
import static de.andrena.ktv.core.fixtures.Fixtures.STANDARD_KEY_STRING;
import static de.andrena.ktv.core.fixtures.Fixtures.standardRepositoryTeamWithKey;
import static de.andrena.ktv.core.fixtures.Fixtures.standardTeamDetailsWithKey;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.andrena.ktv.config.PersistenceConfig;
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
import de.andrena.ktv.persistence.domain.RepositoryTeam;
import de.andrena.ktv.persistence.repository.TeamsRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { PersistenceConfig.class })
public class TeamPersistenceServiceTest {

	@Mock
	TeamsRepository repository;

	private TeamPersistenceService uut;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		this.uut = new TeamPersistenceEventHandler(this.repository);
	}

	@Test
	public void thatCreateOrderWorks() {
		when(this.repository.save(Mockito.any(RepositoryTeam.class))).thenReturn(standardRepositoryTeamWithKey());
		CreatedTeamEvent createdTeamEvent = this.uut.createTeam(new CreateTeamEvent(standardTeamDetailsWithKey()));

		assertThat(createdTeamEvent.getTeamDetails(), is(standardTeamDetailsWithKey()));
	}

	@Test
	public void thatReadAllTeamsWorks() {
		List<RepositoryTeam> someTeams = Arrays.asList(//
				standardRepositoryTeamWithKey(UUID.randomUUID().toString()),//
				standardRepositoryTeamWithKey(UUID.randomUUID().toString()),//
				standardRepositoryTeamWithKey(UUID.randomUUID().toString()));
		when(this.repository.findAll()).thenReturn(someTeams);
		ReadedAllTeamDetailsEvent readedAllTeamDetailsEvent = this.uut.readAllTeams(new ReadAllTeamDetailsEvent());
		assertThat(readedAllTeamDetailsEvent.getTeamDetails().size(), is(someTeams.size()));
	}

	@Test
	public void thatReadTeamWorks() {
		when(this.repository.findOne(STANDARD_KEY_STRING)).thenReturn(standardRepositoryTeamWithKey());
		ReadedTeamDetailsEvent readedTeamDetailsEvent = this.uut.readTeamDetails(new ReadTeamDetailsEvent(STANDARD_KEY));
		assertThat(readedTeamDetailsEvent.getTeamDetails(), is(standardTeamDetailsWithKey()));
		assertThat(readedTeamDetailsEvent.isEntityFound(), is(true));
	}

	@Test
	public void thatReadTeamUsesNotFound() {
		when(this.repository.findOne(Mockito.any(String.class))).thenReturn(null);
		ReadedTeamDetailsEvent readedTeamDetailsEvent = this.uut.readTeamDetails(new ReadTeamDetailsEvent(STANDARD_KEY));
		assertThat(readedTeamDetailsEvent.isEntityFound(), is(false));
	}

	@Test
	public void thatUpdateTeamUsesNotFound() {
		when(this.repository.findOne(Mockito.any(String.class))).thenReturn(null);
		UpdatedTeamEvent updatedTeamDetailsEvent = this.uut.updateTeam(new UpdateTeamEvent(STANDARD_KEY, standardTeamDetailsWithKey()));
		assertThat(updatedTeamDetailsEvent.isEntityFound(), is(false));
	}

	@Test
	public void thatUpdateTeamWorks() {
		when(this.repository.findOne(STANDARD_KEY_STRING)).thenReturn(standardRepositoryTeamWithKey());
		when(this.repository.save(standardRepositoryTeamWithKey())).thenReturn(standardRepositoryTeamWithKey());

		UpdatedTeamEvent updatedTeamDetailsEvent = this.uut.updateTeam(new UpdateTeamEvent(STANDARD_KEY, standardTeamDetailsWithKey()));

		assertThat(updatedTeamDetailsEvent.isEntityFound(), is(true));
		Mockito.verify(this.repository).findOne(STANDARD_KEY_STRING);
		Mockito.verify(this.repository).save(standardRepositoryTeamWithKey());
		Mockito.verifyNoMoreInteractions(this.repository);
	}

	@Test
	public void thatDeleteTeamWorks() {
		when(this.repository.findOne(STANDARD_KEY_STRING)).thenReturn(standardRepositoryTeamWithKey());
		this.uut.deleteTeam(new DeleteTeamEvent(STANDARD_KEY));
		Mockito.verify(this.repository).delete(STANDARD_KEY_STRING);
	}

	@Test
	public void thatDeleteUsesNotFound() {
		DeletedTeamEvent deletedTeamEvent = this.uut.deleteTeam(new DeleteTeamEvent(STANDARD_KEY));
		assertThat(deletedTeamEvent.isEntityFound(), is(false));
		assertThat(deletedTeamEvent.getKey(), is(STANDARD_KEY));
		Mockito.verify(this.repository).findOne(STANDARD_KEY_STRING);
		Mockito.verifyNoMoreInteractions(this.repository);
	}
}
