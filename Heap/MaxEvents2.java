package Heap;

import java.util.Arrays;

/**
 * https://leetcode.com/problems/maximum-number-of-events-that-can-be-attended-ii/
 */
public class MaxEvents2 {
    public static void main(String[] args) {
        int[][] events = {
                {1, 2, 4},
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

    public int dfs(int[][] events, int k, int idx, int[][] mem) {
        if (idx >= len || k == 0) return 0;
        if (mem[idx][k] > 0) return mem[idx][k];

        int without = dfs(events, k, idx + 1, mem);
        int nIdx = idx + 1;
        while (nIdx < len && events[nIdx][0] <= events[idx][1]) {
            nIdx++;
        }
        int with = events[idx][2] + dfs(events, k - 1, nIdx, mem);
        mem[idx][k] = Math.max(without, with);
        return mem[idx][k];
    }


}
