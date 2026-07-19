package heaps;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;


/**
 * Problem: Meeting Rooms II
 *
 * Given meeting intervals [start, end], return the minimum number of conference
 * rooms needed so every meeting can be scheduled. Overlapping meetings require
 * separate rooms; a meeting ending at time t frees a room for another starting at t.
 *
 * Leetcode: https://leetcode.com/problems/meeting-rooms-ii/ (Medium)
 * Rating:   acceptance 52.7% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Heap | Sweep line | Earliest ending meeting first
 *
 * Example:
 *   Input:  intervals = [[0,30],[5,10],[15,20]]
 *   Output: 2
 *   Why:    [5,10] and [15,20] can reuse one room, while [0,30] needs another.
 *
 * Follow-ups:
 *   1. Can you solve it without a heap?
 *      Sort separate start and end arrays, then sweep with two pointers.
 *   2. How do you return actual room assignments?
 *      Store room ids with end times in the heap and append intervals to room lists.
 *   3. What if meetings are added online?
 *      Use a balanced tree or indexed calendar to query overlapping intervals.
 *   4. What if rooms have capacities or equipment constraints?
 *      Partition meetings by compatible room type, then run the greedy per group.
 *
 * Related: Meeting Rooms (252), Maximum Number of Events That Can Be Attended (1353).
 */

public class MeetingRoomsII {

    public static void main(String[] args) {
        MeetingRoomsII solver = new MeetingRoomsII();
        int[][][] inputs = { {{0, 30}, {5, 10}, {15, 20}}, {} };
        int[] expected = {2, 0};

        for (int i = 0; i < inputs.length; i++) {
            int[][] copy = new int[inputs[i].length][];
            for (int row = 0; row < inputs[i].length; row++) {
                copy[row] = inputs[i][row].clone();
            }
            int got = solver.minMeetingRooms(copy);
            System.out.printf("intervals=%s -> %d  expected=%d%n",
                Arrays.deepToString(inputs[i]), got, expected[i]);
        }
    }

        /**
     * Intuition: a room is reusable exactly when its current meeting ends before
     * or at the next meeting's start. Sorting by start time reveals meetings in
     * chronological order, and a min heap keeps the earliest room end time on top.
     *
     * Algorithm:
     *   1. Return 0 for null or empty intervals.
     *   2. Sort meetings by start time.
     *   3. Before scheduling each meeting, poll all rooms whose end time is <= start.
     *   4. Offer the current meeting's end time and return the remaining heap size.
     *
     * Time:  O(n log n) - sorting dominates and each interval updates the heap.
     * Space: O(n) - the heap stores end times of active meetings.
     *
     * @param intervals meeting intervals as [start, end]
     * @return number of rooms reported by the heap schedule
     */

    public int minMeetingRooms(int[][] intervals) {
        if (intervals == null || intervals.length == 0) {
            return 0;
        }

        // Sort meetings by start time
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));

        // Min heap to track end times of ongoing meetings
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();

        for (int[] interval : intervals) {
            int start = interval[0];
            int end = interval[1];

            // Remove meetings that have ended before current meeting starts
            while (!minHeap.isEmpty() && minHeap.peek() <= start) {
                minHeap.poll();
            }

            // Add current meeting's end time
            minHeap.offer(end);
        }

        return minHeap.size();
    }

    /**
     * Returns the actual room assignments for debugging/visualization.
     *
     * @param intervals Meeting intervals
     * @return List of room assignments, where each element is a list of meetings for that room
     */
    public List<List<int[]>> getRoomAssignments(int[][] intervals) {
        if (intervals == null || intervals.length == 0) {
            return new ArrayList<>();
        }
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));

        List<List<int[]>> roomBookings = new ArrayList<>();
        PriorityQueue<RoomBooking> availableRooms = new PriorityQueue<>((a, b) -> Integer.compare(a.endTime, b.endTime));

        int roomIdCounter = 0;

        for (int[] interval : intervals) {
            // Free up rooms that have finished
            while (!availableRooms.isEmpty() && availableRooms.peek().endTime <= interval[0]) {
                availableRooms.poll();
            }

            RoomBooking roomBooking;
            if (availableRooms.isEmpty()) {
                // Need a new room
                roomBookings.add(new ArrayList<>());
                roomBooking = new RoomBooking(roomIdCounter++, interval[0], interval[1]);
            } else {
                // Reuse existing room
                roomBooking = availableRooms.poll();
                roomBooking.endTime = interval[1];
            }

            roomBookings.get(roomBooking.roomId).add(interval);
            availableRooms.offer(roomBooking);
        }

        return roomBookings;
    }

    /** Tracks a room id and the end time of its latest assigned meeting. */
    static class RoomBooking {
        int roomId;
        int startTime;
        int endTime;

        RoomBooking(int roomId, int startTime, int endTime) {
            this.roomId = roomId;
            this.startTime = startTime;
            this.endTime = endTime;
        }
    }
}