package DynamicProgramming;

import java.util.Arrays;


/**
 * LeetCode 265: Paint House II
 *
 * Problem Statement:
 * There are `n` houses to be painted. Each house can be painted with one of `k` colors.
 * The cost of painting house `i` with color `j` is given by `costs[i][j]`.
 * No two adjacent houses can be painted with the same color.
 * Return the minimum cost to paint all houses.
 *
 * Example:
 * Input: costs = [
 * [1,5,3],
 * [2,9,4]]
 * Output: 5
 * Explanation: Paint house 0 with color 0 and house 1 with color 2.
 *
 * LeetCode: https://leetcode.com/problems/paint-house-ii/
 *
 * Follow-Up Questions for FAANG Interviews:
 * 1. Q: Can you reduce the time complexity from O(n*k²) to O(n*k)?
 *    A: Yes. Track the minimum and second minimum cost of the previous row to avoid inner loop.
 *    See optimized method `minCostOptimized()`.
 *
 * 2. Q: What if we are allowed to paint with the same color if the cost is less?
 *    A: That violates the constraint. But if allowed, we drop the adjacent color check.
 *
 * 3. Q: Can we reconstruct the color sequence?
 *    A: Yes. Maintain a parent pointer or backtrack from minimum DP state.
 *
 * 4. Q: What if some colors are forbidden for some houses?
 *    A: Mark those costs as `Integer.MAX_VALUE` and skip them in the loop.
 */
public class PaintHouse2 {

  public static void main(String[] args) {
    PaintHouse2 solver = new PaintHouse2();
    int[][] costs = {{1, 5, 3}, {2, 9, 4}};

    System.out.println("Minimum Cost (Brute Force): " + solver.minCost(costs));
    System.out.println("Minimum Cost (Optimized): " + solver.minCostOptimized(costs));
  }

  /**
   * Brute-force DP solution using 2D table
   *
   * Steps:
   * 1. Create a DP table where dp[i][j] represents the minimum cost to paint up to house `i` with color `j`.
   * 2. For each house and color, choose the minimum cost from previous house with a different color.
   * 3. Return minimum of the last house's color costs.
   *
   * Time Complexity: O(n * k * k)
   * Space Complexity: O(n * k)
   */
  public int minCost(int[][] costs) {
    if (costs == null || costs.length == 0 || costs[0].length == 0) {
      return 0;
    }

    int numHouses = costs.length;
    int numColors = costs[0].length;
    int[][] dp = new int[numHouses][numColors];

    // Initialize first house
    System.arraycopy(costs[0], 0, dp[0], 0, numColors);

    for (int house = 1; house < numHouses; house++) {
      for (int currColor = 0; currColor < numColors; currColor++) {
        int minPrev = Integer.MAX_VALUE;
        for (int prevColor = 0; prevColor < numColors; prevColor++) {
          if (prevColor != currColor) {
            minPrev = Math.min(minPrev, dp[house - 1][prevColor]);
          }
        }
        dp[house][currColor] = costs[house][currColor] + minPrev;
      }
    }

    // Find the minimum number in the last row
    return Arrays.stream(dp[numHouses - 1]).min().orElse(Integer.MAX_VALUE);
  }

  /**
   * Optimized DP solution using tracking of minimum and second minimum costs across colors
   *
   * Steps:
   * 1. For each house, we keep track of:
   *    - The index of the color with the **lowest total cost** from the previous house.
   *    - The index of the color with the **second-lowest total cost** from the previous house.
   * 2. When calculating the cost for a current color:
   *    - If it's different from the color with previous min cost → add that min cost.
   *    - If it's the same → we must add the second min cost to avoid adjacent duplicates.
   * 3. Update min/secondMin cost indices for the current house.
   *
   * Time Complexity: O(n * k)
   * Space Complexity: O(1) extra (in-place cost update)
   */
  public int minCostOptimized(int[][] costs) {
    if (costs == null || costs.length == 0 || costs[0].length == 0) {
      return 0;
    }

    int numHouses = costs.length;
    int numColors = costs[0].length;

    // These hold the index of the colors with the lowest and second-lowest total costs from the previous row
    int prevMinCostColorIndex = -1;
    int prevSecondMinCostColorIndex = -1;

    for (int house = 0; house < numHouses; house++) {
      // Temporary variables to track the current row's lowest and second-lowest total cost color indices
      int currMinCostColorIndex = -1;
      int currSecondMinCostColorIndex = -1;

      for (int color = 0; color < numColors; color++) {
        if (house > 0) {
          if (color != prevMinCostColorIndex) {
            // Use previous min cost if color is different
            costs[house][color] += costs[house - 1][prevMinCostColorIndex];
          } else {
            // Otherwise use second min (to avoid same color as previous house)
            costs[house][color] += costs[house - 1][prevSecondMinCostColorIndex];
          }
        }

        // Update currMinCostColorIndex and currSecondMinCostColorIndex for the current house
        if (currMinCostColorIndex == -1 || costs[house][color] < costs[house][currMinCostColorIndex]) {
          currSecondMinCostColorIndex = currMinCostColorIndex;
          currMinCostColorIndex = color;
        } else if (currSecondMinCostColorIndex == -1
            || costs[house][color] < costs[house][currSecondMinCostColorIndex]) {
          currSecondMinCostColorIndex = color;
        }
      }

      // Update for next iteration
      prevMinCostColorIndex = currMinCostColorIndex;
      prevSecondMinCostColorIndex = currSecondMinCostColorIndex;
    }

    return costs[numHouses - 1][prevMinCostColorIndex];
  }
}