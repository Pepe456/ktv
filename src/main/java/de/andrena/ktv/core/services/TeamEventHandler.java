package de.andrena.ktv.core.services;

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

public class TeamEventHandler implements TeamService {

	private final TeamPersistenceService persistenceService;

	public TeamEventHandler(TeamPersistenceService teamPersistenceService) {
		this.persistenceService = teamPersistenceService;
	}

	@Override
	public CreatedTeamEvent createTeam(CreateTeamEvent event) {
		return this.persistenceService.createTeam(event);
	}

	@Override
	public UpdatedTeamEvent updateTeam(UpdateTeamEvent event) {
		return this.persistenceService.updateTeam(event);
	}

	@Override
	public ReadedAllTeamDetailsEvent readAllTeams(ReadAllTeamDetailsEvent readAllTeamDetailsEvent) {
		return this.persistenceService.readAllTeams(readAllTeamDetailsEvent);
	}

	@Override
	public ReadedTeamDetailsEvent readTeamDetails(ReadTeamDetailsEvent readTeamDetailsEvent) {
		return this.persistenceService.readTeamDetails(readTeamDetailsEvent);
	}

	@Override
	public DeletedTeamEvent deleteTeam(DeleteTeamEvent deleteTeamEvent) {
		return this.persistenceService.deleteTeam(deleteTeamEvent);
	}

}
