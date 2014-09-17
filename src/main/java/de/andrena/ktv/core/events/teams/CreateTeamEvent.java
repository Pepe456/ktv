package de.andrena.ktv.core.events.teams;

import de.andrena.ktv.core.domain.TeamDetails;
import de.andrena.ktv.core.events.CreateEvent;

public class CreateTeamEvent extends CreateEvent {
	private final TeamDetails teamDetails;

	public CreateTeamEvent(TeamDetails teamDetails) {
		this.teamDetails = teamDetails;
	}

	public TeamDetails getTeamDetails() {
		return this.teamDetails;
	}
}
