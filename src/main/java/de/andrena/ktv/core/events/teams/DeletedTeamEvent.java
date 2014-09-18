package de.andrena.ktv.core.events.teams;

import java.util.UUID;

import de.andrena.ktv.core.domain.TeamDetails;
import de.andrena.ktv.core.events.DeletedEvent;

public class DeletedTeamEvent extends DeletedEvent {
	private final UUID key;
	private TeamDetails teamDetails;
	private boolean deletionCompleted;

	public DeletedTeamEvent(UUID key) {
		this.key = key;
		this.deletionCompleted = true;
	}

	public DeletedTeamEvent(UUID key, TeamDetails teamDetails) {
		this.key = key;
		this.teamDetails = teamDetails;
		this.deletionCompleted = true;
	}

	public UUID getKey() {
		return this.key;
	}

	public TeamDetails getTeamDetails() {
		return this.teamDetails;
	}

	public boolean isDeletionCompleted() {
		return this.deletionCompleted;
	}

	public static DeletedTeamEvent deletionForbidden(UUID key, TeamDetails teamDetails) {
		DeletedTeamEvent event = new DeletedTeamEvent(key, teamDetails);
		event.entityFound = true;
		event.deletionCompleted = false;
		return event;
	}

	public static DeletedTeamEvent notFound(UUID key) {
		DeletedTeamEvent event = new DeletedTeamEvent(key);
		event.deletionCompleted = false;
		event.entityFound = false;
		return event;
	}
}
