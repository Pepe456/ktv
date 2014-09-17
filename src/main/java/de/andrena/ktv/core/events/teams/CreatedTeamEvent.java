package de.andrena.ktv.core.events.teams;

import java.util.UUID;

import de.andrena.ktv.core.domain.TeamDetails;
import de.andrena.ktv.core.events.CreatedEvent;

public class CreatedTeamEvent extends CreatedEvent {
	private final TeamDetails teamDetails;
	private final UUID newTeamKey;

	public CreatedTeamEvent(final UUID newTeamKey, final TeamDetails teamDetails) {
		this.newTeamKey = newTeamKey;
		this.teamDetails = teamDetails;
	}

	public UUID getNewTeamKey() {
		return this.newTeamKey;
	}

	public TeamDetails getTeamDetails() {
		return this.teamDetails;
	}
}
