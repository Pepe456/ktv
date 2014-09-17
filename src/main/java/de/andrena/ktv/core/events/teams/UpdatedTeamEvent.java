package de.andrena.ktv.core.events.teams;

import java.util.UUID;

import de.andrena.ktv.core.domain.TeamDetails;
import de.andrena.ktv.core.events.UpdatedEvent;

public class UpdatedTeamEvent extends UpdatedEvent {
	private final UUID key;
	private final TeamDetails teamDetails;
	private boolean updateCompleted = false;

	public UpdatedTeamEvent(UUID key, TeamDetails teamDetails) {
		this.key = key;
		this.teamDetails = teamDetails;
		this.updateCompleted = true;
	}

	public UUID getKey() {
		return this.key;
	}

	public TeamDetails getTeamDetails() {
		return this.teamDetails;
	}

	public boolean isUpdateCompleted() {
		return this.updateCompleted;
	}

	public boolean isUpdateNotCompleted() {
		return !this.updateCompleted;
	}

	public static UpdatedTeamEvent updateFordbidden(UUID key, TeamDetails teamDetails) {
		UpdatedTeamEvent event = new UpdatedTeamEvent(key, teamDetails);
		event.entityFound = true;
		event.updateCompleted = false;
		return event;
	}

	public static UpdatedTeamEvent notFound(UUID key, TeamDetails teamDetails) {
		UpdatedTeamEvent event = new UpdatedTeamEvent(key, teamDetails);
		event.entityFound = false;
		event.updateCompleted = false;
		return event;
	}
}
