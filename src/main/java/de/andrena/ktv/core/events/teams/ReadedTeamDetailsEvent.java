package de.andrena.ktv.core.events.teams;

import java.util.UUID;

import de.andrena.ktv.core.domain.TeamDetails;
import de.andrena.ktv.core.events.ReadedEvent;

public class ReadedTeamDetailsEvent extends ReadedEvent {
	private final UUID key;
	private TeamDetails teamDetails;

	public ReadedTeamDetailsEvent(UUID key) {
		this.key = key;
	}

	public ReadedTeamDetailsEvent(UUID key, TeamDetails teamDetails) {
		this.key = key;
		this.teamDetails = teamDetails;
	}

	public TeamDetails getTeamDetails() {
		return this.teamDetails;
	}

	public UUID getKey() {
		return this.key;
	}

	public static ReadedTeamDetailsEvent notFound(UUID key) {
		ReadedTeamDetailsEvent ev = new ReadedTeamDetailsEvent(key);
		ev.entityFound = false;
		return ev;
	}
}
