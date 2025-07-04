package Heap;

import java.sql.Time;
import java.util.Arrays;

/**
 * LeetCode: https://leetcode.com/problems/maximum-number-of-events-that-can-be-attended-ii/
 *
 * Given an array of events where each event has [startDay, endDay, value], find the max value
 * that can be obtained by attending up to k events, attending all days for any chosen event.
 * Example:
 * Input: events = [[1,2,4],[3,4,3],[2,3,1]], k = 2
 * Output: 7
 * Explanation: Attend event 1 (value 4) and event 2 (value 3) for a total value of 7.
 */
public class MaxEvents2 {
    public static void main(String[] args) {
        int[][] events = {
            {1, 2, 4}, // [startDay, endDay, value]
            {3, 4, 3},
            {2, 3, 1}
        };
        int k = 2;
        System.out.println("Max value: " + new MaxEvents2().maxValue(events, k));
    }

    private int[][] dp;
    private int eventCount;

    /*
     * Algorithm:
     * - Sort events by start day (then by end day for stability).
     * - Use DP with memoization and binary search to maximize event value selection.
     *
     * Time Complexity: O(N * K) (for DP) + O(N log N) (for sorting and binary search), overall **O(NK + N log N)**.
     * Space Complexity: O(NK) for memoization.
     */
    public int maxValue(int[][] events, int k) {
        if (events == null || events.length == 0 || k == 0) return 0;

        // Sort by start day; if equal, sort by end day for better stability.
        Arrays.sort(events, (a, b) -> a[0] == b[0] ? Integer.compare(a[1], b[1]) : Integer.compare(a[0], b[0]));

        this.eventCount = events.length;
        this.dp = new int[eventCount][k + 1];

        // Initialize DP table with -1 (uncomputed states)
        for (int[] row : dp) {
            Arrays.fill(row, -1);
        }

        return dfs(events, k, 0);
    }

    /**
     * Recursive DFS with Memoization to find the maximum event value.
     *
     * @param events    Sorted event list.
     * @param remaining Remaining events that can be attended.
     * @param index     Current event index.
     * @return Maximum obtainable value.
     */
    private int dfs(int[][] events, int remaining, int index) {
        if (index >= eventCount || remaining == 0) return 0;
        if (dp[index][remaining] != -1) return dp[index][remaining];

        // Case 1: Skip the current event.
        int valueWithoutCurrent = dfs(events, remaining, index + 1);

        // Case 2: Attend current event and find the next non-overlapping event using binary search.
        int nextIndex = findNextEvent(events, events[index][1] + 1);
        int valueWithCurrent = events[index][2] + dfs(events, remaining - 1, nextIndex);

        // Store result in DP table.
        return dp[index][remaining] = Math.max(valueWithoutCurrent, valueWithCurrent);
    }

    /**
     * Uses Binary Search to find the next available non-overlapping event.
     *
     * @param events Sorted event list.
     * @param start  The start day to search for.
     * @return Index of the next available event.
     */
    private int findNextEvent(int[][] events, int start) {
        int left = 0, right = eventCount;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (events[mid][0] >= start) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }
}
