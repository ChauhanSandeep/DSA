package dynamicprogramming.KnapsackUnbounded;

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
 * Leetcode-style link (similar): https://www.geeksforgeeks.org/cutting-a-rod-dp-13/
 *
 * Follow-up Questions:
 * - How would you optimize space?
 *   → Use a 1D DP array instead of 2D.
 *
 * - How would you reconstruct the actual cut sequence?
 *   → Track choices in a separate array (backtracking path).
 *     (Related problem: https://leetcode.com/problems/coin-change/)
 */
public class RodCutting {
  public static void main(String[] args) {
    int[] prices = {1, 5, 8, 9}; // Prices for rod pieces of length 1, 2, 3, 4
    int rodLength = prices.length;
    int maxProfit = computeMaxProfitOptimized(prices, rodLength);
    System.out.println("Maximum Profit: " + maxProfit);
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
   * Standard 2D Bottom-Up DP approach for Rod Cutting (Unbounded Knapsack).
   *
   * Algorithm:
   * - dp[i][j] represents the maximum profit using rod pieces up to length i for rod of length j.
   * - Either take the i-th piece (can be reused) or skip it.
   *
   * Time Complexity: O(N^2)
   * Space Complexity: O(N^2)
   *
   * @param prices Array where prices[i] is the price of a rod piece of length i+1
   * @param rodLength The total length of the rod
   * @return Maximum achievable profit
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
