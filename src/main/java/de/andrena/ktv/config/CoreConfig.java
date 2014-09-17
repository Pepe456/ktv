package de.andrena.ktv.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.andrena.ktv.core.services.TeamEventHandler;
import de.andrena.ktv.core.services.TeamService;
import de.andrena.ktv.persistence.service.TeamPersistenceService;

@Configuration
public class CoreConfig {

	@Bean
	public TeamService teamService(TeamPersistenceService teamPersistenceService) {
		return new TeamEventHandler(teamPersistenceService);
	}
}
