package arrays.intervals;

import java.util.Arrays;
import java.util.PriorityQueue;


/**
 * Problem Statement:
 * You are given the number of meeting rooms and a list of meetings with start and end times.
 * Each meeting must be assigned to a room, and if multiple rooms are available, assign the meeting
 * to the lowest-numbered room. If no room is available, the meeting waits for the earliest
 * finishing room. Return the room that hosted the most meetings.
 *
 * Example:
 * Input: n = 2, meetings = [[0,10],[1,5],[2,7],[3,4]]
 * Output: 0
 * Explanation: Room 0 hosted 2 meetings, Room 1 hosted 2, but room 0 handled them earlier.
 *
 * Leetcode URL: https://leetcode.com/problems/meeting-rooms-iii
 *
 * Follow-up Questions:
 * 1. What if the meeting durations can be changed slightly (flexible start/end)? -> Use greedy + priority queue
 * 2. Can you optimize for minimal wait time instead of maximizing usage? -> Use heap and additional state tracking
 * 3. What if each room has different setup time or cost per meeting? -> Use cost-based priority queue
 * LeetCode Contest Rating: 2093
 **/

public class MeetingRoomsIII {

  /**
   * Assigns meetings to rooms such that each meeting goes to the lowest-numbered available room.
   * If all rooms are busy, the meeting waits for the room that becomes free the earliest.
   *
   * Steps:
   * 1. Sort meetings by start time.
   * 2. Use two priority queues:
   *    - `availableRooms`: to manage free room numbers (min-heap).
   *    - `busyRooms`: to manage rooms currently hosting a meeting with their end times (min-heap).
   * 3. For each meeting:
   *    - Free up rooms that have completed before the meeting starts.
   *    - If an available room exists, assign it.
   *    - Otherwise, wait for the earliest room to become free and schedule the meeting accordingly.
   * 4. Track the number of meetings each room handles.
   * 5. Return the room with the highest meeting count.
   *
   * Time Complexity: O(m log rooms) where m = number of meetings, rooms = number of rooms
   * Space Complexity: O(rooms + m) for room queues and counters
   */
  public int mostBooked(int rooms, int[][] meetings) {
    // Sort meetings by start time
    Arrays.sort(meetings, (a, b) -> Integer.compare(a[0], b[0]));

    // Min-heap for available room numbers
    PriorityQueue<Integer> availableRooms = new PriorityQueue<>();
    for (int i = 0; i < rooms; i++) {
      availableRooms.offer(i);
    }

    // Min-heap for busy rooms: (endTime, roomNumber)
    PriorityQueue<long[]> busyRooms = new PriorityQueue<>((a, b) -> {
      if (a[0] != b[0]) {
        return Long.compare(a[0], b[0]); // Compare by end time
      }
      return Long.compare(a[1], b[1]); // Tie-breaker by room number
    });

    // Meeting count for each room
    int[] meetingCount = new int[rooms];

    for (int[] meeting : meetings) {
      int start = meeting[0];
      int end = meeting[1];
      long duration = end - start;

      // Free rooms that have finished before this meeting starts
      while (!busyRooms.isEmpty() && busyRooms.peek()[0] <= start) {
        long[] finished = busyRooms.poll();
        availableRooms.offer((int) finished[1]);
      }

      if (!availableRooms.isEmpty()) {
        // Assign to available room
        int room = availableRooms.poll();
        busyRooms.offer(new long[]{end, room});
        meetingCount[room]++;
      } else {
        // Wait for earliest room to be free
        long[] nextAvailable = busyRooms.poll();
        long newStart = nextAvailable[0];
        int room = (int) nextAvailable[1];
        busyRooms.offer(new long[]{newStart + duration, room});
        meetingCount[room]++;
      }
    }

    // Find the room with maximum meeting count (tie-breaker: smallest index)
    int maxMeetings = -1;
    int resultRoom = -1;
    for (int i = 0; i < rooms; i++) {
      if (meetingCount[i] > maxMeetings) {
        maxMeetings = meetingCount[i];
        resultRoom = i;
      }
    }

    return resultRoom;
  }
}