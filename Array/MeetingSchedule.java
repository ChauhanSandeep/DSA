package Array;

import java.util.*;

public class MeetingSchedule {

    public static void main(String[] args) {
        String[][] scheduleStr = {
                {"10:00", "11:00"},
                {"14:00", "16:00"},
                {"23:00", "23:30"}
        };

        List<Schedule> schedules = new ArrayList<>();
        for (String[] times : scheduleStr) {
            schedules.add(new Schedule(times[0], times[1]));
        }

        Schedule incomingSchedule = new Schedule("11:00", "14:00");
        boolean canAccommodate = accommodate(schedules, incomingSchedule);

        System.out.println("Can accommodate: " + canAccommodate);
        System.out.println("Updated Schedule: " + schedules);
    }

    /**
     * Tries to insert a new schedule into an existing sorted schedule list.
     *
     * @param schedules        The list of existing schedules.
     * @param incomingSchedule The new schedule to insert.
     * @return true if the schedule was successfully inserted; false if there is a conflict.
     */
    public static boolean accommodate(List<Schedule> schedules, Schedule incomingSchedule) {
        if (schedules.isEmpty()) {
            schedules.add(incomingSchedule);
            return true;
        }

        Collections.sort(schedules); // Ensure the schedules are sorted by start time

        // Check if the new schedule fits before the first meeting
        if (incomingSchedule.endTime <= schedules.get(0).startTime) {
            schedules.add(0, incomingSchedule);
            return true;
        }

        // Check available slots between meetings
        for (int i = 0; i < schedules.size(); i++) {
            if (schedules.get(i).endTime <= incomingSchedule.startTime &&
                    (i == schedules.size() - 1 || incomingSchedule.endTime <= schedules.get(i + 1).startTime)) {
                schedules.add(i + 1, incomingSchedule);
                return true;
            }
        }

        return false; // No available slot
    }
}

class Schedule implements Comparable<Schedule> {
    float startTime;
    float endTime;

    public Schedule(String startTime, String endTime) {
        this.startTime = convertToFloatTime(startTime);
        this.endTime = convertToFloatTime(endTime);
    }

    /**
     * Converts a "HH:MM" time format into a float representation (e.g., "10:30" → 10.5).
     */
    private float convertToFloatTime(String time) {
        String[] parts = time.split(":");
        return Integer.parseInt(parts[0]) + Integer.parseInt(parts[1]) / 60f;
    }

    @Override
    public int compareTo(Schedule other) {
        return Float.compare(this.startTime, other.startTime);
    }

    @Override
    public String toString() {
        return String.format("{startTime=%.2f, endTime=%.2f}", startTime, endTime);
    }
}
