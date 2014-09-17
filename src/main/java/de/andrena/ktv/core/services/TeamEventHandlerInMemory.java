package de.andrena.ktv.core.services;

import java.util.ArrayList;
import java.util.List;

import de.andrena.ktv.core.domain.Team;
import de.andrena.ktv.core.domain.TeamDetails;
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
import de.andrena.ktv.core.repository.TeamsRepositoryInMemory;

public class TeamEventHandlerInMemory implements TeamService {

	private final TeamsRepositoryInMemory teamsRepository;

	public TeamEventHandlerInMemory(TeamsRepositoryInMemory teamsRepository) {
		this.teamsRepository = teamsRepository;
	}

	@Override
	public CreatedTeamEvent createTeam(CreateTeamEvent createTeameEvent) {

		Team newTeam = Team.fromTeamDetailsWithNewRandomKey(createTeameEvent.getTeamDetails());
		Team createdTeam = this.teamsRepository.save(newTeam);
		return new CreatedTeamEvent(createdTeam.getKey(), createdTeam.toTeamDetails());
	}

	@Override
	public UpdatedTeamEvent updateTeam(UpdateTeamEvent event) {

		Team team = this.teamsRepository.findById(event.getKey());
		if (team == null) {
			return UpdatedTeamEvent.notFound(event.getKey(), event.getTeamDetails());
		}

		Team teamUpdate = Team.fromTeamDetails(event.getTeamDetails());
		Team teamUpdated = this.teamsRepository.updateTeam(teamUpdate);

		if (teamUpdated == null) {
			return UpdatedTeamEvent.updateFordbidden(event.getKey(), event.getTeamDetails());
		}

		return new UpdatedTeamEvent(teamUpdated.getKey(), teamUpdated.toTeamDetails());
	}

	@Override
	public ReadedAllTeamDetailsEvent readAllTeams(ReadAllTeamDetailsEvent readAllTeamDetailsEvent) {
		List<TeamDetails> result = new ArrayList<>();
		for (Team team : this.teamsRepository.findAll()) {
			result.add(team.toTeamDetails());
		}
		return new ReadedAllTeamDetailsEvent(result);
	}

	@Override
	public ReadedTeamDetailsEvent readTeamDetails(ReadTeamDetailsEvent readTeamDetailsEvent) {
		Team team = this.teamsRepository.findById(readTeamDetailsEvent.getKey());
		if (team == null) {
			return ReadedTeamDetailsEvent.notFound(readTeamDetailsEvent.getKey());
		}
		return new ReadedTeamDetailsEvent(readTeamDetailsEvent.getKey(), team.toTeamDetails());
	}

	@Override
	public DeletedTeamEvent deleteTeam(DeleteTeamEvent deleteTeamEvent) {
		Team team = this.teamsRepository.findById(deleteTeamEvent.getKey());

		if (team == null) {
			return DeletedTeamEvent.notFound(deleteTeamEvent.getKey());
		}

		TeamDetails teamDetails = team.toTeamDetails();

		if (team.cannotBeDeleted()) {
			return DeletedTeamEvent.deletionForbidden(deleteTeamEvent.getKey(), teamDetails);
		}

		this.teamsRepository.delete(deleteTeamEvent.getKey());

		return new DeletedTeamEvent(deleteTeamEvent.getKey(), teamDetails);
	}

}
