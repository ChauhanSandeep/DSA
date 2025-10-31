package arrays.greedy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * -----------------------------------------
 * 💡 Problem: Maximum Number of Non-Overlapping Events
 * -----------------------------------------
 * Given two integer arrays `arrivals` and `durations` of equal length `n`, where:
 * - `arrivals[i]` represents the start time of the i-th event.
 * - `durations[i]` represents the duration of the i-th event.
 *
 * You can attend only one event at a time. Find the **maximum number of non-overlapping events** you can attend.
 *
 * 🔹 Example:
 * Input: arrivals = [1, 3, 3, 5, 7], durations = [2, 2, 1, 2, 1]
 * Output: 4
 * Explanation: You can attend events at [1-3], [3-4], [5-7], [7-8]
 *
 * 🔗 Leetcode Link: https://leetcode.com/problems/maximum-number-of-events-that-can-be-attended/
 *
 * -----------------------------------------
 * 🔄 Follow-up Questions:
 * -----------------------------------------
 * 1. What if events can span multiple days, and attending one event blocks you for all overlapping days?
 *    → Still greedy by end-time but handle multi-day conflict properly.
 * 2. What if you can attend multiple events per day?
 *    → Model it as a priority queue sorted by availability.
 *    → Ref: https://leetcode.com/problems/maximum-number-of-events-that-can-be-attended-ii/ (if constraints change)
 */
public class MaxEvents {

  public static void main(String[] args) {
    int[] arrivals = {1, 3, 3, 5, 7};
    int[] durations = {2, 2, 1, 2, 1};
    System.out.println("Maximum events attended: " + new MaxEvents().getMaxNonOverlappingEvents(arrivals, durations));
  }

  /**
   * Greedy algorithm to select the maximum number of non-overlapping intervals.
   * Greedy works here because:
   * By always choosing the event that finishes earliest (i.e., sorting by end time),
   * we leave the most room for future events, maximizing the total number you can attend.
   * This strategy ensures that no time is wasted between events and that you never skip an opportunity
   * to attend more events later.
   *
   * Steps:
   * 1. Convert the arrivals and durations into a list of [start, end] intervals.
   * 2. Sort the intervals based on the end time (classic greedy for interval scheduling).
   * 3. Iterate over sorted intervals:
   *      - If an event starts after or at the end of the last attended one, attend it.
   *      - Otherwise, skip.
   *
   * Time Complexity: O(N log N) due to sorting.
   * Space Complexity: O(N) for storing the list of intervals.
   */
  public int getMaxNonOverlappingEvents(int[] arrivals, int[] durations) {
    int totalEvents = arrivals.length;

    // Convert to list of intervals
    List<EventInterval> eventList = new ArrayList<>();
    for (int i = 0; i < totalEvents; i++) {
      int startTime = arrivals[i];
      int endTime = arrivals[i] + durations[i];  // exclusive end
      eventList.add(new EventInterval(startTime, endTime));
    }

    // Sort by event end time (earliest finish time first)
    Collections.sort(eventList, Comparator.comparingInt(event -> event.end));

    int attendedEvents = 0;
    int lastEventEndTime = 0;

    for (EventInterval event : eventList) {
      // Attend event only if it does not overlap with the last attended event
      if (event.start >= lastEventEndTime) {
        attendedEvents++;
        lastEventEndTime = event.end;
      }
    }

    return attendedEvents;
  }

  /**
   * Simple POJO to represent an event interval with start and end.
   */
  static class EventInterval {
    int start;
    int end;

    public EventInterval(int start, int end) {
      this.start = start;
      this.end = end;
    }
  }
}
