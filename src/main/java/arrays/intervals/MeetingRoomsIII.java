package arrays.intervals;

import java.util.Arrays;
import java.util.PriorityQueue;


/**
 * Problem: Meeting Rooms III
 *
 * Given n rooms numbered from 0 to n - 1 and a list of meeting start/end times,
 * schedule every meeting in start-time order. Use the lowest-numbered available
 * room; if all rooms are busy, delay the meeting until the earliest room opens.
 * Return the room that hosted the most meetings, using the smallest room number
 * to break ties.
 *
 * Leetcode: https://leetcode.com/problems/meeting-rooms-iii/
 * Rating:   2093 (zerotrac Elo, Q4, weekly-contest-309)
 * Pattern:  Intervals | Heap | Available-room and busy-room priority queues
 *
 * Example:
 *   Input:  n = 2, meetings = [[0,10],[1,5],[2,7],[3,4]]
 *   Output: 0
 *   Why:    both rooms host two meetings, and the tie goes to the smaller room id.
 *
 * Follow-ups:
 *   1. What if meetings can be cancelled dynamically?
 *      Use indexed heap entries or lazy deletion keyed by meeting id.
 *   2. What if rooms have different costs?
 *      Make the available-room heap order by cost first, then room number.
 *   3. What if you need the full schedule, not just the room id?
 *      Store each assigned interval per room while performing the same heap updates.
 *
 * Related: Meeting Rooms II (253), My Calendar I (729).
 */

public class MeetingRoomsIII {

    public static void main(String[] args) {
        MeetingRoomsIII solver = new MeetingRoomsIII();
        int[] roomCounts = {2, 3};
        int[][][] meetings = {
            {{0, 10}, {1, 5}, {2, 7}, {3, 4}},
            {{1, 20}, {2, 10}, {3, 5}, {4, 9}, {6, 8}}
        };
        int[] expected = {0, 1};

        for (int i = 0; i < meetings.length; i++) {
            int[][] input = Arrays.stream(meetings[i]).map(int[]::clone).toArray(int[][]::new);
            int got = solver.mostBooked(roomCounts[i], input);
            System.out.printf("rooms=%d meetings=%s -> %d  expected=%d%n",
                roomCounts[i], Arrays.deepToString(meetings[i]), got, expected[i]);
        }
    }


  /**
     * Intuition: two different choices must be made quickly for each meeting. When
     * rooms are free, the smallest room number wins, so a min-heap of available
     * room ids is perfect. When all rooms are busy, the meeting waits for the room
     * with the earliest end time, with room number as a tie-breaker, so a second
     * heap tracks busy rooms by release time. Sorting meetings by start time lets
     * us release finished rooms before deciding where the next meeting goes.
     *
     * Time:  O(m log m + m log n) - meetings are sorted once, then each meeting does heap work over n rooms.
     * Space: O(n) - the room heaps and count array store room-level state.
     *
     * @param rooms number of rooms, numbered from 0 to rooms - 1
     * @param meetings meeting intervals [start, end]
     * @return room id that hosted the most meetings
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