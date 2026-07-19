package dynamicprogramming.statemachine;

import java.util.Arrays;


/**
 * Problem: Paint House II
 *
 * Paint n houses using k colors. costs[i][j] is the cost to paint house i with
 * color j, and adjacent houses cannot use the same color. Return the minimum
 * total cost.
 *
 * Leetcode: https://leetcode.com/problems/paint-house-ii/
 * Rating:   acceptance 57.2% (Hard) - no contest Elo (pre-contest problem)
 * Pattern:  Dynamic Programming | State machine | Minimum and second-minimum color states
 *
 * Example:
 *   Input:  costs = [[1,5,3],[2,9,4]]
 *   Output: 5
 *   Why:    choose color 0 for the first house and color 2 for the second, for total 1 + 4.
 *
 * Follow-ups:
 *   1. Can you reduce O(n*k^2) to O(n*k)?
 *      Track the cheapest and second-cheapest colors from the previous row.
 *   2. Can you return the selected colors?
 *      Store parent color choices while computing DP, then backtrack from the last row.
 *   3. What if some colors are forbidden for some houses?
 *      Treat forbidden costs as impossible and skip them when updating min states.
 *
 * Related: Paint House (256), Paint House III (1473).
 */
public class PaintHouse2 {

    /**
   * Intuition: each color for the current house needs the cheapest previous color
   * that is not the same. The direct DP checks all previous colors for each current
   * color.
   *
   * Algorithm:
   *   1. Let dp[house][color] be the minimum cost ending with that color.
   *   2. Initialize the first row from costs.
   *   3. For every later house/color, try all different previous colors.
   *   4. Return the minimum value in the last row.
   *
   * Time:  O(n * k^2) - every house/color scans all previous colors.
   * Space: O(n * k) - DP table for all houses and colors.
   *
   * @param costs costs[house][color]
   * @return minimum cost with no equal adjacent colors
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
   * Intuition: to avoid scanning every previous color, keep the smallest and
   * second-smallest totals from the previous row. The same color must use the
   * second-smallest; every other color can use the smallest.
   *
   * Algorithm:
   *   1. Track previous row's minimum, second minimum, and minimum color index.
   *   2. For each current color, add costs to the compatible previous minimum.
   *   3. Build the current row's new minimum and second minimum.
   *   4. Return the final minimum total.
   *
   * Time:  O(n * k) - each house/color is processed once.
   * Space: O(1) - only rolling minimum states are stored.
   *
   * @param costs costs[house][color]
   * @return minimum cost with no equal adjacent colors
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


    public static void main(String[] args) {
        PaintHouse2 solver = new PaintHouse2();
        int[][][] inputs = {
            {},
            {{8}},
            {{1, 5, 3}, {2, 9, 4}}
        };
        int[] expected = {0, 8, 5};

        for (int i = 0; i < inputs.length; i++) {
            int output = solver.minCostOptimized(inputs[i]);
            System.out.printf("costs=%s  ->  %d  expected=%d%n",
                Arrays.deepToString(inputs[i]), output, expected[i]);
        }
    }

}