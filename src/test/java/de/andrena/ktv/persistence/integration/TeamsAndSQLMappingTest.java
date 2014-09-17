package de.andrena.ktv.persistence.integration;

import static de.andrena.ktv.persistence.domain.fixture.JPAAssertions.JPAAssertions.assertTableExists;
import static de.andrena.ktv.persistence.domain.fixture.JPAAssertions.JPAAssertions.assertTableHasColumn;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import de.andrena.ktv.config.PersistenceConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { PersistenceConfig.class })
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class TeamsAndSQLMappingTest {

	@Autowired
	EntityManager entityManager;

	@Test
	public void testName() throws Exception {
		assertTableExists(this.entityManager, "TEAMS");
		assertTableHasColumn(this.entityManager, "TEAMS", "TEAM_ID");
		assertTableHasColumn(this.entityManager, "TEAMS", "NAME");
		assertTableHasColumn(this.entityManager, "TEAMS", "PLAYER_1");
		assertTableHasColumn(this.entityManager, "TEAMS", "PLAYER_2");
	}
}
