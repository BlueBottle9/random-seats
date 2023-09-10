package io.github.fsaxz.randomseats;

public class Seat {
	private String id;
	private String fullID;

	public Seat(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public void fullID(String groupId) {
		this.fullID = groupId + this.id;
	}

	public String getFullID() {
		return this.fullID;
	}
}
