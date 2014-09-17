package de.andrena.ktv.core.repository;

import java.util.List;
import java.util.UUID;

import de.andrena.ktv.core.domain.Team;

public interface TeamsRepositoryInMemory {

	Team save(Team team);

	Team updateTeam(Team team);

	void delete(UUID key);

	Team findById(UUID key);

	List<Team> findAll();
}
