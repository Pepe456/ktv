package de.andrena.ktv.core.events.teams;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.andrena.ktv.core.domain.TeamDetails;
import de.andrena.ktv.core.events.ReadedEvent;

public class ReadedAllTeamDetailsEvent extends ReadedEvent {

	private final List<TeamDetails> teamsDetails;

	public ReadedAllTeamDetailsEvent(List<TeamDetails> teamsDetails) {
		this.teamsDetails = Collections.unmodifiableList(teamsDetails);
	}

	public Collection<TeamDetails> getTeamDetails() {
		return this.teamsDetails;
	}

}
