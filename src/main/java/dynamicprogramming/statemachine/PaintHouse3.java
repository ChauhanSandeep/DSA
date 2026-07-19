package dynamicprogramming.statemachine;

import java.util.Arrays;


/**
 * Problem: Paint House III
 *
 * Some houses may already be painted, and unpainted houses can be painted with
 * one of n colors at given costs. A neighborhood is a maximal consecutive block
 * of equal colors. Return the minimum cost to end with exactly target neighborhoods.
 *
 * Leetcode: https://leetcode.com/problems/paint-house-iii/
 * Rating:   2056 (zerotrac Elo)
 * Pattern:  Dynamic Programming | State machine | House, neighborhoods, previous color
 *
 * Example:
 *   Input:  houses = [0,0,0,0,0], cost = [[1,10],[10,1],[10,1],[1,10],[5,1]], target = 3
 *   Output: 9
 *   Why:    painting [1,1,2,1,1] costs 1 + 1 + 1 + 1 + 5 and forms exactly three neighborhoods.
 *
 * Follow-ups:
 *   1. Can space be optimized in bottom-up DP?
 *      Yes; each house only needs the previous house's states, so use two layers.
 *   2. Can you return the color assignment?
 *      Store parent color choices for each state and backtrack from the cheapest final state.
 *   3. What if the objective is at most target neighborhoods?
 *      Take the minimum over all final neighborhood counts from 1 through target.
 *
 * Related: Paint House (256), Paint House II (265).
 */
public class PaintHouse3 {
  private Integer[][][] dp;
  private static final int IMPOSSIBLE_COST = Integer.MAX_VALUE / 2;

    /**
   * Intuition: neighborhoods are created exactly when the current color differs
   * from the previous color. A DP state must therefore remember the house index,
   * neighborhoods formed so far, and previous color.
   *
   * Algorithm:
   *   1. Initialize memo for house, neighborhood count, and previous color.
   *   2. If a house is already painted, keep its color and update neighborhoods.
   *   3. If unpainted, try every color and add its painting cost.
   *   4. Return the cheapest path that ends with exactly targetNeighborhoods.
   *
   * Time:  O(numHouses * targetNeighborhoods * numColors^2) - states try colors.
   * Space: O(numHouses * targetNeighborhoods * numColors) - memo table plus stack.
   *
   * @param houses 0 for unpainted, otherwise the fixed color
   * @param paintingCosts paintingCosts[house][color - 1]
   * @param numHouses number of houses
   * @param numColors number of colors
   * @param targetNeighborhoods required neighborhood count
   * @return minimum cost, or -1 if impossible
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

    /** Returns the cheapest suffix cost from the current neighborhood state. */
  private int findMinimumCostRecursive(int[] houses, int[][] paintingCosts, int currentHouseIndex,
      int currentneighborhoodCount, int previousHouseColor, int targetNeighborhoods, int numColors) {
    // Base case: processed all houses
    if (currentHouseIndex == houses.length) {
      return (currentneighborhoodCount == targetNeighborhoods) ? 0 : IMPOSSIBLE_COST;
    }

    // Pruning: too many neighborhoods already formed
    if (currentneighborhoodCount > targetNeighborhoods) {
      return IMPOSSIBLE_COST;
    }

    // Pruning: impossible to reach target with remaining houses
    int remainingHouses = houses.length - currentHouseIndex;
    if (currentneighborhoodCount + remainingHouses < targetNeighborhoods) {
      return IMPOSSIBLE_COST;
    }

    // Check memoization cache
    if (dp[currentHouseIndex][currentneighborhoodCount][previousHouseColor] != null) {
      return dp[currentHouseIndex][currentneighborhoodCount][previousHouseColor];
    }

    int minimumCostFromCurrentState = IMPOSSIBLE_COST;

    if (houses[currentHouseIndex] != 0) {
      // House is already painted
      int currentHouseColor = houses[currentHouseIndex];
      int newNeighborhoodCount = currentneighborhoodCount + ((currentHouseColor != previousHouseColor) ? 1 : 0);

      minimumCostFromCurrentState =
          findMinimumCostRecursive(houses, paintingCosts, currentHouseIndex + 1, newNeighborhoodCount,
              currentHouseColor, targetNeighborhoods, numColors);
    } else {
      // House needs to be painted - try all available colors
      for (int colorChoice = 1; colorChoice <= numColors; colorChoice++) {
        int newNeighborhoodCount = currentneighborhoodCount + ((colorChoice != previousHouseColor) ? 1 : 0);

        int costForThisColor = paintingCosts[currentHouseIndex][colorChoice - 1]
            + findMinimumCostRecursive(houses, paintingCosts, currentHouseIndex + 1,
            newNeighborhoodCount, colorChoice, targetNeighborhoods, numColors);

        minimumCostFromCurrentState = Math.min(minimumCostFromCurrentState, costForThisColor);
      }
    }

    // Cache and return result
    dp[currentHouseIndex][currentneighborhoodCount][previousHouseColor] = minimumCostFromCurrentState;
    return minimumCostFromCurrentState;
  }

    /**
   * Intuition: the top-down state can be filled left to right. Each table entry
   * stores the cheapest way to paint through a house with a fixed color and a
   * fixed neighborhood count.
   *
   * Algorithm:
   *   1. Initialize impossible costs and seed the first house.
   *   2. For each next house, respect pre-painted colors or try all colors.
   *   3. Carry same-color transitions without adding a neighborhood.
   *   4. Add a neighborhood when the previous color differs, then take the best final color.
   *
   * Time:  O(numHouses * targetNeighborhoods * numColors^2) - transitions compare previous colors.
   * Space: O(numHouses * targetNeighborhoods * numColors) - full DP table.
   *
   * @param houses 0 for unpainted, otherwise the fixed color
   * @param paintingCosts paintingCosts[house][color - 1]
   * @param numHouses number of houses
   * @param numColors number of colors
   * @param targetNeighborhoods required neighborhood count
   * @return minimum cost, or -1 if impossible
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


    public static void main(String[] args) {
        PaintHouse3 solver = new PaintHouse3();
        int[][] houses = { {0}, {0, 0, 0, 0, 0}, {0, 2, 1, 2, 0} };
        int[][][] costs = {
            {{5, 3}},
            {{1, 10}, {10, 1}, {10, 1}, {1, 10}, {5, 1}},
            {{1, 10}, {10, 1}, {10, 1}, {1, 10}, {5, 1}}
        };
        int[] colors = {2, 2, 2};
        int[] targets = {1, 3, 3};
        int[] expected = {3, 9, 11};

        for (int i = 0; i < houses.length; i++) {
            int output = solver.minCostBottomUp(houses[i], costs[i], houses[i].length, colors[i], targets[i]);
            System.out.printf("houses=%s target=%d  ->  %d  expected=%d%n",
                Arrays.toString(houses[i]), targets[i], output, expected[i]);
        }
    }

}