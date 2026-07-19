package dynamicprogramming.statemachine;

import java.util.Arrays;

/**
 * Problem: Paint House
 *
 * Paint a row of houses using exactly three colors. costs[i][c] gives the cost
 * of painting house i with color c, and adjacent houses cannot share a color.
 * Return the minimum total cost.
 *
 * Leetcode: https://leetcode.com/problems/paint-house/
 * Rating:   acceptance 64.5% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Dynamic Programming | State machine | Color choice with previous-color constraint
 *
 * Example:
 *   Input:  costs = [[17,2,17],[16,16,5],[14,3,19]]
 *   Output: 10
 *   Why:    blue, green, blue costs 2 + 5 + 3 and no adjacent houses share a color.
 *
 * Follow-ups:
 *   1. What if there are k colors?
 *      Use Paint House II and track the minimum and second minimum previous colors.
 *   2. Can you return the color sequence?
 *      Store the previous color that produced each minimum and backtrack from the last house.
 *   3. What if some houses are already painted?
 *      Restrict the color loop for those houses to the fixed color only.
 *
 * Related: Paint House II (265), Paint House III (1473).
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
   * Intuition: the only state needed before painting a house is the color used on
   * the previous house. For each current color, choose the cheapest future among
   * the two different next colors.
   *
   * Algorithm:
   *   1. Try each color for the first house.
   *   2. Recursively paint the next house with any different color.
   *   3. Add the current painting cost to the best future cost.
   *   4. Memoize by house index and previous color.
   *
   * Time:  O(n * 3 * 3) - each house/color state tries three colors.
   * Space: O(n * 3) - memo table plus recursion depth.
   *
   * @param costs costs[house][color] for three colors
   * @return minimum cost to paint all houses with no equal adjacent colors
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

    /** Returns the minimum cost from houseIndex onward after using color previously. */
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
   * Intuition: the cost to paint a house a color is its own cost plus the cheaper
   * previous-house cost among the other two colors. Updating each row accumulates
   * the best total ending in each color.
   *
   * Algorithm:
   *   1. Treat each row as minimum total cost ending with that color.
   *   2. For every house after the first, add the minimum compatible previous color.
   *   3. Continue in place through the costs matrix.
   *   4. Return the minimum value in the final row.
   *
   * Time:  O(n) - three color states are updated per house.
   * Space: O(1) - the input cost matrix stores the DP totals.
   *
   * @param costs costs[house][color] for three colors
   * @return minimum cost to paint all houses with no equal adjacent colors
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
        PaintHouse solver = new PaintHouse();
        int[][][] inputs = {
            {},
            {{7, 6, 2}},
            {{17, 2, 17}, {16, 16, 5}, {14, 3, 19}}
        };
        int[] expected = {0, 2, 10};

        for (int i = 0; i < inputs.length; i++) {
            int[][] costsCopy = Arrays.stream(inputs[i]).map(int[]::clone).toArray(int[][]::new);
            int output = solver.minCostIterative(costsCopy);
            System.out.printf("costs=%s  ->  %d  expected=%d%n",
                Arrays.deepToString(inputs[i]), output, expected[i]);
        }
    }

}