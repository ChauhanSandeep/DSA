package DynamicProgramming.StockBuySell;

/**
 * Best Time to Buy and Sell Stock IV
 *
 * Problem Statement:
 * You are given an integer array prices where prices[i] is the price of a given stock on the ith day,
 * and an integer k. Find the maximum profit you can achieve. You may complete at most k transactions.
 * Note: You may not engage in multiple transactions simultaneously (i.e., you must sell the stock
 * before you buy again).
 *
 * Example:
 * Input: k = 2, prices = [2,4,1]
 * Output: 2
 * Explanation: Buy on day 1 (price = 2) and sell on day 2 (price = 4), profit = 4-2 = 2.
 *
 * Input: k = 2, prices = [3,2,6,5,0,3]
 * Output: 7
 * Explanation: Buy on day 2 (price = 2) and sell on day 3 (price = 6), profit = 6-2 = 4.
 * Then buy on day 5 (price = 0) and sell on day 6 (price = 3), profit = 3-0 = 3.
 * Total profit = 4 + 3 = 7.
 *
 * LeetCode: https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iv/
 *
 * Follow-up Questions for FAANG Interviews:
 * 1. What if k is very large compared to array size?
 *  - Use greedy approach when k >= n/2
 * 2. How to optimize space complexity?
 *  - Use 1D arrays or rolling arrays technique
 * 3. What if we want actual transaction details?
 *  - Track buy/sell decisions during DP
 * 4. Handle transaction fees in this scenario?
 *  - Extend DP states to include fees
 * 5. What about different transaction limits per time period?
 *  - Use sliding window with DP
 * 6. Memory optimization for very large k?
 *  - Use coordinate compression or state reduction
 */
public class BuySellStock4 {

  public static void main(String[] args) {
    BuySellStock4 solution = new BuySellStock4();

    int[] stockPrices = {3, 2, 6, 5, 0, 3};
    int maxTransactions = 2;

    System.out.println("Optimized DP result: " + solution.maxProfit(stockPrices, maxTransactions)); // Output: 7
    System.out.println(
        "Space optimized result: " + solution.maxProfitSpaceOptimized(stockPrices, maxTransactions)); // Output: 7
    System.out.println(
        "State machine result: " + solution.maxProfitStateMachine(stockPrices, maxTransactions)); // Output: 7
  }

  /**
   * Finds maximum profit with at most k transactions using optimized dynamic programming
   *
   * Algorithm: Optimized Dynamic Programming with Early Termination
   * Steps:
   * 1. Handle edge cases and check if k is large enough for unlimited transactions
   * 2. Use 2D DP where dp[i][j] = max profit with at most i transactions by day j
   * 3. Optimize inner loop by tracking maximum profit after buying
   * 4. For each transaction, calculate best profit considering buy and sell decisions
   *
   * Time Complexity: O(k * n) where k is max transactions, n is number of days
   * Space Complexity: O(k * n) for the DP table
   *
   * @param stockPrices array of daily stock prices
   * @param maxTransactions maximum number of transactions allowed
   * @return maximum profit achievable with at most k transactions
   */
  public int maxProfit(int[] stockPrices, int maxTransactions) {
    int numberOfDays = stockPrices.length;

    // Handle edge cases
    if (numberOfDays <= 1 || maxTransactions <= 0) {
      return 0;
    }

    // Optimization: if k >= n/2, we can make unlimited transactions
    // This is because maximum possible transactions is n/2 (buy-sell pairs)
    if (maxTransactions >= numberOfDays / 2) {
      return maxProfitUnlimitedTransactions(stockPrices);
    }

    // DP table: maxProfitWithTransactions[i][j] = max profit with at most i transactions by day j
    int[][] maxProfitWithTransactions = new int[maxTransactions + 1][numberOfDays];

    // Process each transaction limit
    for (int currentTransactionLimit = 1; currentTransactionLimit <= maxTransactions; currentTransactionLimit++) {
      // Track maximum profit after buying (profit from previous transactions minus buy price)
      int maxProfitAfterBuying = -stockPrices[0];

      // Process each day for current transaction limit
      for (int day = 1; day < numberOfDays; day++) {
        int currentPrice = stockPrices[day];

        // Option 1: Don't trade today, keep previous profit
        // Option 2: Sell today using best previous buy opportunity
        int maxProfitAfterSellingToday = Math.max(
            maxProfitWithTransactions[currentTransactionLimit][day - 1],   // Keep previous selling profit only
            currentPrice + maxProfitAfterBuying                            // Sell today
            );

        // Update DP table with maximum profit after selling today
        maxProfitWithTransactions[currentTransactionLimit][day] = maxProfitAfterSellingToday;

        // Update best buying opportunity: either keep previous or buy today
        maxProfitAfterBuying = Math.max(
            maxProfitAfterBuying,             // Keep previous buy opportunity
            maxProfitWithTransactions[currentTransactionLimit - 1][day - 1] - currentPrice  // Buy today
        );
      }
    }

    return maxProfitWithTransactions[maxTransactions][numberOfDays - 1];
  }

  /**
   * Space-optimized version using only two 1D arrays
   *
   * Algorithm: Space-Optimized Dynamic Programming
   * Steps:
   * 1. Use only current and previous transaction arrays instead of 2D table
   * 2. Alternate between arrays for each transaction level
   * 3. Same logic as 2D version but with O(n) space
   *
   * Time Complexity: O(k * n)
   * Space Complexity: O(n) - only two arrays needed
   */
  public int maxProfitSpaceOptimized(int[] stockPrices, int maxTransactions) {
    int numberOfDays = stockPrices.length;

    if (numberOfDays <= 1 || maxTransactions <= 0) {
      return 0;
    }

    if (maxTransactions >= numberOfDays / 2) {
      return maxProfitUnlimitedTransactions(stockPrices);
    }

    // Use two arrays to represent current and previous transaction limits
    int[] previousTransactionProfits = new int[numberOfDays];
    int[] currentTransactionProfits = new int[numberOfDays];

    for (int transactionCount = 1; transactionCount <= maxTransactions; transactionCount++) {
      int maxProfitAfterBuying = -stockPrices[0];

      for (int day = 1; day < numberOfDays; day++) {
        currentTransactionProfits[day] = Math.max(currentTransactionProfits[day - 1],           // Don't sell today
            stockPrices[day] + maxProfitAfterBuying      // Sell today
        );

        maxProfitAfterBuying = Math.max(maxProfitAfterBuying,                        // Keep previous buy
            previousTransactionProfits[day - 1] - stockPrices[day]  // Buy today
        );
      }

      // Swap arrays for next iteration
      int[] temp = previousTransactionProfits;
      previousTransactionProfits = currentTransactionProfits;
      currentTransactionProfits = temp;
    }

    return previousTransactionProfits[numberOfDays - 1];
  }

  /**
   * Greedy approach for unlimited transactions (when k >= n/2)
   *
   * Algorithm: Greedy Algorithm
   * Steps:
   * 1. Buy before every price increase
   * 2. Sell before every price decrease
   * 3. Sum all profitable single-day trades
   *
   * Time Complexity: O(n)
   * Space Complexity: O(1)
   *
   * @param stockPrices array of daily stock prices
   * @return maximum profit with unlimited transactions
   */
  private int maxProfitUnlimitedTransactions(int[] stockPrices) {
    int totalMaxProfit = 0;

    // Take advantage of every profitable opportunity
    for (int day = 1; day < stockPrices.length; day++) {
      int dailyPriceChange = stockPrices[day] - stockPrices[day - 1];
      if (dailyPriceChange > 0) {
        totalMaxProfit += dailyPriceChange;
      }
    }

    return totalMaxProfit;
  }
}