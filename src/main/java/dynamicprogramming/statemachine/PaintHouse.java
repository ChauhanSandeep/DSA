package dynamicprogramming.statemachine;

import java.util.Arrays;


/**
 * Problem: Paint House (Leetcode #256)
 * Link: https://leetcode.com/problems/paint-house/
 *
 * Problem Statement:
 * There is a row of n houses, where each house can be painted one of three colors: red, blue, or green.
 * The cost of painting each house with a certain color is given in a `costs` matrix of size n x 3.
 * - costs[i][0] is the cost of painting house i with red,
 * - costs[i][1] with blue,
 * - costs[i][2] with green.
 *
 * You cannot paint two adjacent houses with the same color.
 * Return the minimum cost to paint all the houses.
 *
 * Example:
 * Input: costs = [[17,2,17],[16,16,5],[14,3,19]]
 * Output: 10
 * Explanation:
 * - Paint house 0 with blue (cost = 2)
 * - Paint house 1 with green (cost = 5)
 * - Paint house 2 with blue (cost = 3)
 * Total cost = 2 + 5 + 3 = 10
 */
public class PaintHouse {

  /**
   * Recursive method to compute min cost using top-down DP.
   *
   * Intuition:
   * - Try all 3 color choices for each house recursively.
   * - Use memoization to cache results and avoid recomputation.
   *
   * Time Complexity: O(n * 3) => each house * each color
   * Space Complexity: O(n * 3) for memo table + O(n) recursion stack
   *
   * @param costs 2D array of painting costs
   * @return Minimum total cost to paint all houses
   */
  public int minCostRecursive(int[][] costs) {
    if (costs == null || costs.length == 0) return 0;

    int length = costs.length;
    int[][] memo = new int[length][3];

    // Fill memo with -1 to indicate uncomputed values
    for (int[] row : memo) {
      Arrays.fill(row, -1);
    }

    // Try all 3 starting colors
    return Math.min(
        dfs(0, 0, costs, memo),
        Math.min(dfs(0, 1, costs, memo),
                 dfs(0, 2, costs, memo))
    );
  }

  /**
   * Recursive helper function with memoization.
   *
   * @param i current house index
   * @param color current color (0=R, 1=G, 2=B)
   * @param costs cost matrix
   * @param memo memoization table
   * @return minimum cost to paint from house i to end, starting with given color
   */
  private int dfs(int i, int color, int[][] costs, int[][] memo) {
    if (i == costs.length) return 0;

    if (memo[i][color] != -1) {
      return memo[i][color];  // return cached result
    }

    int minCost = Integer.MAX_VALUE;

    // Try the other two colors for the next house
    for (int nextColor = 0; nextColor < 3; nextColor++) {
      if (nextColor != color) {
        int cost = costs[i][color] + dfs(i + 1, nextColor, costs, memo);
        minCost = Math.min(minCost, cost);
      }
    }

    memo[i][color] = minCost;  // cache result
    return minCost;
  }

  /**
   * Iterative DP approach to compute the minimum cost to paint all houses.
   *
   * Intuition:
   * - Start from the first house and move forward.
   * - At each house, compute the cost of painting it red, green, or blue,
   *   based on the minimum cost of painting the previous house with a different color.
   *
   * Steps:
   * 1. Iterate from house 1 to house n-1.
   * 2. For each color, update the cost by adding the minimum cost of the two other colors from the previous house.
   * 3. The minimum value in the last row gives the final result.
   *
   * Time Complexity: O(n), where n is number of houses
   * Space Complexity: O(1), in-place update of the input array
   *
   * @param costs 2D array of costs to paint each house with red, green, or blue
   * @return The minimum cost to paint all houses with no two adjacent houses having the same color
   */
  public int minCostIterative(int[][] costs) {
    if (costs == null || costs.length == 0) return 0;

    int length = costs.length;

    for (int i = 1; i < length; i++) {
      costs[i][0] += Math.min(costs[i - 1][1], costs[i - 1][2]); // Red
      costs[i][1] += Math.min(costs[i - 1][0], costs[i - 1][2]); // Green
      costs[i][2] += Math.min(costs[i - 1][0], costs[i - 1][1]); // Blue
    }

    return Math.min(costs[length - 1][0], Math.min(costs[length - 1][1], costs[length - 1][2]));
  }

    // For quick testing
    public static void main(String[] args) {
        PaintHouse ph = new PaintHouse();
        int[][] costs = {{17,2,17},{16,16,5},{14,3,19}};
        System.out.println("Minimum cost to paint all houses: " + ph.minCostIterative(costs)); // Expected: 10
    }
}