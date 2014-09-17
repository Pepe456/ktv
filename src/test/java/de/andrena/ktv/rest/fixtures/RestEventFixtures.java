package de.andrena.ktv.rest.fixtures;

import static de.andrena.ktv.core.fixtures.Fixtures.standardTeamDetailsWithoutKey;
import static de.andrena.ktv.core.fixtures.Fixtures.standardTeamDetailsWithKey;

import java.util.UUID;

import de.andrena.ktv.core.events.teams.CreatedTeamEvent;
import de.andrena.ktv.core.events.teams.DeletedTeamEvent;
import de.andrena.ktv.core.events.teams.ReadedTeamDetailsEvent;
import de.andrena.ktv.core.events.teams.UpdatedTeamEvent;

public class RestEventFixtures {

	public static ReadedTeamDetailsEvent teamDetailsNotFound(UUID key) {
		return ReadedTeamDetailsEvent.notFound(key);
	}

	public static ReadedTeamDetailsEvent teamDetailsEvent(UUID key) {
		return new ReadedTeamDetailsEvent(key, standardTeamDetailsWithKey(key));
	}

	public static CreatedTeamEvent teamCreatedEvent(UUID key) {
		return new CreatedTeamEvent(key, standardTeamDetailsWithKey(key));
	}

	public static UpdatedTeamEvent teamUpdatedEvent(UUID key) {
		return new UpdatedTeamEvent(key, standardTeamDetailsWithKey(key));
	}

	public static DeletedTeamEvent teamDeleted(UUID key) {
		return new DeletedTeamEvent(key, standardTeamDetailsWithoutKey());
	}

	public static DeletedTeamEvent teamDeletedFailed(UUID key) {
		return DeletedTeamEvent.deletionForbidden(key, standardTeamDetailsWithoutKey());
	}

	public static DeletedTeamEvent teamDeletedNotFound(UUID key) {
		return DeletedTeamEvent.notFound(key);
	}

}
