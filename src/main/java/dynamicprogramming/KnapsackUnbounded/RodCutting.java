package dynamicprogramming.knapsackunbounded;

import java.util.Arrays;

/**
 * Problem: Rod Cutting
 *
 * Given prices for rod pieces of each length, cut a rod to maximize total sale value. Any piece length may be reused, making this unbounded knapsack.
 *
 * Leetcode: https://www.geeksforgeeks.org/cutting-a-rod-dp-13/ (Classic DP)
 * Rating:   not available (not a Leetcode contest problem)
 * Pattern:  Dynamic programming | Unbounded knapsack | Max profit
 *
 * Example:
 *   Input:  prices = [1, 5, 8, 9], rodLength = 4
 *   Output: 10
 *   Why:    two pieces of length 2 earn 5 + 5, which beats selling length 4 for 9.
 *
 * Follow-ups:
 *   1. How would you return an actual solution, not only the value?
 *      Store predecessor or choice information while filling the same states.
 *   2. How can space be reduced?
 *      Keep only the previous row or active states when the recurrence allows it.
 *   3. How would constraints such as fees, limits, or weights change it?
 *      Add the constraint to the state or transition and keep the same invariant.
 *
 * Related: Coin Change (322), Integer Break (343).
 */
public class RodCutting {
    public static void main(String[] args) {
    int[][] priceCases = { {1, 5, 8, 9}, {3, 5, 8, 9, 10, 17, 17, 20}, {} };
    int[] rodLengths = {4, 8, 0};
    int[] expected = {10, 24, 0};
    for (int i = 0; i < priceCases.length; i++) {
      int got = computeMaxProfitOptimized(priceCases[i], rodLengths[i]);
      System.out.printf("prices=%s rodLength=%d -> %d  expected=%d%n", Arrays.toString(priceCases[i]), rodLengths[i], got, expected[i]);
    }
  }

  /**
   * Recursive approach for Rod Cutting (Unbounded Knapsack).
   *
   * Algorithm:
   * - Recursively try to cut the rod into pieces of length i and calculate the maximum profit.
   * - Base case: If rod length is 0, profit is 0.
   * - For each piece length, either include it (if it fits) or skip it.
   * - Take the maximum of both options.
   *
   * Time Complexity: O(N^2)
   * Space Complexity: O(N) (due to recursion stack)
   *
   * @param prices Array where prices[i] is the price of a rod piece of length i+1
   * @param rodLength The total length of the rod
   * @return Maximum achievable profit
   */
  public static int getMaxProfitRecursive(int[] prices, int rodLength) {
    Integer[][] dp = new Integer[rodLength + 1][rodLength + 1]; // dp[remainingLength][pieceLength]
    return getMaxProfitRecHelper(prices, rodLength, 1, dp);
  }

  /**
   * @param prices Array where prices[i] is the price of rod piece of length i+1
   * @param remainingLength Remaining rod length to be cut
   * @param pieceLength Current piece length being considered (starts from 1)
   * @param dp Memoization table: dp[remainingLength][pieceLength]
   * @return Maximum profit
   */
  public static int getMaxProfitRecHelper(int[] prices, int remainingLength, int pieceLength, Integer[][] dp) {
    // Base case: no rod left or all pieces tried
    if (pieceLength > prices.length || remainingLength == 0) return 0;

    if (dp[remainingLength][pieceLength] != null) {
      return dp[remainingLength][pieceLength];
    }

    // Option 1: Include the current piece if it fits
    int include = 0;
    if (pieceLength <= remainingLength) {
      include = prices[pieceLength - 1] +
          getMaxProfitRecHelper(prices, remainingLength - pieceLength, pieceLength, dp);
    }
    // Option 2: Exclude the current piece and try the next piece length
    int exclude = getMaxProfitRecHelper(prices, remainingLength, pieceLength + 1, dp);

    // Store the result in the memoization table
    dp[remainingLength][pieceLength] = Math.max(include, exclude);
    return dp[remainingLength][pieceLength];
  }

    /**
   * Intuition: dp[pieceLength][currentRodLength] is the best profit for currentRodLength using piece sizes up to pieceLength. Taking a piece stays on the same row because the same length can be reused; skipping it moves to the previous row.
   *
   * Algorithm:
   *   1. Create dp[rodLength + 1][rodLength + 1].
   *   2. Iterate pieceLength from 1 through rodLength.
   *   3. Iterate currentRodLength from 1 through rodLength.
   *   4. If the piece fits, choose max(take, skip).
   *   5. Otherwise copy the previous-row value.
   *
   * Time:  O(rodLength^2) - every piece length is checked for every rod length.
   * Space: O(rodLength^2) - stores the full table.
   *
   * @param prices prices for piece lengths 1..n
   * @param rodLength total rod length
   * @return maximum profit
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
   * Space-Optimized 1D DP approach for Rod Cutting.
   *
   * Algorithm:
   * - dp[j] stores the max profit for rod of length j.
   * - For each piece length, update dp from pieceLen to rodLength.
   *
   * Time Complexity: O(N^2)
   * Space Complexity: O(N)
   *
   * @param prices Array where prices[i] is the price of a rod piece of length i+1
   * @param rodLength Total length of the rod
   * @return Maximum achievable profit
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
