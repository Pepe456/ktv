package de.andrena.ktv.core.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.andrena.ktv.core.domain.Team;

public class TeamsMemoryRepository implements TeamsRepositoryInMemory {
	private Map<UUID, Team> teams;

	public TeamsMemoryRepository(final Map<UUID, Team> teams) {
		this.teams = Collections.unmodifiableMap(teams);
	}

	@Override
	public synchronized Team save(Team team) {

		UUID key = team.getKey();
		Map<UUID, Team> modifiableTeams = new HashMap<UUID, Team>(this.teams);
		modifiableTeams.put(key, team);
		this.teams = Collections.unmodifiableMap(modifiableTeams);
		return this.teams.get(key);
	}

	@Override
	public synchronized Team updateTeam(Team team) {

		UUID key = team.getKey();
		if (!this.teams.containsKey(key) || this.teams.get(key).cannotBeModified()) {
			return null;
		}

		Map<UUID, Team> modifiableTeams = new HashMap<UUID, Team>(this.teams);
		modifiableTeams.put(key, team);
		this.teams = Collections.unmodifiableMap(modifiableTeams);

		return this.teams.get(key);
	}

	@Override
	public void delete(UUID key) {
		if (this.teams.containsKey(key)) {
			Map<UUID, Team> modifiableOrders = new HashMap<UUID, Team>(this.teams);
			modifiableOrders.remove(key);
			this.teams = Collections.unmodifiableMap(modifiableOrders);
		}
	}

	@Override
	public Team findById(UUID key) {
		return this.teams.get(key);
	}

	@Override
	public List<Team> findAll() {
		return Collections.unmodifiableList(new ArrayList<Team>(this.teams.values()));
	}

}
