package dynamicprogramming.knapsackunbounded;

import java.util.Arrays;

/**
 * Problem: Coin Change II
 *
 * Count combinations that form a target amount when every coin denomination can be used unlimited times. Different orders of the same coins count as one combination.
 *
 * Leetcode: https://leetcode.com/problems/coin-change-ii/ (Medium)
 * Rating:   not available (not a contest problem)
 * Pattern:  Dynamic programming | Unbounded knapsack | Combination counting
 *
 * Example:
 *   Input:  amount = 4, coins = [1, 2, 3]
 *   Output: 4
 *   Why:    the combinations are [1,1,1,1], [1,1,2], [2,2], and [1,3].
 *
 * Follow-ups:
 *   1. How would you return an actual solution, not only the value?
 *      Store predecessor or choice information while filling the same states.
 *   2. How can space be reduced?
 *      Keep only the previous row or active states when the recurrence allows it.
 *   3. How would constraints such as fees, limits, or weights change it?
 *      Add the constraint to the state or transition and keep the same invariant.
 *
 * Related: Coin Change (322), Combination Sum IV (377).
 */
public class CoinChange2 {

    public static void main(String[] args) {
    int[][] coinCases = { {1, 2, 5}, {2}, {1} };
    int[] amounts = {5, 3, 0};
    int[] expected = {4, 0, 1};
    for (int i = 0; i < coinCases.length; i++) {
      int got = countCombinationsOptimized(coinCases[i], amounts[i]);
      System.out.printf("coins=%s amount=%d -> %d  expected=%d%n", Arrays.toString(coinCases[i]), amounts[i], got, expected[i]);
    }
  }

  /**
   * Recursive solution to count coin combinations
   *
   * Steps:
   * 1. Base cases: if target is 0 return 1 (one way), if target < 0 or no coins left return 0
   * 2. For each coin, we have two choices: include it or exclude it
   * 3. Include: reduce target by coin value, keep same coin index (unlimited use)
   * 4. Exclude: move to next coin without changing target
   * 5. Return sum of both choices
   *
   * Algorithm: Recursion with choice exploration
   * Time Complexity: O(2^(amount + coins.length)) - exponential due to overlapping subproblems
   * Space Complexity: O(amount + coins.length) - recursion stack depth
   *
   * @param coinDenominations array of available coin denominations
   * @param targetAmount the target sum to achieve
   * @return number of ways to make the target amount
   */
  static int countCombinationsRecursive(int[] coinDenominations, int targetAmount) {
    return countCombinationsHelper(targetAmount, coinDenominations, 0);
  }

  private static int countCombinationsHelper(int remainingAmount, int[] coinDenominations, int currentCoinIndex) {
    // Base case: exact amount reached
      if (remainingAmount == 0) {
          return 1;
      }

    // Base case: amount exceeded or no more coins
      if (remainingAmount < 0 || currentCoinIndex >= coinDenominations.length) {
          return 0;
      }

    // Include current coin (can use same coin again due to unlimited supply)
    int waysIncludingCurrentCoin = countCombinationsHelper(remainingAmount - coinDenominations[currentCoinIndex],
        coinDenominations, currentCoinIndex);

    // Exclude current coin and move to next
    int waysExcludingCurrentCoin = countCombinationsHelper(remainingAmount, coinDenominations, currentCoinIndex + 1);

    return waysIncludingCurrentCoin + waysExcludingCurrentCoin;
  }

    /**
   * Intuition: dp[coinIndex][currentAmount] counts combinations for currentAmount using the first coinIndex coins. Skipping the current coin uses the row above; taking it stays in the same row at currentAmount - coin value because supply is unlimited.
   *
   * Algorithm:
   *   1. Reject invalid inputs.
   *   2. Create dp[numberOfCoins + 1][targetAmount + 1].
   *   3. Set every dp[coinIndex][0] to 1.
   *   4. Fill coinIndex from 1..numberOfCoins and currentAmount from 1..targetAmount.
   *   5. Use skip plus take counts when the coin fits; otherwise copy skip.
   *
   * Time:  O(numberOfCoins * targetAmount) - each cell is filled once.
   * Space: O(numberOfCoins * targetAmount) - stores the full table.
   *
   * @param coinDenominations available coin denominations
   * @param targetAmount amount to form
   * @return number of combinations
   */
public static int countCombinationsDP(int[] coinDenominations, int targetAmount) {
    if (coinDenominations == null || coinDenominations.length == 0 || targetAmount < 0) {
      return 0;
    }

    int numberOfCoins = coinDenominations.length;
    int[][] dp = new int[numberOfCoins + 1][targetAmount + 1]; // dp[i][j] = ways to make amount j using first i coins

    // Base case: one way to make amount 0 (use no coins)
    for (int coinIndex = 0; coinIndex <= numberOfCoins; coinIndex++) {
      dp[coinIndex][0] = 1;
    }

    // Fill DP table
    for (int coinIndex = 1; coinIndex <= numberOfCoins; coinIndex++) {
      for (int currentAmount = 1; currentAmount <= targetAmount; currentAmount++) {
        int currentCoinValue = coinDenominations[coinIndex - 1];

        if (currentCoinValue <= currentAmount) {
          // Ways without current coin + ways with current coin
          dp[coinIndex][currentAmount] = dp[coinIndex - 1][currentAmount]   // ways without current coin
              + dp[coinIndex][currentAmount - currentCoinValue];            // ways with current coin
        } else {
          // Cannot use current coin, take ways without it
          dp[coinIndex][currentAmount] = dp[coinIndex - 1][currentAmount];
        }
      }
    }

    return dp[numberOfCoins][targetAmount];
  }

  /**
   * Space-optimized Dynamic Programming solution
   *
   * Steps:
   * 1. Use 1D DP array where dp[i] represents ways to make amount i
   * 2. Initialize dp[0] = 1 (one way to make amount 0)
   * 3. For each coin, update dp array from coin value to target amount
   * 4. dp[amount] += dp[amount - coin] for each valid amount
   *
   * Algorithm: Dynamic Programming (Space-optimized)
   * Time Complexity: O(coins.length * targetAmount)
   * Space Complexity: O(targetAmount)
   *
   * @param coinDenominations array of available coin denominations
   * @param targetAmount the target sum to achieve
   * @return number of ways to make the target amount
   */
  public static int countCombinationsOptimized(int[] coinDenominations, int targetAmount) {
    if (coinDenominations == null || coinDenominations.length == 0 || targetAmount < 0) {
      return 0;
    }

    int[] dp = new int[targetAmount + 1]; // dp[i] = number of ways to make amount i
    dp[0] = 1; // One way to make amount 0

    // Process each coin
    for (int coinValue : coinDenominations) {
      // Update dp array for all amounts that can use this coin
      for (int currentAmount = coinValue; currentAmount <= targetAmount; currentAmount++) {
        dp[currentAmount] += dp[currentAmount - coinValue];
      }
    }

    return dp[targetAmount];
  }
}