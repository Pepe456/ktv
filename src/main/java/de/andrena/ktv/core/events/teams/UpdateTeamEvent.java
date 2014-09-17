package de.andrena.ktv.core.events.teams;

import java.util.UUID;

import de.andrena.ktv.core.domain.TeamDetails;
import de.andrena.ktv.core.events.UpdateEvent;

public class UpdateTeamEvent extends UpdateEvent {
	private final UUID key;
	private final TeamDetails teamDetails;

	public UpdateTeamEvent(UUID key, TeamDetails teamDetails) {
		this.key = key;
		this.teamDetails = teamDetails;
	}

	public UUID getKey() {
		return this.key;
	}

	public TeamDetails getTeamDetails() {
		return this.teamDetails;
	}
}
