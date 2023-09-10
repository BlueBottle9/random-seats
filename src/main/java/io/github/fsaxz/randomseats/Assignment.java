package io.github.fsaxz.randomseats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Assignment {
	public static HashMap<Seat, Student> assign(ArrayList<SeatGroup> groups, List<?>... studentGroups) {
		Main.logger.info("Assign seats...");

		HashMap<Seat, Student> map = new HashMap<>();
		ArrayList<Student> students = new ArrayList<>();

		for (List<?> _students : studentGroups) {
			Collections.shuffle(_students);
			for (Object std : _students) {
				students.add((Student) std);
			}
		}

		/*
		 * for (Student student:students) { for (SeatGroup group : groups) {
		 * ArrayList<Seat> seats = group.getSeats(); if (seats.isEmpty()) { continue; }
		 * Collections.shuffle(seats); for (Seat seat:seats) { map.put(seat, student); }
		 * } }
		 */
		ArrayList<Seat> seats = new ArrayList<>();
		for (SeatGroup sg : groups) {
			ArrayList<Seat> array = sg.getSeats();
			Collections.shuffle(array);

			for (Seat seat : array) {
				seats.add(seat);
			}
			
		}

		for (int i = 0; i < seats.size(); i++) {
			Student student = students.get(i);
			Seat seat = seats.get(i);
			map.put(seat, student);

			Main.logger.info("Assigned " + seat.getFullID() + " to " + student.getName());
		}

		return map;
	}
}
