package heaps;

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * Problem: Maximum Number of Events That Can Be Attended
 *
 * Given events [startDay, endDay], attend at most one event per day and maximize
 * how many events you attend. On each day, among currently available events, the
 * safest greedy choice is the one that ends earliest.
 *
 * Leetcode: https://leetcode.com/problems/maximum-number-of-events-that-can-be-attended/ (Medium)
 * Rating:   2016 (zerotrac Elo)
 * Pattern:  Heap | Greedy by earliest end day | Sweep line over days
 *
 * Example:
 *   Input:  events = [[1,4],[4,4],[2,2],[3,4],[1,1]]
 *   Output: 4
 *   Why:    attend events on days 1, 2, 3, and 4, choosing an available event each day.
 *
 * Follow-ups:
 *   1. How do you maximize value instead of count with at most k events?
 *      Use weighted interval scheduling DP with binary search over next event.
 *   2. What if multiple events can be attended on the same day?
 *      Track daily capacity and poll up to that many earliest-ending events.
 *   3. How do you avoid iterating over empty day ranges?
 *      Jump today to the next event start whenever the heap is empty.
 *   4. What if events are streamed by start day?
 *      Feed starts into the same min heap as days advance.
 *
 * Related: Maximum Number of Events That Can Be Attended II (1751), Meeting Rooms II (253).
 */

public class MaxEventsThatCanbeAttended {
    public static void main(String[] args) {
        MaxEventsThatCanbeAttended solver = new MaxEventsThatCanbeAttended();
        int[][][] inputs = {
                {{1, 4}, {4, 4}, {2, 2}, {3, 4}, {1, 1}},
                {}
        };
        int[] expected = {4, 0};

        for (int i = 0; i < inputs.length; i++) {
            int[][] copy = new int[inputs[i].length][];
            for (int row = 0; row < inputs[i].length; row++) {
                copy[row] = inputs[i][row].clone();
            }
            int got = solver.maxEvents(copy);
            System.out.printf("events=%s -> %d  expected=%d%n",
                    Arrays.deepToString(inputs[i]), got, expected[i]);
        }
    }

    /**
     * Using Greedy approach:
     *
     * Algorithm:
     * - Always attend the event that ends the earliest.
     * This ensures that we leave the most room for future events,
     * maximizing the total number of events attended.
     * * This greedy strategy works because by always choosing the event that finishes earliest,
     * we leave the most room for future events,
     * maximizing the total number you can attend.
     *
     * * Time Complexity: O(N log N) due to sorting.
     * * Space Complexity: O(N) for the heap.
     *
     */
    public int maxEventsGreedy(int[][] events) {
        if (events == null || events.length == 0) return 0;

        // Sort events by end day, then by start day for tie-breaking.
        Arrays.sort(events, (a, b) -> a[1] == b[1] ? Integer.compare(a[0], b[0]) : Integer.compare(a[1], b[1]));

        int eventsAttended = 0;
        int lastEndDay = 0;

        for (int[] event : events) {
            // Attend the event only if it starts after the last attended event's end day.
            if (event[0] > lastEndDay) {
                eventsAttended++;
                lastEndDay = event[1]; // Update the last end day to this event's end day.
            }
        }
        return eventsAttended;
    }

        /**
     * Intuition: once a day arrives, attending the available event with the
     * earliest end day leaves the widest future window. Sorting by start day lets
     * us add newly available events, while a min heap exposes the event that would
     * expire first.
     *
     * Algorithm:
     *   1. Return 0 for null or empty input.
     *   2. Sort events by start day, then end day.
     *   3. For each day, remove expired events and add events starting today.
     *   4. Attend one earliest-ending available event, then move to the next day.
     *
     * Time:  O(n log n) - sorting and one heap offer/poll per event.
     * Space: O(n) - the heap can hold all currently available events.
     *
     * @param events events as [startDay, endDay]
     * @return maximum number of events that can be attended
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
