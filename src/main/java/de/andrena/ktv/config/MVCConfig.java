package de.andrena.ktv.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import de.andrena.ktv.rest.controller.TeamCommandsController;
import de.andrena.ktv.rest.controller.TeamQueriesController;

@Configuration
@EnableWebMvc
public class MVCConfig {

	@Bean
	TeamCommandsController teamCommandsController() {
		return new TeamCommandsController();
	}

	@Bean
	TeamQueriesController teamQueriesController() {
		return new TeamQueriesController();
	}
}
