package DynamicProgramming;

import java.util.Arrays;


/**
 * Paint House III
 *
 * Problem Statement:
 * There is a row of m houses in a small city, each house must be painted with one of n colors.
 * The cost of painting each house with a certain color is different. You have to paint all the houses
 * such that no two adjacent houses have the same color. Some houses are already painted (houses[i] != 0).
 * A neighborhood is a maximal group of continuous houses that are painted with the same color.
 * Return the minimum cost of painting all the remaining houses so that there are exactly target neighborhoods.
 * If it is not possible, return -1.
 *
 * Example:
 * Input: houses = [0,0,0,0,0],
 * cost = [
 * [1,10],
 * [10,1],
 * [10,1],
 * [1,10],
 * [5,1]
 * ], m = 5, n = 2, target = 3
 * Output: 9
 * Explanation: Paint houses with colors [1,2,2,1,1] to get 3 neighborhoods [1],[2,2],[1,1] with cost 1+1+1+1+5=9.
 *
 * LeetCode: https://leetcode.com/problems/paint-house-iii/
 *
 * Follow-up Questions for FAANG Interviews:
 * 1. Q: What if we want to minimize neighborhoods instead of achieving exact target?
 *    A: Remove target constraint and track minimum neighborhoods formed. Use similar DP structure.
 *
 * 2. Q: How would you handle the case where some colors are forbidden for certain houses?
 *    A: Add validation in color selection loop to skip forbidden colors for each house.
 *
 * 3. Q: Can you solve this with bottom-up DP instead of top-down?
 *    A: Yes, iterate houses from left to right, filling DP table for all states systematically.
 *
 * 4. Q: What if we need to return the actual color assignment, not just the cost?
 *    A: Maintain parent pointers in DP state or reconstruct path from memoization table.
 *
 * 5. Q: How would you optimize space complexity?
 *    A: Use rolling array technique since we only need previous house's state. (LeetCode 256, 265)
 */
public class PaintHouse3 {
  private Integer[][][] dp;
  private static final int IMPOSSIBLE_COST = Integer.MAX_VALUE / 2;

  public static void main(String[] args) {
    PaintHouse3 solution = new PaintHouse3();
    int[] houses = {0, 0, 0, 0, 0};
    int[][] paintingCosts = {{1, 10}, {10, 1}, {10, 1}, {1, 10}, {5, 1}};
    int numHouses = 5, numColors = 2, targetNeighborhoods = 3;

    System.out.println(
        "Minimum cost: " + solution.minCost(houses, paintingCosts, numHouses, numColors, targetNeighborhoods));
    System.out.println("Bottom-up solution: " + solution.minCostBottomUp(houses, paintingCosts, numHouses, numColors,
        targetNeighborhoods));
  }

  /**
   * Top-down DP solution to find minimum cost for painting houses
   *
   * Steps:
   * 1. Use memoization with state (houseIndex, neighborhoodCount, previousColor)
   * 2. For each house, either it's pre-painted or we try all possible colors
   * 3. Track neighborhoods by checking if current color differs from previous
   * 4. Return minimum cost among all valid color assignments
   * 5. Use pruning when neighborhoods exceed target (impossible to achieve exact target)
   *
   * Algorithm: Top-Down Dynamic Programming with Memoization
   * Time Complexity: O(m * target * n * n) = O(m * target * n²)
   * Space Complexity: O(m * target * n) for memoization + O(m) recursion stack
   *
   * @param houses array where 0 means unpainted, >0 means painted with that color
   * @param paintingCosts cost[i][j] is cost to paint house i with color j+1
   * @param numHouses number of houses
   * @param numColors number of available colors
   * @param targetNeighborhoods exact number of neighborhoods required
   * @return minimum cost to achieve target neighborhoods, -1 if impossible
   */
  public int minCost(int[] houses, int[][] paintingCosts, int numHouses, int numColors, int targetNeighborhoods) {
    if (houses == null || paintingCosts == null || numHouses == 0 || numColors == 0 || targetNeighborhoods <= 0) {
      return -1;
    }

    // Memoization table: [houseIndex][neighborhoodCount][previousColor]
    // previousColor ranges from 0 to numColors (0 means no previous color)
    // dp[i][j][k] = minimum cost to paint houses from i to end with j neighborhoods and previous color k
    dp = new Integer[numHouses][targetNeighborhoods + 1][numColors + 1];

    int minimumCost = findMinimumCostRecursive(houses, paintingCosts, 0, 0, 0, targetNeighborhoods, numColors);

    return (minimumCost >= IMPOSSIBLE_COST) ? -1 : minimumCost;
  }

  private int findMinimumCostRecursive(int[] houses, int[][] paintingCosts, int currentHouseIndex,
      int currentNeighborhoods, int previousHouseColor, int targetNeighborhoods, int numColors) {
    // Base case: processed all houses
    if (currentHouseIndex == houses.length) {
      return (currentNeighborhoods == targetNeighborhoods) ? 0 : IMPOSSIBLE_COST;
    }

    // Pruning: too many neighborhoods already formed
    if (currentNeighborhoods > targetNeighborhoods) {
      return IMPOSSIBLE_COST;
    }

    // Pruning: impossible to reach target with remaining houses
    int remainingHouses = houses.length - currentHouseIndex;
    if (currentNeighborhoods + remainingHouses < targetNeighborhoods) {
      return IMPOSSIBLE_COST;
    }

    // Check memoization cache
    if (dp[currentHouseIndex][currentNeighborhoods][previousHouseColor] != null) {
      return dp[currentHouseIndex][currentNeighborhoods][previousHouseColor];
    }

    int minimumCostFromCurrentState = IMPOSSIBLE_COST;

    if (houses[currentHouseIndex] != 0) {
      // House is already painted
      int currentHouseColor = houses[currentHouseIndex];
      int newNeighborhoodCount = currentNeighborhoods + ((currentHouseColor != previousHouseColor) ? 1 : 0);

      minimumCostFromCurrentState =
          findMinimumCostRecursive(houses, paintingCosts, currentHouseIndex + 1, newNeighborhoodCount,
              currentHouseColor, targetNeighborhoods, numColors);
    } else {
      // House needs to be painted - try all available colors
      for (int colorChoice = 1; colorChoice <= numColors; colorChoice++) {
        int newNeighborhoodCount = currentNeighborhoods + ((colorChoice != previousHouseColor) ? 1 : 0);

        int costForThisColor = paintingCosts[currentHouseIndex][colorChoice - 1]
            + findMinimumCostRecursive(houses, paintingCosts, currentHouseIndex + 1,
            newNeighborhoodCount, colorChoice, targetNeighborhoods, numColors);

        minimumCostFromCurrentState = Math.min(minimumCostFromCurrentState, costForThisColor);
      }
    }

    // Cache and return result
    dp[currentHouseIndex][currentNeighborhoods][previousHouseColor] = minimumCostFromCurrentState;
    return minimumCostFromCurrentState;
  }

  /**
   * Bottom-up DP solution for comparison
   *
   * Steps:
   * 1. Create 3D DP table: dp[house][neighborhoods][color]
   * 2. Initialize base cases for first house
   * 3. Fill table iteratively for each house, neighborhood count, and color
   * 4. Consider both pre-painted and unpainted houses
   * 5. Return minimum cost among all valid final states
   *
   * Algorithm: Bottom-Up Dynamic Programming
   * Time Complexity: O(m * target * n²)
   * Space Complexity: O(m * target * n)
   *
   * @param houses array where 0 means unpainted, >0 means painted with that color
   * @param paintingCosts cost matrix for painting
   * @param numHouses number of houses
   * @param numColors number of available colors
   * @param targetNeighborhoods exact number of neighborhoods required
   * @return minimum cost to achieve target neighborhoods, -1 if impossible
   */
  public int minCostBottomUp(int[] houses, int[][] paintingCosts, int numHouses, int numColors,
      int targetNeighborhoods) {
    if (houses == null || paintingCosts == null || numHouses == 0 || numColors == 0 || targetNeighborhoods <= 0) {
      return -1;
    }

    // DP table: dp[house][neighborhoods][color] = minimum cost
    int[][][] dp = new int[numHouses][targetNeighborhoods + 1][numColors + 1];

    // Initialize with impossible costs
    for (int i = 0; i < numHouses; i++) {
      for (int j = 0; j <= targetNeighborhoods; j++) {
        Arrays.fill(dp[i][j], IMPOSSIBLE_COST);
      }
    }

    // Base case: first house
    if (houses[0] != 0) {
      // First house is pre-painted
      dp[0][1][houses[0]] = 0;
    } else {
      // First house needs painting
      for (int color = 1; color <= numColors; color++) {
        dp[0][1][color] = paintingCosts[0][color - 1];
      }
    }

    // Fill DP table for remaining houses
    for (int houseIndex = 1; houseIndex < numHouses; houseIndex++) {
      for (int neighborhoodCount = 1; neighborhoodCount <= targetNeighborhoods; neighborhoodCount++) {

        if (houses[houseIndex] != 0) {
          // Current house is pre-painted
          int currentColor = houses[houseIndex];

          // Same color as previous house (no new neighborhood)
          if (dp[houseIndex - 1][neighborhoodCount][currentColor] < IMPOSSIBLE_COST) {
            dp[houseIndex][neighborhoodCount][currentColor] = Math.min(dp[houseIndex][neighborhoodCount][currentColor],
                dp[houseIndex - 1][neighborhoodCount][currentColor]);
          }

          // Different color from previous house (new neighborhood)
          if (neighborhoodCount > 1) {
            for (int prevColor = 1; prevColor <= numColors; prevColor++) {
              if (prevColor != currentColor && dp[houseIndex - 1][neighborhoodCount - 1][prevColor] < IMPOSSIBLE_COST) {
                dp[houseIndex][neighborhoodCount][currentColor] =
                    Math.min(dp[houseIndex][neighborhoodCount][currentColor],
                        dp[houseIndex - 1][neighborhoodCount - 1][prevColor]);
              }
            }
          }
        } else {
          // Current house needs painting
          for (int currentColor = 1; currentColor <= numColors; currentColor++) {
            int paintingCost = paintingCosts[houseIndex][currentColor - 1];

            // Same color as previous house (no new neighborhood)
            if (dp[houseIndex - 1][neighborhoodCount][currentColor] < IMPOSSIBLE_COST) {
              dp[houseIndex][neighborhoodCount][currentColor] =
                  Math.min(dp[houseIndex][neighborhoodCount][currentColor],
                      dp[houseIndex - 1][neighborhoodCount][currentColor] + paintingCost);
            }

            // Different color from previous house (new neighborhood)
            if (neighborhoodCount > 1) {
              for (int prevColor = 1; prevColor <= numColors; prevColor++) {
                if (prevColor != currentColor
                    && dp[houseIndex - 1][neighborhoodCount - 1][prevColor] < IMPOSSIBLE_COST) {
                  dp[houseIndex][neighborhoodCount][currentColor] =
                      Math.min(dp[houseIndex][neighborhoodCount][currentColor],
                          dp[houseIndex - 1][neighborhoodCount - 1][prevColor] + paintingCost);
                }
              }
            }
          }
        }
      }
    }

    // Find minimum cost among all colors for the last house with target neighborhoods
    int result = IMPOSSIBLE_COST;
    for (int color = 1; color <= numColors; color++) {
      result = Math.min(result, dp[numHouses - 1][targetNeighborhoods][color]);
    }

    return (result >= IMPOSSIBLE_COST) ? -1 : result;
  }
}