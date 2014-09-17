package de.andrena.ktv.core.repository;

import static de.andrena.ktv.core.fixtures.Fixtures.STANDARD_KEY;
import static de.andrena.ktv.core.fixtures.Fixtures.TEAM_NAME_GAUL;
import static de.andrena.ktv.core.fixtures.Fixtures.TEAM_NAME_ROMAN;
import static de.andrena.ktv.core.fixtures.Fixtures.standardTeamWithoutKey;
import static de.andrena.ktv.core.fixtures.Fixtures.standardTeamWithKey;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import de.andrena.ktv.core.domain.Team;
import de.andrena.ktv.core.repository.TeamsMemoryRepository;

public class TeamsMemoryRepositoryTest {
	TeamsMemoryRepository uut;

	@Before
	public void setUp() {
		this.uut = new TeamsMemoryRepository(Collections.singletonMap(STANDARD_KEY, standardTeamWithKey()));
	}

	@Test
	public void addASingleTeam() {
		assertThat(this.uut.findAll().size(), is(1));

		Team newTeam = standardTeamWithoutKey();
		Team savedTeam = this.uut.save(newTeam);

		assertThat(savedTeam, is(equalTo(newTeam)));
		assertThat(this.uut.findAll().size(), is(2));
	}

	@Test
	public void add10TeamsAndFindAll() throws Exception {
		for (int i = 0; i < 10; i++) {
			this.uut.save(standardTeamWithoutKey());
		}
		assertThat(this.uut.findAll().size(), is(11));
	}

	@Test
	public void findTeamByID() throws Exception {
		assertThat(this.uut.findById(standardTeamWithKey().getKey()), is(equalTo(standardTeamWithKey())));
	}

	@Test
	public void removeASingleTeam() throws Exception {
		assertThat(this.uut.findAll().size(), is(1));
		this.uut.delete(STANDARD_KEY);
		assertThat(this.uut.findAll().size(), is(0));
	}

	@Test
	public void updateTeam() throws Exception {
		assertThat(this.uut.findById(STANDARD_KEY).getName(), is(equalTo(TEAM_NAME_GAUL)));

		Team team = standardTeamWithKey();
		team.setName(TEAM_NAME_ROMAN);
		this.uut.updateTeam(team);
		assertThat(this.uut.findById(STANDARD_KEY).getName(), is(equalTo(TEAM_NAME_ROMAN)));
	}
}
