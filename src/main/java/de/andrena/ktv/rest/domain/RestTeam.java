package de.andrena.ktv.rest.domain;

import java.io.Serializable;
import java.util.UUID;

import de.andrena.ktv.core.domain.TeamDetails;

public class RestTeam implements Serializable {

	private static final long serialVersionUID = 1L;
	private UUID key;
	private String name;
	private String player1;
	private String player2;

	public TeamDetails toTeamDetails() {
		TeamDetails teamDetails = new TeamDetails();
		teamDetails.setKey(this.key);
		teamDetails.setName(this.name);
		teamDetails.setPlayer1(this.player1);
		teamDetails.setPlayer2(this.player2);
		return teamDetails;
	}

	public static RestTeam fromTeamDetails(TeamDetails teamDetails) {
		RestTeam team = new RestTeam();
		team.key = teamDetails.getKey();
		team.name = teamDetails.getName();
		team.player1 = teamDetails.getPlayer1();
		team.player2 = teamDetails.getPlayer2();
		return team;
	}

	public UUID getKey() {
		return this.key;
	}

	public void setKey(UUID key) {
		this.key = key;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPlayer1() {
		return this.player1;
	}

	public void setPlayer1(String player1) {
		this.player1 = player1;
	}

	public String getPlayer2() {
		return this.player2;
	}

	public void setPlayer2(String player2) {
		this.player2 = player2;
	}

}
