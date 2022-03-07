package Heap;

import java.util.Arrays;

/**
 * You are given an array of events. Each event is like [startDay, endDay, value] where startDay and endDay
 * are inclusive. If you choose to attend the event, you have to attend all days(startDay to endDay inclusive).
 * In exchange, you will get `value`. Find the max value you can get.
 *
 * https://leetcode.com/problems/maximum-number-of-events-that-can-be-attended-ii/
 */
public class MaxEvents2 {
    public static void main(String[] args) {
        int[][] events = {
                {1, 2, 4}, // [startDay, endDay, value]
                {3, 4, 3},
                {2, 3, 1}};
        int k = 2;
        System.out.println(new MaxEvents2().maxValue(events, k));
    }

    int len;
    public int maxValue(int[][] events, int k) {
        if (events == null || events.length == 0) return 0;
        Arrays.sort(events, (a, b) -> {
            if (a[0] == b[0]) return a[1] - b[1];
            return a[0] - b[0];
        });
        this.len = events.length;
        int[][] dp = new int[len][k+1];

        return dfs(events, k, 0, dp);
    }

    private int dfs(int[][] events, int limit, int index, int[][] dp) {
        if (index >= len || limit == 0) return 0;
        if (dp[index][limit] > 0) return dp[index][limit];

        int valWithoutCurr = dfs(events, limit, index + 1, dp);
        int nIndex = index + 1;
        while (nIndex < len && events[nIndex][0] <= events[index][1]) {
            nIndex++;
        }
        int valWithCurr = events[index][2] + dfs(events, limit - 1, nIndex, dp);
        dp[index][limit] = Math.max(valWithoutCurr, valWithCurr);
        return dp[index][limit];
    }


}
