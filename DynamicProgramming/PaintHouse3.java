package DynamicProgramming;

import java.util.Arrays;

/**
 * https://leetcode.com/problems/paint-house-iii/
 */
public class PaintHouse3 {
    private Integer[][][] memo;
    private int MAX_COST;

    public int minCost(int[] houses, int[][] cost, int m, int n, int target) {
        this.memo = new Integer[100][100][21]; // [houseCount, neighborCount, colorCount]
        this. MAX_COST = 1000001; // maxCost

        int minCost = findMinCost(houses, cost, target, 0, 0, 0);
        return minCost == MAX_COST ? -1 : minCost;
    }

    int findMinCost(int[] houses, int[][] cost, int targetCount, int currIndex, int neighborCount, int prevColor) {
        if (currIndex == houses.length) return neighborCount == targetCount ? 0 : MAX_COST;
        if (neighborCount > targetCount) return MAX_COST;
        if (memo[currIndex][neighborCount][prevColor] != null) return memo[currIndex][neighborCount][prevColor];

        int minCost = MAX_COST;
        if (houses[currIndex] != 0) {
            int newNeighborCount = neighborCount + (houses[currIndex] != prevColor ? 1 : 0);
            minCost = findMinCost(houses, cost, targetCount, currIndex + 1, newNeighborCount, houses[currIndex]);
        } else {
            int totalColors = cost[0].length;
            for (int color = 1; color <= totalColors; color++) {
                int newNeighborCount = neighborCount + (color != prevColor ? 1 : 0);
                int currCost = cost[currIndex][color - 1]
                        + findMinCost(houses, cost, targetCount, currIndex + 1, newNeighborCount, color);
                minCost = Math.min(minCost, currCost);
            }
        }
        return memo[currIndex][neighborCount][prevColor] = minCost;
    }
}
