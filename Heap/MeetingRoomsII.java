package com.sandeep.frazsheet.heap;

import java.util.PriorityQueue;
import java.util.Arrays;

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
            if (!minHeap.isEmpty() && minHeap.peek() <= start) {
                minHeap.poll();
            }
            
            // Add current meeting's end time
            minHeap.offer(end);
        }
        
        return minHeap.size();
    }
    
    /**
     * Two-pointer approach using separate start and end arrays.
     * More intuitive understanding of the problem.
     * 
     * Time Complexity: O(n log n)
     * Space Complexity: O(n)
     */
    public int minMeetingRoomsTwoPointers(int[][] intervals) {
        if (intervals == null || intervals.length == 0) {
            return 0;
        }
        
        int n = intervals.length;
        int[] starts = new int[n];
        int[] ends = new int[n];
        
        // Extract start and end times
        for (int i = 0; i < n; i++) {
            starts[i] = intervals[i][0];
            ends[i] = intervals[i][1];
        }
        
        // Sort both arrays
        Arrays.sort(starts);
        Arrays.sort(ends);
        
        int rooms = 0;
        int maxRooms = 0;
        int startPtr = 0;
        int endPtr = 0;
        
        // Use two pointers to track room allocation
        while (startPtr < n) {
            if (starts[startPtr] < ends[endPtr]) {
                // Meeting starts, need a room
                rooms++;
                startPtr++;
            } else {
                // Meeting ends, free up a room
                rooms--;
                endPtr++;
            }
            
            maxRooms = Math.max(maxRooms, rooms);
        }
        
        return maxRooms;
    }
    
    /**
     * Event-based approach using custom Event class.
     * Treats start and end times as separate events.
     * 
     * Time Complexity: O(n log n)
     * Space Complexity: O(n)
     */
    public int minMeetingRoomsEventBased(int[][] intervals) {
        if (intervals == null || intervals.length == 0) {
            return 0;
        }
        
        java.util.List<Event> events = new java.util.ArrayList<>();
        
        // Create events for start and end times
        for (int[] interval : intervals) {
            events.add(new Event(interval[0], EventType.START));
            events.add(new Event(interval[1], EventType.END));
        }
        
        // Sort events by time, with END events before START events at same time
        events.sort((a, b) -> {
            if (a.time != b.time) {
                return Integer.compare(a.time, b.time);
            }
            return a.type.compareTo(b.type); // END comes before START
        });
        
        int currentRooms = 0;
        int maxRooms = 0;
        
        for (Event event : events) {
            if (event.type == EventType.START) {
                currentRooms++;
            } else {
                currentRooms--;
            }
            maxRooms = Math.max(maxRooms, currentRooms);
        }
        
        return maxRooms;
    }
    
    /**
     * Sweep line algorithm with coordinate compression.
     * Useful when dealing with large time ranges.
     */
    public int minMeetingRoomsSweepLine(int[][] intervals) {
        java.util.Map<Integer, Integer> timePoints = new java.util.TreeMap<>();
        
        // Count start and end events at each time point
        for (int[] interval : intervals) {
            timePoints.put(interval[0], timePoints.getOrDefault(interval[0], 0) + 1);
            timePoints.put(interval[1], timePoints.getOrDefault(interval[1], 0) - 1);
        }
        
        int currentRooms = 0;
        int maxRooms = 0;
        
        for (int change : timePoints.values()) {
            currentRooms += change;
            maxRooms = Math.max(maxRooms, currentRooms);
        }
        
        return maxRooms;
    }
    
    // Helper classes for event-based approach
    enum EventType {
        END, START  // END has lower ordinal, so it comes first in natural ordering
    }
    
    static class Event {
        int time;
        EventType type;
        
        Event(int time, EventType type) {
            this.time = time;
            this.type = type;
        }
    }
    
    /**
     * Returns the actual room assignments for debugging/visualization.
     * 
     * @param intervals Meeting intervals
     * @return List of room assignments, where each element is a list of meetings for that room
     */
    public java.util.List<java.util.List<int[]>> getRoomAssignments(int[][] intervals) {
        if (intervals == null || intervals.length == 0) {
            return new java.util.ArrayList<>();
        }
        
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
        
        java.util.List<java.util.List<int[]>> rooms = new java.util.ArrayList<>();
        PriorityQueue<RoomInfo> availableRooms = new PriorityQueue<>((a, b) -> 
            Integer.compare(a.endTime, b.endTime));
        
        int roomIdCounter = 0;
        
        for (int[] interval : intervals) {
            // Free up rooms that have finished
            while (!availableRooms.isEmpty() && availableRooms.peek().endTime <= interval[0]) {
                availableRooms.poll();
            }
            
            RoomInfo room;
            if (availableRooms.isEmpty()) {
                // Need a new room
                rooms.add(new java.util.ArrayList<>());
                room = new RoomInfo(roomIdCounter++, interval[1]);
            } else {
                // Reuse existing room
                room = availableRooms.poll();
                room.endTime = interval[1];
            }
            
            rooms.get(room.roomId).add(interval);
            availableRooms.offer(room);
        }
        
        return rooms;
    }
    
    // Helper class for room assignment tracking
    static class RoomInfo {
        int roomId;
        int endTime;
        
        RoomInfo(int roomId, int endTime) {
            this.roomId = roomId;
            this.endTime = endTime;
        }
    }
}