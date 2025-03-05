package DynamicProgramming;

import java.util.Arrays;

/**
 * LeetCode 1473: Paint House III (Top-Down DP with Memoization)
 * 
 * - You are given `m` houses, `n` colors, and `target` neighborhoods.
 * - Some houses are already painted (`houses[i] != 0`).
 * - Minimize painting cost while ensuring exactly `target` neighborhoods.
 *
 * Approach:
 * - Use **Top-Down DP + Memoization** to explore valid color assignments.
 * - Maintain `(index, neighborhoodCount, previousColor)` as state.
 * - Use **Integer.MAX_VALUE / 2** to prevent overflow in cost calculations.
 * 
 * Time Complexity: **O(m * target * n²)**  
 * Space Complexity: **O(m * target * n)**
 */
public class PaintHouse3 {
    private Integer[][][] memo;
    private static final int INF = Integer.MAX_VALUE / 2;

    public int minCost(int[] houses, int[][] cost, int m, int n, int target) {
        // Memoization cache: [houseIndex][neighborhoodCount][previousColor]
        memo = new Integer[m][target + 1][n + 1];

        int minCost = dfs(houses, cost, 0, 0, 0, target, n);
        return (minCost == INF) ? -1 : minCost;
    }

    private int dfs(int[] houses, int[][] cost, int index, int neighborhoods, int prevColor, int target, int n) {
        // If all houses are processed, return 0 if neighborhoods == target, else INF
        if (index == houses.length) return (neighborhoods == target) ? 0 : INF;
        if (neighborhoods > target) return INF; // Too many neighborhoods → invalid

        // Check memoization cache
        if (memo[index][neighborhoods][prevColor] != null) return memo[index][neighborhoods][prevColor];

        int minCost = INF;

        if (houses[index] != 0) {
            // House already painted, check if it forms a new neighborhood
            int newNeighborhoods = neighborhoods + ((houses[index] != prevColor) ? 1 : 0);
            minCost = dfs(houses, cost, index + 1, newNeighborhoods, houses[index], target, n);
        } else {
            // House needs to be painted → try all colors
            for (int color = 1; color <= n; color++) {
                int newNeighborhoods = neighborhoods + ((color != prevColor) ? 1 : 0);
                int currentCost = cost[index][color - 1] + dfs(houses, cost, index + 1, newNeighborhoods, color, target, n);
                minCost = Math.min(minCost, currentCost);
            }
        }

        // Store result in memoization cache
        return memo[index][neighborhoods][prevColor] = minCost;
    }
}
