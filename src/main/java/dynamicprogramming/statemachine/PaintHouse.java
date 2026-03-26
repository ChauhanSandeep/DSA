package dynamicprogramming.statemachine;

import java.util.Arrays;

/**
 * Problem: Paint House (Leetcode #256)
 * Link: https://leetcode.com/problems/paint-house/
 *
 * Problem Statement:
 * There is a row of n houses, where each house can be painted one of three
 * colors: red, blue, or green.
 * The cost of painting each house with a certain color is given in a `costs`
 * matrix of size n x 3.
 * - costs[i][0] is the cost of painting house i with red,
 * - costs[i][1] with blue,
 * - costs[i][2] with green.
 *
 * You cannot paint two adjacent houses with the same color.
 * Return the minimum cost to paint all the houses.
 *
 * Example:
 * Input: costs = [
 * // R, B, G
 * [17,2,17], // House 0
 * [16,16,5], // House 1
 * [14,3,19] // House 2
 * ]
 * Output: 10
 * Explanation:
 * - Paint house 0 with blue (cost = 2)
 * - Paint house 1 with green (cost = 5)
 * - Paint house 2 with blue (cost = 3)
 * Total cost = 2 + 5 + 3 = 10
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class PaintHouse {

  /**
   * Dynamic Programming Approach: Iterative Bottom-Up
   * Intuition:
   * - The cost to paint the current house with a certain color depends on the
   * minimum cost
   * of painting the previous house with a different color.
   * - We can build up a DP table where dp[i][c] represents the minimum cost to
   * paint up to house i with color c.
   * 
   * Steps:
   * 1. Initialize a DP table of size (n+1) x 3, where n is the number of houses.
   * 2. Fill the DP table iteratively, ensuring that no two adjacent houses have
   * the same color.
   * 3. The answer will be the minimum value in the last row of the DP table.
   * 
   * Time Complexity: O(n * 3 * 3) = O(n) — each house, each color, and checking
   * previous colors.
   * Space Complexity: O(n * 3) for the DP table, which can be optimized to O(1)
   * by only keeping track of the previous row.
   * 
   * @param costs
   * @return
   */
  int decorateCakes(int[][] costs) {
    if (costs == null || costs.length == 0) {
      return 0;
    }

    int numberOfCakes = costs.length;
    int numberOfColors = costs[0].length;

    // dp[i][c] = min cost to paint first i cakes
    // where cake i is painted with color c
    int[][] dp = new int[numberOfCakes + 1][numberOfColors];

    // Base case:
    // dp[0][*] = 0 (painting 0 cakes costs 0)
    // Java already initializes to 0, so no work needed.

    // Initialize all rows with max values
    for (int cake = 1; cake <= numberOfCakes; cake++) {
      for (int color = 0; color < numberOfColors; color++) {
        dp[cake][color] = Integer.MAX_VALUE;
      }
    }

    // Compute minimum costs for each cake
    for (int cake = 1; cake <= numberOfCakes; cake++) {
      // Try all previous colors
      for (int prevColor = 0; prevColor < numberOfColors; prevColor++) {

        // Try all current colors
        for (int currColor = 0; currColor < numberOfColors; currColor++) {

          if (currColor == prevColor) {
            continue; // adjacent cakes cannot have same color
          }

          int costIfChooseThisColor = dp[cake - 1][prevColor] +
              costs[cake - 1][currColor];

          dp[cake][currColor] = Math.min(dp[cake][currColor], costIfChooseThisColor);
        }
      }
    }

    // Final answer = minimum cost for the last cake
    int result = Integer.MAX_VALUE;
    for (int color = 0; color < numberOfColors; color++) {
      result = Math.min(result, dp[numberOfCakes][color]);
    }

    return result;
  }

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
    if (costs == null || costs.length == 0)
      return 0;

    int totalHouses = costs.length;
    int[][] memo = new int[totalHouses][3];

    // Fill memo with -1 to indicate uncomputed values
    for (int[] row : memo) {
      Arrays.fill(row, -1);
    }

    // Try all 3 starting colors
    return Math.min(
        dfs(0, 0, costs, memo),
        Math.min(dfs(0, 1, costs, memo),
            dfs(0, 2, costs, memo)));
  }

  /**
   * Recursive helper function with memoization.
   *
   * @param houseIndex current house index
   * @param color      current color (0=R, 1=G, 2=B)
   * @param costs      cost matrix
   * @param memo       memoization table
   * @return minimum cost to paint from house houseIndex to end, starting with
   *         given color
   */
  private int dfs(int houseIndex, int color, int[][] costs, int[][] memo) {
    if (houseIndex == costs.length)
      return 0;

    if (memo[houseIndex][color] != -1) {
      return memo[houseIndex][color]; // return cached result
    }

    int minCost = Integer.MAX_VALUE;

    // Try the other two colors for the next house
    for (int nextColor = 0; nextColor < 3; nextColor++) {
      if (nextColor != color) {
        int cost = costs[houseIndex][color] + dfs(houseIndex + 1, nextColor, costs, memo);
        minCost = Math.min(minCost, cost);
      }
    }

    memo[houseIndex][color] = minCost; // cache result
    return minCost;
  }

  /**
   * Iterative DP approach to compute the minimum cost to paint all houses.
   *
   * Intuition:
   * - Start from the first house and move forward.
   * - At each house, compute the cost of painting it red, green, or blue,
   * based on the minimum cost of painting the previous house with a different
   * color.
   *
   * Steps:
   * 1. Iterate from house 1 to house n-1.
   * 2. For each color, update the cost by adding the minimum cost of the two
   * other colors from the previous house.
   * 3. The minimum value in the last row gives the final result.
   *
   * Time Complexity: O(n), where n is number of houses
   * Space Complexity: O(1), in-place update of the input array
   *
   * @param costs 2D array of costs to paint each house with red, green, or blue
   * @return The minimum cost to paint all houses with no two adjacent houses
   *         having the same color
   */
  public int minCostIterative(int[][] costs) {
    if (costs == null || costs.length == 0)
      return 0;

    int totalHouses = costs.length;

    for (int houseIndex = 1; houseIndex < totalHouses; houseIndex++) {
      // The cost to paint current house with red is its own cost plus the minimum of
      // painting previous house with green or blue
      costs[houseIndex][0] += Math.min(costs[houseIndex - 1][1], costs[houseIndex - 1][2]);
      // The cost to paint current house with green is its own cost plus the minimum
      // of painting previous house with red or blue
      costs[houseIndex][1] += Math.min(costs[houseIndex - 1][0], costs[houseIndex - 1][2]);
      // The cost to paint current house with blue is its own cost plus the minimum of
      // painting previous house with red or green
      costs[houseIndex][2] += Math.min(costs[houseIndex - 1][0], costs[houseIndex - 1][1]);
    }

    return Math.min(costs[totalHouses - 1][0], Math.min(costs[totalHouses - 1][1], costs[totalHouses - 1][2]));
  }

  // For quick testing
  public static void main(String[] args) {
    PaintHouse ph = new PaintHouse();
    int[][] costs = { { 17, 2, 17 }, { 16, 16, 5 }, { 14, 3, 19 } };
    System.out.println("Minimum cost to paint all houses: " + ph.minCostIterative(costs)); // Expected: 10
  }
}