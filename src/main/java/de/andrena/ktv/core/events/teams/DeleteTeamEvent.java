package de.andrena.ktv.core.events.teams;

import java.util.UUID;

import de.andrena.ktv.core.events.DeleteEvent;

public class DeleteTeamEvent extends DeleteEvent {
	private final UUID key;

	public DeleteTeamEvent(final UUID key) {
		this.key = key;
	}

	public UUID getKey() {
		return key;
	}
}
