package io.github.fsaxz.randomseats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Assignment {
	public static HashMap<Seat, Student> assign(ArrayList<SeatGroup> groups, List<?>... studentGroups) {
		Main.logger.info("Assign seats...");

		boolean random = Main.configuration.getBoolean("random-assignment");
		
		Main.logger.info("Random seat group assignment has set to " + random);

		HashMap<Seat, Student> map = new HashMap<>();
		ArrayList<Student> students = new ArrayList<>();

		for (List<?> _students : studentGroups) {
			Collections.shuffle(_students);
			for (Object std : _students) {
				students.add((Student) std);
			}
		}

		if (random) {
			Collections.shuffle(groups);
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
		} else {
			
			ArrayList<List<?>> groupStudent = new ArrayList<>();
			for (List <?> list:studentGroups) {
				groupStudent.add(list);
			}
			
			Collections.shuffle(groupStudent);
			
			int i = 0;
			for (SeatGroup sg : groups) {
				ArrayList<Seat> array = sg.getSeats();
				Collections.shuffle(array);
				if (groupStudent.get(i).isEmpty()) {
					groupStudent.remove(i);
				}
				if (i < groupStudent.size() - 1) {
					i++;
				}else {
					i = 0;
				}
				for (Seat seat:array) {
					Student student = (Student)groupStudent.get(i).get(0);
					map.put(seat, student);
					groupStudent.get(i).remove(0);
					Main.logger.info("Assigned " + seat.getFullID() + " to" + student.getName());
					
					if (groupStudent.get(i).isEmpty()) {
						groupStudent.remove(i);
						if (i < groupStudent.size() - 1) {
							i++;
						}else {
							i = 0;
						}
					}
				}
			}
		}

		return map;
	}
}
