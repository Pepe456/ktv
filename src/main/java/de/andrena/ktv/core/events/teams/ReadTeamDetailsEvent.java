package de.andrena.ktv.core.events.teams;

import java.util.UUID;

import de.andrena.ktv.core.events.ReadEvent;

public class ReadTeamDetailsEvent extends ReadEvent {
	private final UUID key;

	public ReadTeamDetailsEvent(UUID key) {
		this.key = key;
	}

	public UUID getKey() {
		return this.key;
	}

}
