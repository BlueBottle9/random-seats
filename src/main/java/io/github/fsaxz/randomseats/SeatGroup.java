package io.github.fsaxz.randomseats;

import java.util.ArrayList;

public class SeatGroup {
	private ArrayList<Seat> seats = new ArrayList<>();
	private String groupId;

	public SeatGroup(String groupId, Seat... seats) {
		this.groupId = groupId;
		
		for (Seat seat : seats) {
			seat.fullID(this.groupId);
			this.seats.add(seat);
		}

	}

	public ArrayList<Seat> getSeats() {
		return this.seats;
	}

	public String getGroupId() {
		return this.groupId;
	}
}
