package de.andrena.ktv.config;

import java.util.HashMap;
import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.andrena.ktv.core.domain.Team;
import de.andrena.ktv.core.repository.TeamsMemoryRepository;
import de.andrena.ktv.core.repository.TeamsRepositoryInMemory;
import de.andrena.ktv.core.services.TeamEventHandlerInMemory;
import de.andrena.ktv.core.services.TeamService;

@Configuration
public class CoreInMemoryConfig {

	@Bean
	public TeamService createService(TeamsRepositoryInMemory repository) {
		return new TeamEventHandlerInMemory(repository);
	}

	@Bean
	public TeamsRepositoryInMemory createRepository() {
		return new TeamsMemoryRepository(new HashMap<UUID, Team>());
	}
}
