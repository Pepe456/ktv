package de.andrena.ktv.core.domain;

import java.util.UUID;

public class Team {

	private UUID key;
	private String name;
	private String player1;
	private String player2;

	private boolean canBeDeleted;
	private boolean canBeModified;

	public Team(String name, String player1, String player2) {
		this.key = UUID.randomUUID();
		this.name = name;
		this.player1 = player1;
		this.player2 = player2;
		this.canBeDeleted = true;
		this.canBeModified = true;
	}

	public Team(UUID key, String name, String player1, String player2) {
		this.key = key;
		this.name = name;
		this.player1 = player1;
		this.player2 = player2;
		this.canBeDeleted = true;
		this.canBeModified = true;
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

	public UUID getKey() {
		return this.key;
	}

	public void setKey(UUID key) {
		this.key = key;
	}

	public static Team fromTeamDetails(TeamDetails teamDetails) {
		if (teamDetails.getKey() != null) {
			return new Team(teamDetails.getKey(), teamDetails.getName(), teamDetails.getPlayer1(), teamDetails.getPlayer2());
		}
		return new Team(teamDetails.getName(), teamDetails.getPlayer1(), teamDetails.getPlayer2());
	}

	public static Team fromTeamDetailsWithNewRandomKey(TeamDetails teamDetails) {
		return new Team(teamDetails.getName(), teamDetails.getPlayer1(), teamDetails.getPlayer2());
	}

	public TeamDetails toTeamDetails() {
		TeamDetails teamDetails = new TeamDetails();
		teamDetails.setKey(this.key);
		teamDetails.setName(this.name);
		teamDetails.setPlayer1(this.player1);
		teamDetails.setPlayer2(this.player2);
		return teamDetails;
	}

	public boolean cannotBeDeleted() {
		return !this.canBeDeleted;
	}

	public boolean cannotBeModified() {
		return !this.canBeModified;
	}

	public void setCanBeDeleted(boolean canBeDeleted) {
		this.canBeDeleted = canBeDeleted;
	}

	public void setCanBeModified(boolean canBeModified) {
		this.canBeModified = canBeModified;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((this.key == null) ? 0 : this.key.hashCode());
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
		Team other = (Team) obj;
		if (this.key == null) {
			if (other.key != null) {
				return false;
			}
		} else if (!this.key.equals(other.key)) {
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
		return "Team [key=" + this.key + ", name=" + this.name + ", player1=" + this.player1 + ", player2=" + this.player2 + "]";
	}

}
