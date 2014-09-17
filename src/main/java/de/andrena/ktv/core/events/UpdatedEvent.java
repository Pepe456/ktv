package de.andrena.ktv.core.events;

public class UpdatedEvent {
	protected boolean entityFound = true;

	public boolean isEntityFound() {
		return this.entityFound;
	}

	public boolean isEntityNotFound() {
		return !this.entityFound;
	}
}
