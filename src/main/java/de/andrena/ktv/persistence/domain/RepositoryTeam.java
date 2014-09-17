package de.andrena.ktv.persistence.domain;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import de.andrena.ktv.core.domain.TeamDetails;

@Entity(name = "TEAMS")
public class RepositoryTeam {

	@Id
	@Column(name = "TEAM_ID")
	private String id;

	@Column(name = "NAME")
	private String name;

	@Column(name = "PLAYER_1")
	private String player1;

	@Column(name = "PLAYER_2")
	private String player2;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
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

	public TeamDetails toTeamDetails() {
		TeamDetails details = new TeamDetails();
		details.setKey(UUID.fromString(this.id));
		details.setName(this.name);
		details.setPlayer1(this.player1);
		details.setPlayer2(this.player2);
		return details;
	}

	public static RepositoryTeam fromTeamDetails(TeamDetails details) {
		RepositoryTeam team = new RepositoryTeam();

		team.setId(details.getKey().toString());
		team.setName(details.getName());
		team.setPlayer1(details.getPlayer1());
		team.setPlayer2(details.getPlayer2());

		return team;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((this.id == null) ? 0 : this.id.hashCode());
		result = (prime * result) + ((this.name == null) ? 0 : this.name.hashCode());
		result = (prime * result) + ((this.player1 == null) ? 0 : this.player1.hashCode());
		result = (prime * result) + ((this.player2 == null) ? 0 : this.player2.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		RepositoryTeam other = (RepositoryTeam) obj;
		if (this.id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!this.id.equals(other.id)) {
			return false;
		}
		if (this.name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!this.name.equals(other.name)) {
			return false;
		}
		if (this.player1 == null) {
			if (other.player1 != null) {
				return false;
			}
		} else if (!this.player1.equals(other.player1)) {
			return false;
		}
		if (this.player2 == null) {
			if (other.player2 != null) {
				return false;
			}
		} else if (!this.player2.equals(other.player2)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "RepositoryTeam [id=" + this.id + ", name=" + this.name + ", player1=" + this.player1 + ", player2=" + this.player2 + "]";
	}

}
