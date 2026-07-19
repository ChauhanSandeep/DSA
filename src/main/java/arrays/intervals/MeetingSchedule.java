package arrays.intervals;

import java.util.*;

/**
 * Problem: Meeting Schedule Insertion
 *
 * Given an existing list of meetings and one incoming meeting, decide whether the
 * incoming meeting can be added without overlapping any existing meeting. Times
 * are represented as HH:MM strings within one day, and touching endpoints are
 * allowed, such as 11:00 after a meeting ending at 11:00.
 *
 * Pattern:  Intervals | Sorting | Gap check
 *
 * Example:
 *   Input:  existing = [[10:00,11:00],[14:00,16:00]], incoming = [11:00,14:00]
 *   Output: true
 *   Why:    the incoming meeting starts exactly when the first meeting ends and
 *           ends exactly when the next meeting begins, so it fits in the gap.
 *
 * Follow-ups:
 *   1. What if every meeting needs a 10-minute buffer?
 *      Expand each existing meeting by the buffer before checking gaps.
 *   2. What if meetings can cross midnight?
 *      Normalize into absolute minutes on a timeline that can exceed one day.
 *   3. What if you receive many insertion requests?
 *      Keep meetings sorted in a balanced tree and check only predecessor and successor.
 */
public class MeetingSchedule {
    public static void main(String[] args) {
        String[][] schedule = {{"10:00", "11:00"}, {"14:00", "16:00"}, {"23:00", "23:30"}};
        Meeting[] incomingMeetings = {new Meeting("11:00", "14:00"), new Meeting("10:30", "11:30")};
        boolean[] expected = {true, false};

        for (int i = 0; i < incomingMeetings.length; i++) {
            List<Meeting> meetings = new ArrayList<>();
            for (String[] timeRange : schedule) {
                meetings.add(new Meeting(timeRange[0], timeRange[1]));
            }
            String meetingsLabel = meetings.toString();
            boolean got = MeetingScheduler.canAccommodate(meetings, incomingMeetings[i]);
            System.out.printf("meetings=%s incoming=%s -> %s  expected=%s%n",
                meetingsLabel, incomingMeetings[i], got, expected[i]);
        }
    }
}

class MeetingScheduler {

  /**
     * Intuition: after meetings are sorted by start time, the incoming meeting can
     * only fit in one of three places: before the first meeting, between two
     * neighboring meetings, or after the last meeting. We do not need to compare it
     * with every possible pair; checking each gap between consecutive meetings is
     * enough because sorted order makes those the only open spaces. When a gap is
     * found, inserting there preserves the sorted schedule.
     *
     * Time:  O(n log n) - sorting the meetings dominates the linear gap scan.
     * Space: O(1) - aside from the in-place sort, only a few references are used.
     *
     * @param meetings existing meetings, possibly unsorted; modified when insertion succeeds
     * @param incoming new meeting request
     * @return true if incoming was inserted without overlap, otherwise false
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
