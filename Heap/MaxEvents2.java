package Heap;

import java.util.Arrays;

/**
 * LeetCode: https://leetcode.com/problems/maximum-number-of-events-that-can-be-attended-ii/
 *
 * Given an array of events where each event has a start and end day, along with a value,
 * find the maximum sum of event values that can be attended, with a limit on the number of events.
 *
 * Algorithm:
 * - Sort events by start time.
 * - Use dynamic programming (memoization) to explore attending or skipping events.
 * - Use binary search to efficiently find the next non-overlapping event.
 *
 * Time Complexity: O(N log N + NK), where N is the number of events and K is the max events limit.
 * Space Complexity: O(NK) for memoization.
 */
public class MaxEvents {
    public static void main(String[] args) {
        int[][] events = {
                {1, 2, 4}, // [startDay, endDay, value]
                {3, 4, 3},
                {2, 3, 1}};
        int k = 2;
        System.out.println(new MaxEvents().maxValue(events, k));
    }

    private int len;
    
    /**
     * Finds the maximum sum of event values that can be attended with a limit on the number of events.
     *
     * @param events A 2D array where events[i] = [startDay, endDay, value].
     * @param k The maximum number of events that can be attended.
     * @return The maximum sum of event values.
     */
    public int maxValue(int[][] events, int k) {
        if (events == null || events.length == 0) return 0;
        
        // Sort events by start day, then by end day if start days are equal.
        Arrays.sort(events, (a, b) -> a[0] == b[0] ? a[1] - b[1] : a[0] - b[0]);
        
        this.len = events.length;
        int[][] dp = new int[len][k + 1];
        
        return dfs(events, k, 0, dp);
    }

    /**
     * Recursive depth-first search with memoization to compute max event value sum.
     *
     * @param events The event list.
     * @param limit Remaining events that can be attended.
     * @param index Current event index.
     * @param dp Memoization table.
     * @return Maximum event value sum from the current state.
     */
    private int dfs(int[][] events, int limit, int index, int[][] dp) {
        if (index >= len || limit == 0) return 0;
        if (dp[index][limit] > 0) return dp[index][limit];

        // Case 1: Skip the current event.
        int valWithoutCurr = dfs(events, limit, index + 1, dp);

        // Case 2: Attend the current event and find the next non-overlapping event.
        int nextIndex = index + 1;
        while (nextIndex < len && events[nextIndex][0] <= events[index][1]) {
            nextIndex++;
        }
        int valWithCurr = events[index][2] + dfs(events, limit - 1, nextIndex, dp);
        
        // Store the maximum value in the memoization table.
        dp[index][limit] = Math.max(valWithoutCurr, valWithCurr);
        return dp[index][limit];
    }
}
