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

public interface TeamService {
	public CreatedTeamEvent createTeam(CreateTeamEvent event);

	public UpdatedTeamEvent updateTeam(UpdateTeamEvent event);

	public ReadedAllTeamDetailsEvent readAllTeams(ReadAllTeamDetailsEvent readAllTeamDetailsEvent);

	public ReadedTeamDetailsEvent readTeamDetails(ReadTeamDetailsEvent readTeamDetailsEvent);

	public DeletedTeamEvent deleteTeam(DeleteTeamEvent deleteTeamEvent);
}
