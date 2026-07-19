package dynamicprogramming.stockbuysell;

import java.util.Arrays;

/**
 * Problem: Best Time to Buy and Sell Stock IV
 *
 * Given prices and k, return the maximum profit from at most k complete transactions. You may hold at most one share at a time.
 *
 * Leetcode: https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iv/ (Hard)
 * Rating:   not available (not a contest problem)
 * Pattern:  Dynamic programming | Stock state machine | Limited transactions
 *
 * Example:
 *   Input:  k = 2, prices = [3, 2, 6, 5, 0, 3]
 *   Output: 7
 *   Why:    buy at 2 and sell at 6, then buy at 0 and sell at 3.
 *
 * Follow-ups:
 *   1. How would you return an actual solution, not only the value?
 *      Store predecessor or choice information while filling the same states.
 *   2. How can space be reduced?
 *      Keep only the previous row or active states when the recurrence allows it.
 *   3. How would constraints such as fees, limits, or weights change it?
 *      Add the constraint to the state or transition and keep the same invariant.
 *
 * Related: Stock I (121), Stock II (122), Stock III (123), Transaction Fee (714).
 */
public class BestTimeToBuyAndSellStockIV {

    public static void main(String[] args) {
    BestTimeToBuyAndSellStockIV solution = new BestTimeToBuyAndSellStockIV();
    int[][] priceCases = { {2, 4, 1}, {3, 2, 6, 5, 0, 3}, {1} };
    int[] transactionLimits = {2, 2, 2};
    int[] expected = {2, 7, 0};
    for (int i = 0; i < priceCases.length; i++) {
      int got = solution.maxProfit(transactionLimits[i], priceCases[i]);
      System.out.printf("k=%d prices=%s -> %d  expected=%d%n", transactionLimits[i], Arrays.toString(priceCases[i]), got, expected[i]);
    }
  }

  /**
   * Top-down DP approach with memoization
   *
   * Algorithm:
   * - Base cases: no days left or no transactions left = 0 profit
   * - If k >= n/2, use unlimited transactions approach
   * - Initialize memo table with null values
   *    - memo table dimensions: days x (k+1) x 2 (holding or not)
   *    - memo[day][transactionsLeft][holding] = profit
   * - Recursive DFS function:
   *   - If holding stock: can sell or rest
   *   - If not holding stock: can buy or rest
   * - Return maximum profit from all options
   *
   * Time Complexity: O(N * K) with memoization.
   * Space Complexity: O(N * K) for memo table plus O(N) recursion stack.
   *
   * @param maxTransactions maximum number of transactions allowed
   * @param prices array of stock prices per day
   * @return maximum profit achievable
   */
  public int maxProfitMemo(int maxTransactions, int[] prices) {
    int length = prices.length;
    if (length <= 1 || maxTransactions == 0) {
      return 0;
    }

    if (maxTransactions >= length / 2) {
      return maxProfitUnlimited(prices);
    }

    Integer[][][] memo = new Integer[length][maxTransactions + 1][2]; // memo[day][transactionsLeft][isHolding] = profit
    return dfs(prices, 0, maxTransactions, 0, memo);
  }

  // Helper method for recursive DFS with memoization
  // holding: 0 = not holding stock, 1 = holding stock
  /** Solves one memoized stock or sequence state. */
  private int dfs(int[] prices, int day, int transactionsLeft, int holding, Integer[][][] memo) {
    if (day == prices.length || transactionsLeft == 0) {
      return 0;
    }

    if (memo[day][transactionsLeft][holding] != null) {
      return memo[day][transactionsLeft][holding];
    }

    // Option 1: Do nothing (rest)
    int doNothing = dfs(prices, day + 1, transactionsLeft, holding, memo);

    int doSomething = 0;
    if (holding == 1) {
      // Currently holding: can sell
      doSomething = prices[day] + dfs(prices, day + 1, transactionsLeft - 1, 0, memo);
    } else {
      // Not holding: can buy
      doSomething = -prices[day] + dfs(prices, day + 1, transactionsLeft, 1, memo);
    }

    memo[day][transactionsLeft][holding] = Math.max(doNothing, doSomething);
    return memo[day][transactionsLeft][holding];
  }

    /**
   * Intuition: buy[t] is the best profit after the buy step of transaction t, and sell[t] is the best profit after selling transaction t. Each price either preserves a state or performs the next legal buy or sell transition.
   *
   * Algorithm:
   *   1. Return 0 for invalid inputs.
   *   2. Use unlimited-transactions shortcut when maxTransactions >= length / 2.
   *   3. Initialize buy and sell arrays.
   *   4. For each day, update transaction states from 1..maxTransactions.
   *   5. Return sell[maxTransactions].
   *
   * Time:  O(n * maxTransactions) - each day updates each transaction state.
   * Space: O(maxTransactions) - stores buy and sell arrays.
   *
   * @param maxTransactions maximum complete transactions
   * @param prices daily prices
   * @return maximum profit
   */
public int maxProfit(int maxTransactions, int[] prices) {
      if (prices == null || prices.length < 2 || maxTransactions == 0) {
          return 0;
      }

      int length = prices.length;
      
      // Optimization: if k >= length/2, becomes unlimited transactions
      if (maxTransactions >= length / 2) {
          return maxProfitUnlimited(prices);
      }

      // State arrays
      int[] buy = new int[maxTransactions + 1];   // buy[i] = max profit after buying for ith transaction
      int[] sell = new int[maxTransactions + 1];  // sell[i] = max profit after selling for ith transaction
      
      // Initialize: buying requires spending money
      for (int i = 1; i <= maxTransactions; i++) {
          buy[i] = -prices[0];  // Cost of buying on day 0
          sell[i] = 0;          // Can't sell without buying
      }

      // Process each price (day)
      for (int day = 1; day < length; day++) {          
          // Update states for each transaction (process in order)
          for (int transactionCount = 1; transactionCount <= maxTransactions; transactionCount++) {
              // Buy state: either keep previous buy or buy today using previous sell profit
              buy[transactionCount] = Math.max(
                buy[transactionCount], 
                sell[transactionCount - 1] - prices[day]
              );
              
              // Sell state: either keep previous sell or sell today using current buy
              sell[transactionCount] = Math.max(
                sell[transactionCount], 
                buy[transactionCount] + prices[day]
              );
          }
      }

      return sell[maxTransactions];
  }

    /** Computes unlimited-transactions stock profit. */
private int maxProfitUnlimited(int[] stockPrices) {
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
