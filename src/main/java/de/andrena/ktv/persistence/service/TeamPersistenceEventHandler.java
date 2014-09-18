package de.andrena.ktv.persistence.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
import de.andrena.ktv.persistence.domain.RepositoryTeam;
import de.andrena.ktv.persistence.repository.TeamsRepository;

public class TeamPersistenceEventHandler implements TeamPersistenceService {

	private final TeamsRepository teamsRepository;

	public TeamPersistenceEventHandler(TeamsRepository teamsRepository) {
		this.teamsRepository = teamsRepository;
	}

	@Override
	public CreatedTeamEvent createTeam(CreateTeamEvent event) {
		RepositoryTeam team = RepositoryTeam.fromTeamDetails(event.getTeamDetails());
		team = this.teamsRepository.save(team);
		return new CreatedTeamEvent(UUID.fromString(team.getId()), team.toTeamDetails());
	}

	@Override
	public ReadedAllTeamDetailsEvent readAllTeams(ReadAllTeamDetailsEvent readAllTeamDetailsEvent) {
		List<TeamDetails> teamsDetails = new ArrayList<>();
		for (RepositoryTeam team : this.teamsRepository.findAll()) {
			teamsDetails.add(team.toTeamDetails());
		}
		return new ReadedAllTeamDetailsEvent(teamsDetails);
	}

	@Override
	public ReadedTeamDetailsEvent readTeamDetails(ReadTeamDetailsEvent readTeamDetailsEvent) {
		RepositoryTeam team = this.teamsRepository.findOne(readTeamDetailsEvent.getKey().toString());

		if (team == null) {
			return ReadedTeamDetailsEvent.notFound(readTeamDetailsEvent.getKey());
		}

		return new ReadedTeamDetailsEvent(readTeamDetailsEvent.getKey(), team.toTeamDetails());
	}

	@Override
	public UpdatedTeamEvent updateTeam(UpdateTeamEvent event) {
		TeamDetails teamUpdate = event.getTeamDetails();
		RepositoryTeam team = this.teamsRepository.findOne(event.getKey().toString());

		if (team == null) {
			return UpdatedTeamEvent.notFound(event.getKey(), event.getTeamDetails());
		}

		this.updatePropertiesOfTeamWhenNotNull(teamUpdate, team);

		RepositoryTeam updatedTeam = this.teamsRepository.save(team);

		return new UpdatedTeamEvent(UUID.fromString(updatedTeam.getId()), updatedTeam.toTeamDetails());
	}

	private void updatePropertiesOfTeamWhenNotNull(TeamDetails teamUpdate, RepositoryTeam team) {
		if (teamUpdate.getName() != null) {
			team.setName(teamUpdate.getName());
		}

		if (teamUpdate.getPlayer1() != null) {
			team.setPlayer1(teamUpdate.getPlayer1());
		}

		if (teamUpdate.getPlayer2() != null) {
			team.setPlayer2(teamUpdate.getPlayer2());
		}
	}

	@Override
	public DeletedTeamEvent deleteTeam(DeleteTeamEvent deleteTeamEvent) {
		UUID key = deleteTeamEvent.getKey();
		RepositoryTeam team = null;
		if ((team = this.teamsRepository.findOne(key.toString())) == null) {
			return DeletedTeamEvent.notFound(deleteTeamEvent.getKey());
		}

		this.teamsRepository.delete(deleteTeamEvent.getKey().toString());

		if ((team = this.teamsRepository.findOne(key.toString())) != null) {
			return DeletedTeamEvent.deletionForbidden(key, team.toTeamDetails());
		}

		return new DeletedTeamEvent(deleteTeamEvent.getKey());
	}

}
