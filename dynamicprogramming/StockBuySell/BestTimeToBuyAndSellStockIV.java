package dynamicprogramming.StockBuySell;

/**
 * Problem: Best Time to Buy and Sell Stock IV
 *
 * You are given an integer array prices where prices[i] is the price of a given stock on the ith day,
 * and an integer k. Find the maximum profit you can achieve. You may complete at most k transactions.
 * Note: You may not engage in multiple transactions simultaneously (i.e., you must sell the stock
 * before you buy again).
 *
 * Example 1:
 * Input: k = 2, prices = [2,4,1]
 * Output: 2
 * Explanation: Buy on day 1 (price = 2) and sell on day 2 (price = 4), profit = 4-2 = 2.
 *
 * Example 2:
 * Input: k = 2, prices = [3,2,6,5,0,3]
 * Output: 7
 * Explanation: Buy on day 2 (price = 2) and sell on day 3 (price = 6), profit = 6-2 = 4.
 * Then buy on day 5 (price = 0) and sell on day 6 (price = 3), profit = 3-0 = 3.
 *
 * LeetCode Link: https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iv/
 *
 * Follow-up Questions for FAANG Interviews:
 * 1. Q: What if k is very large compared to array size?
 *    A: Use greedy approach when k >= n/2, since max possible transactions is n/2
 * 2. Q: How to optimize space complexity?
 *    A: Use 1D arrays or state variables instead of 2D DP table
 * 3. Q: What if we want actual transaction details?
 *    A: Track buy/sell decisions during DP and reconstruct path
 * 4. Q: Handle transaction fees in this scenario?
 *    A: Extend DP states to subtract fees on each transaction
 * 5. Q: Related problems? A: Stock I/II/III problems, Best Time to Buy and Sell Stock with Transaction Fee
 */
public class BestTimeToBuyAndSellStockIV {

  public static void main(String[] args) {
    BestTimeToBuyAndSellStockIV solution = new BestTimeToBuyAndSellStockIV();

    int[] prices1 = {2, 4, 1};
    int k1 = 2;
    System.out.println("Result: " + solution.maxProfit(k1, prices1)); // 2

    int[] prices2 = {3, 2, 6, 5, 0, 3};
    int k2 = 2;
    System.out.println("Result: " + solution.maxProfit(k2, prices2)); // 7

    int[] prices3 = {1, 2, 3, 4, 5};
    int k3 = 2;
    System.out.println("Result: " + solution.maxProfit(k3, prices3)); // 4
  }

  /**
   * Finds maximum profit with at most k transactions using optimized dynamic programming.
   *
   * Algorithm Steps:
   * 1. Handle edge cases and check if k >= n/2 for unlimited transactions optimization
   * 2. Use 2D DP where dp[i][j] = max profit with at most i transactions by day j
   * 3. For each transaction limit and day, consider:
   *    - Don't trade today (carry forward previous profit)
   *    - Sell today (use best previous buying opportunity)
   * 4. Track maxProfitAfterBuying to optimize inner loop calculations
   *
   * Time Complexity: O(k * n) where k = max transactions, n = number of days
   * Space Complexity: O(k * n) for the DP table
   *
   * @param maxTransactions maximum number of transactions allowed
   * @param stockPrices array of daily stock prices
   * @return maximum profit achievable with at most k transactions
   */
  public int maxProfit(int maxTransactions, int[] stockPrices) {
    int numberOfDays = stockPrices.length;

    if (numberOfDays <= 1 || maxTransactions <= 0) {
      return 0;
    }

    // Optimization: if k >= n/2, we can make unlimited transactions
    if (maxTransactions >= numberOfDays / 2) {
      return maxProfitUnlimitedTransactions(stockPrices);
    }

    // dp[i][j] = max profit with at most i transactions by day j
    int[][] dp = new int[maxTransactions + 1][numberOfDays];

    for (int transactionLimit = 1; transactionLimit <= maxTransactions; transactionLimit++) {
      // Track maximum profit after buying (profit from previous transactions minus buy price)
      int maxProfitAfterBuying = -stockPrices[0];

      for (int day = 1; day < numberOfDays; day++) {
        int currentPrice = stockPrices[day];

        // Option 1: Don't trade today, keep previous day's profit
        // Option 2: Sell today using best previous buying opportunity
        dp[transactionLimit][day] = Math.max(
            dp[transactionLimit][day - 1],           // Don't sell today
            currentPrice + maxProfitAfterBuying      // Sell today
        );

        // Update best buying opportunity for next iteration
        maxProfitAfterBuying = Math.max(
            maxProfitAfterBuying,                                        // Keep previous buy
            dp[transactionLimit - 1][day - 1] - currentPrice           // Buy today
        );
      }
    }

    return dp[maxTransactions][numberOfDays - 1];
  }

  /**
   * Space-optimized version using only 1D arrays.
   *
   * Algorithm Steps:
   * 1. Use two 1D arrays instead of 2D table: previous and current transaction limits
   * 2. For each transaction limit, update current array based on previous array
   * 3. Swap arrays after processing each transaction limit
   *
   * Time Complexity: O(k * n)
   * Space Complexity: O(n) - only two arrays needed
   *
   * @param maxTransactions maximum number of transactions allowed
   * @param stockPrices array of daily stock prices
   * @return maximum profit achievable
   */
  public int maxProfitSpaceOptimized(int maxTransactions, int[] stockPrices) {
    int numberOfDays = stockPrices.length;

    if (numberOfDays <= 1 || maxTransactions <= 0) {
      return 0;
    }

    if (maxTransactions >= numberOfDays / 2) {
      return maxProfitUnlimitedTransactions(stockPrices);
    }

    int[] previousTransactionProfits = new int[numberOfDays];
    int[] currentTransactionProfits = new int[numberOfDays];

    for (int transactionCount = 1; transactionCount <= maxTransactions; transactionCount++) {
      int maxProfitAfterBuying = -stockPrices[0];

      for (int day = 1; day < numberOfDays; day++) {
        currentTransactionProfits[day] = Math.max(
            currentTransactionProfits[day - 1],           // Don't sell today
            stockPrices[day] + maxProfitAfterBuying      // Sell today
        );

        maxProfitAfterBuying = Math.max(
            maxProfitAfterBuying,                               // Keep previous buy
            previousTransactionProfits[day - 1] - stockPrices[day]  // Buy today
        );
      }

      // Swap arrays for next transaction limit
      int[] temp = previousTransactionProfits;
      previousTransactionProfits = currentTransactionProfits;
      currentTransactionProfits = temp;
    }

    return previousTransactionProfits[numberOfDays - 1];
  }

  /**
   * Greedy approach for unlimited transactions when k >= n/2.
   *
   * Algorithm Steps:
   * 1. Take advantage of every profitable price increase
   * 2. Buy before price goes up, sell before price goes down
   * 3. Sum all positive daily price differences
   *
   * Time Complexity: O(n)
   * Space Complexity: O(1)
   *
   * @param stockPrices array of daily stock prices
   * @return maximum profit with unlimited transactions
   */
  private int maxProfitUnlimitedTransactions(int[] stockPrices) {
    int totalMaxProfit = 0;

    for (int day = 1; day < stockPrices.length; day++) {
      int dailyPriceChange = stockPrices[day] - stockPrices[day - 1];
      if (dailyPriceChange > 0) {
        totalMaxProfit += dailyPriceChange;
      }
    }

    return totalMaxProfit;
  }
}
