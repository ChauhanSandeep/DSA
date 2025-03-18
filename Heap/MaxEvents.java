package Heap;

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * LeetCode: https://leetcode.com/problems/maximum-number-of-events-that-can-be-attended/
 *
 * Given an array of events where each event has a start and end day,
 * find the maximum number of events that can be attended by attending only one event per day.
 *
 * Algorithm:
 * - Sort events by start time (then by end time for tie-breaking).
 * - Use a min-heap to track the earliest ending events available to attend.
 * - Iterate through days, attending the event that ends the earliest first.
 *
 * Time Complexity: O(N log N) due to sorting and heap operations.
 * Space Complexity: O(N) for the heap.
 */
public class MaxEvents {
    public static void main(String[] args) {
        int[][] events = {
                {1, 4},
                {4, 4},
                {2, 2},
                {3, 4},
                {1, 1}
        };
        System.out.println("Max events attended: " + new MaxEvents().maxEvents(events));
    }

    /**
     * Finds the maximum number of events that can be attended.
     *
     * @param events A 2D array where events[i] = [startDay, endDay].
     * @return The maximum number of events that can be attended.
     */
    public int maxEvents(int[][] events) {
        if (events == null || events.length == 0) return 0;

        // Sort events by start day; if start days are equal, sort by end day.
        Arrays.sort(events, (a, b) -> a[0] == b[0] ? Integer.compare(a[1], b[1]) : Integer.compare(a[0], b[0]));

        PriorityQueue<Integer> minHeap = new PriorityQueue<>(); // Min-heap to track event end days.
        int index = 0, eventsAttended = 0, totalEvents = events.length;
        int today = events[0][0]; // Start from the earliest event day.

        while (!minHeap.isEmpty() || index < totalEvents) {
            // Remove events that have already ended before today.
            while (!minHeap.isEmpty() && minHeap.peek() < today) {
                minHeap.poll();
            }

            // Add all events that start today.
            while (index < totalEvents && events[index][0] == today) {
                minHeap.offer(events[index][1]);
                index++;
            }

            // Attend the event that ends the earliest.
            if (!minHeap.isEmpty()) {
                eventsAttended++;
                minHeap.poll();
            }

            today++; // Move to the next day.
        }
        return eventsAttended;
    }
}
