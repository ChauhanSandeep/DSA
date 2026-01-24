package dynamicprogramming.knapsackunbounded;

/**
 * Coin Change II - Number of Ways to Make Change
 *
 * Problem Statement:
 * You are given an integer array coins representing coins of different denominations and an integer amount
 * representing a total amount of money. Return the number of combinations that make up that amount.
 * If that amount of money cannot be made up by any combination of the coins, return 0.
 * You may assume that you have an infinite number of each kind of coin.
 *
 * Example:
 * Input: amount = 4, coins = [1,2,3]
 * Output: 4
 * Explanation: there are four ways to make up the amount:
 * 4=1+1+1+1
 * 4=1+1+2
 * 4=2+2
 * 4=1+3
 *
 * LeetCode: https://leetcode.com/problems/coin-change-2/
 *
 * Follow-up Questions for FAANG Interviews:
 * 1. Q: What if we need to find the minimum number of coins to make the amount?
 *    A: Use DP with dp[i] = min(dp[i], dp[i-coin] + 1) for each coin. (LeetCode 322)
 *
 * 2. Q: What if we need to return the actual combinations instead of just the count?
 *    A: Use backtracking with memoization to generate all valid combinations.
 *
 * 3. Q: What if coins have limited quantities?
 *    A: Transform to 0/1 knapsack by treating each coin quantity as separate items.
 *
 * 4. Q: How would you handle very large amounts efficiently?
 *    A: Use space-optimized 1D DP array and consider mathematical approaches for specific coin systems.
 */
public class CoinChange2 {

  public static void main(String[] args) {
    int[] coinDenominations = new int[]{1, 2, 5, 10, 20, 50, 100};
    int targetAmount = 6;
    System.out.printf("Number of ways to make amount %d is %d%n", targetAmount,
        countCombinationsOptimized(coinDenominations, targetAmount));
    System.out.printf("Using recursive approach: %d%n", countCombinationsRecursive(coinDenominations, targetAmount));
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
   * Dynamic Programming solution to count coin combinations
   *
   * Steps:
   * 1. Create 2D DP table where dp[i][j] represents ways to make amount j using first i coins
   * 2. Initialize base case: dp[i][0] = 1 (one way to make amount 0 - use no coins)
   * 3. For each coin and each amount, decide whether to include the coin
   * 4. If coin value <= current amount: dp[i][j] = dp[i-1][j] + dp[i][j-coin_value]
   * 5. Otherwise: dp[i][j] = dp[i-1][j]
   *
   * Algorithm: Dynamic Programming (Bottom-up)
   * Time Complexity: O(coins.length * targetAmount)
   * Space Complexity: O(coins.length * targetAmount)
   *
   * @param coinDenominations array of available coin denominations
   * @param targetAmount the target sum to achieve
   * @return number of ways to make the target amount
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