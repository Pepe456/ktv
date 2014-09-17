package de.andrena.ktv.persistence.repository;

import static de.andrena.ktv.core.fixtures.Fixtures.PLAYER1_ASTERIX;
import static de.andrena.ktv.core.fixtures.Fixtures.PLAYER2_OBELIX;
import static de.andrena.ktv.core.fixtures.Fixtures.STANDARD_KEY_STRING;
import static de.andrena.ktv.core.fixtures.Fixtures.TEAM_NAME_GAUL;
import static de.andrena.ktv.core.fixtures.Fixtures.standardRepositoryTeamWithKey;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import de.andrena.ktv.config.PersistenceConfig;
import de.andrena.ktv.persistence.domain.RepositoryTeam;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { PersistenceConfig.class })
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class TeamsRepositoryTest {

	@Autowired
	TeamsRepository uut;
	private RepositoryTeam team;

	@Before
	public void setUp() throws Exception {
		this.team = standardRepositoryTeamWithKey();
	}

	@Test
	public void thatSavingTeamIncrementsTeamCount() throws Exception {
		assertThat(this.uut.count(), is(0L));
		this.uut.save(this.team);
		assertThat(this.uut.count(), is(1L));
	}

	@Test
	public void thatSavedTeamCanBeFound() throws Exception {
		this.uut.save(this.team);
		RepositoryTeam retrievedTeam = this.uut.findOne(STANDARD_KEY_STRING);
		assertThat(retrievedTeam, is(notNullValue()));
		assertThat(retrievedTeam.getId(), is(equalTo(STANDARD_KEY_STRING)));
		assertThat(retrievedTeam.getName(), is(equalTo(TEAM_NAME_GAUL)));
		assertThat(retrievedTeam.getPlayer1(), is(equalTo(PLAYER1_ASTERIX)));
		assertThat(retrievedTeam.getPlayer2(), is(equalTo(PLAYER2_OBELIX)));
	}

	@Test
	public void thatSavingTeamSavesCorrectData() throws Exception {
		RepositoryTeam retrievedTeam = this.uut.save(this.team);
		assertThat(retrievedTeam.getId(), is(STANDARD_KEY_STRING));
		assertThat(retrievedTeam.getName(), is(TEAM_NAME_GAUL));
		assertThat(retrievedTeam.getPlayer1(), is(PLAYER1_ASTERIX));
		assertThat(retrievedTeam.getPlayer2(), is(PLAYER2_OBELIX));
	}

	@Test
	public void thatFindByIdWorks() throws Exception {
		this.uut.save(this.team);
		RepositoryTeam retrievedTeam = this.uut.findOne(STANDARD_KEY_STRING);
		assertThat(retrievedTeam, is(equalTo(this.team)));
	}

	@Test
	public void thatATeamCanBeDeletedWithTeamAsParameter() throws Exception {
		this.uut.save(this.team);
		assertThat(this.uut.count(), is(1L));
		this.uut.delete(this.team);
		assertThat(this.uut.count(), is(0L));
	}

	@Test
	public void thatATeamCanBeDeletedWithKeyAsParameter() throws Exception {
		this.uut.save(this.team);
		assertThat(this.uut.count(), is(1L));
		this.uut.delete(STANDARD_KEY_STRING);
		assertThat(this.uut.count(), is(0L));
	}

	@Test
	public void thatCountWorks() throws Exception {
		for (int i = 0; i < 100; i++) {
			this.uut.save(standardRepositoryTeamWithKey(i + ""));
		}
		assertThat(this.uut.count(), is(100L));
	}

	@Test
	public void thatTeamExistsWork() throws Exception {
		this.uut.save(this.team);
		assertThat(this.uut.exists(STANDARD_KEY_STRING), is(true));
	}

	@Test
	public void thatFindsAllFindsAll() throws Exception {
		for (int i = 0; i < 100; i++) {
			this.uut.save(standardRepositoryTeamWithKey(i + ""));
		}

		assertThat(this.getSize(this.uut.findAll()), is(100));

	}

	@SuppressWarnings("unused")
	private int getSize(Iterable<RepositoryTeam> teams) {
		int counter = 0;
		for (RepositoryTeam repositoryTeam : teams) {
			counter++;
		}
		return counter;
	}
}
