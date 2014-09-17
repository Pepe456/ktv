package de.andrena.ktv.rest.fixtures;

import java.util.UUID;

import de.andrena.ktv.rest.domain.RestTeam;

public class RestTeamBuilder {
	private final RestTeam team;

	public RestTeamBuilder() {
		this.team = new RestTeam();
	}

	public RestTeamBuilder withKey(String key) {
		this.team.setKey(UUID.fromString(key));
		return this;
	}

	public RestTeamBuilder withKey(UUID key) {
		this.team.setKey(key);
		return this;
	}

	public RestTeamBuilder withName(String name) {
		this.team.setName(name);
		return this;
	}

	public RestTeamBuilder withPlayer1(String player1) {
		this.team.setPlayer1(player1);
		return this;
	}

	public RestTeamBuilder withPlayer2(String player2) {
		this.team.setPlayer2(player2);
		return this;
	}

	public RestTeam create() {
		return this.team;
	}

	public String createAsJSON() {
		StringBuilder json = new StringBuilder("{");
		if (this.team.getKey() != null) {
			json.append("\"key\": \"" + this.team.getKey() + "\",");
		}
		if (this.team.getName() != null) {
			json.append("\"name\": \"" + this.team.getName() + "\",");
		}
		if (this.team.getPlayer1() != null) {
			json.append("\"player1\": \"" + this.team.getPlayer1() + "\",");
		}
		if (this.team.getPlayer2() != null) {
			json.append("\"player2\": \"" + this.team.getPlayer2() + "\"");
		}
		json.append("}");
		return json.toString();
	}
}
