package heaps;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;


/**
 * Problem: Meeting Rooms II
 *
 * Given an array of meeting time intervals where intervals[i] = [starti, endi],
 * determine the minimum number of conference rooms required.
 *
 * Example:
 * Input: intervals = [[0,30],[5,10],[15,20]]
 * Output: 2
 * Explanation: We need two meeting rooms:
 * - Room 1: [0,30]
 * - Room 2: [5,10],[15,20]
 *
 * LeetCode: https://leetcode.com/problems/meeting-rooms-ii
 *
 * Follow-up Questions:
 * 1. What if we need to return the actual room assignments?
 *    Answer: Modify heap to track room IDs and return mapping of meetings to rooms.
 *
 * 2. How would you handle meetings with priorities?
 *    Answer: Sort by priority first, then apply greedy room assignment with priority consideration.
 *
 * 3. What if rooms have different capacities?
 *    Answer: Use more complex data structure to track room capacity and current occupancy.
 *    Related: https://leetcode.com/problems/meeting-rooms/
 *
 * @author Sandeep
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class MeetingRoomsII {

    /**
     * Finds minimum meeting rooms using min heap approach.
     *
     * Algorithm:
     * 1. Sort meetings by start time
     * 2. Use min heap to track end times of ongoing meetings
     * 3. For each meeting, remove finished meetings from heap
     * 4. Add current meeting's end time to heap
     * 5. Maximum heap size is the answer
     *
     * Time Complexity: O(n log n) where n is number of meetings
     * Space Complexity: O(n) for the heap
     *
     * @param intervals Array of meeting intervals [start, end]
     * @return Minimum number of conference rooms required
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

    // Helper class for room assignment tracking
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