package arrays;

import java.util.*;

/**
 * ✅ Problem:
 * You are given a list of existing meeting schedules and a new meeting request.
 * Write a function to determine if the new meeting can be accommodated without overlapping.
 *
 * ✅ Example:
 * Existing: [10:00–11:00], [14:00–16:00], [23:00–23:30]
 * Incoming: [11:00–14:00] → Can be added ✔
 * Incoming: [10:30–11:30] → Overlaps ❌
 *
 * Time: O(N log N) due to sorting (can be reduced to O(N) if list is pre-sorted).
 * Space: O(1) additional.
 *
 * 🔗 Follow-up:
 * - Add a buffer time between meetings
 * - Handle multi-day meetings
 * - Support for recurring meetings
 */
public class MeetingSchedule {

  public static void main(String[] args) {
    String[][] scheduleStr = {{"10:00", "11:00"}, {"14:00", "16:00"}, {"23:00", "23:30"}};

    List<Meeting> meetings = new ArrayList<>();
    for (String[] times : scheduleStr) {
      meetings.add(new Meeting(times[0], times[1]));
    }

    Meeting incoming = new Meeting("11:00", "14:00");
    boolean canFit = MeetingScheduler.canAccommodate(meetings, incoming);

    System.out.println("Can accommodate: " + canFit);
    System.out.println("Updated Schedule: " + meetings);
  }
}

class MeetingScheduler {

  /**
   * Tries to accommodate a new meeting in an existing list of meetings.
   *
   * ✅ Steps:
   * 1. Sort the existing meetings by start time
   * 2. Try inserting in the gap between any two meetings or before/after all
   *
   * @param meetings List of existing meetings (can be unsorted)
   * @param incoming The new meeting request
   * @return true if it can be added without overlap
   */
  public static boolean canAccommodate(List<Meeting> meetings, Meeting incoming) {
    if (meetings.isEmpty()) {
      meetings.add(incoming);
      return true;
    }

    // Sort by start time
    Collections.sort(meetings);

    // Check before the first meeting
    if (incoming.endMinutes <= meetings.get(0).startMinutes) {
      meetings.add(0, incoming);
      return true;
    }

    // Check between meetings
    for (int i = 0; i < meetings.size() - 1; i++) {
      Meeting current = meetings.get(i);
      Meeting next = meetings.get(i + 1);

      if (incoming.startMinutes >= current.endMinutes && incoming.endMinutes <= next.startMinutes) {
        meetings.add(i + 1, incoming);
        return true;
      }
    }

    // Check after the last meeting
    if (incoming.startMinutes >= meetings.get(meetings.size() - 1).endMinutes) {
      meetings.add(incoming);
      return true;
    }

    return false; // No gap found
  }
}

class Meeting implements Comparable<Meeting> {
  int startMinutes;
  int endMinutes;

  public Meeting(String startTime, String endTime) {
    this.startMinutes = parseTimeToMinutes(startTime);
    this.endMinutes = parseTimeToMinutes(endTime);
  }

  /**
   * Converts time string "HH:MM" to total minutes since midnight.
   */
  private int parseTimeToMinutes(String time) {
    String[] parts = time.split(":");
    return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
  }

  @Override
  public int compareTo(Meeting other) {
    return Integer.compare(this.startMinutes, other.startMinutes);
  }

  @Override
  public String toString() {
    return String.format("[%s - %s]", formatTime(startMinutes), formatTime(endMinutes));
  }

  private String formatTime(int minutes) {
    return String.format("%02d:%02d", minutes / 60, minutes % 60);
  }
}
