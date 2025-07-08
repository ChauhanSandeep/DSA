package DynamicProgramming.KnapsackUnbounded;

import java.util.Arrays;


/**
 * Problem: Rod Cutting Problem (Unbounded Knapsack)
 *
 * Given a rod of length `N` and an array `prices` where `prices[i]` is the price of a rod piece of length `i + 1`,
 * determine the maximum total value obtainable by cutting up the rod and selling the pieces.
 * You can reuse any piece length any number of times.
 *
 * Example:
 * prices = [1, 5, 8, 9], rodLength = 4
 * Cuts = [2,2] → total = 5 + 5 = 10 (max possible)
 *
 * Output:
 * Maximum Profit: 10
 *
 * Leetcode-style Link (equivalent): https://www.geeksforgeeks.org/cutting-a-rod-dp-13/
 * Follow-ups:
 * - How would you optimize space? → Use a 1D DP array
 * - How would you recover the cut sequence? → Track choices in another matrix
 */
public class RodCutting {
  public static void main(String[] args) {
    int[] prices = {1, 5, 8, 9}; // Prices for rod pieces of length 1, 2, 3, 4
    int rodLength = prices.length;
    int maxProfit = computeMaxProfitOptimized(prices, rodLength);
    System.out.println("Maximum Profit: " + maxProfit);
  }

  /**
   * Bottom-up DP approach (2D Table) for Unbounded Knapsack (Rod Cutting).
   *
   * Time Complexity: O(N^2) where N = rodLength
   * Space Complexity: O(N^2)
   *
   * @param prices Array where prices[i] is price of rod piece length i+1
   * @param rodLength Total length of the rod
   * @return Maximum profit
   */
  public static int computeMaxProfit(int[] prices, int rodLength) {
    int[][] dp = new int[rodLength + 1][rodLength + 1]; // dp[pieceLength][currentRodLength]

    for (int pieceLength = 1; pieceLength <= rodLength; pieceLength++) {
      for (int currentRodLength = 1; currentRodLength <= rodLength; currentRodLength++) {
        if (pieceLength <= currentRodLength) {
          // Include the piece (can be used multiple times)
          dp[pieceLength][currentRodLength] =
              Math.max(prices[pieceLength - 1] + dp[pieceLength][currentRodLength - pieceLength], // pick
                  dp[pieceLength - 1][currentRodLength] // don't pick
              );
        } else {
          dp[pieceLength][currentRodLength] = dp[pieceLength - 1][currentRodLength];
        }
      }
    }

    return dp[rodLength][rodLength];
  }

  /**
   * Space-optimized 1D DP solution.
   *
   * Time Complexity: O(N^2)
   * Space Complexity: O(N)
   *
   * @param prices Array of prices
   * @param rodLength Rod length
   * @return Maximum profit
   */
  public static int computeMaxProfitOptimized(int[] prices, int rodLength) {
    int[] dp = new int[rodLength + 1]; // dp[currentRodLength]

    for (int pieceLength = 1; pieceLength <= rodLength; pieceLength++) {
      for (int currentRodLength = pieceLength; currentRodLength <= rodLength; currentRodLength++) {
        dp[currentRodLength] =
            Math.max(dp[currentRodLength], prices[pieceLength - 1] + dp[currentRodLength - pieceLength]);
      }
    }

    return dp[rodLength];
  }
}
