package dynamicprogramming.stockbuysell;

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
 * LeetCode Contest Rating: Not available (not a contest problem)
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
   * State Machine approach: Track buy and sell states for each transaction.
   * Step-by-step:
   *  1. Create arrays to track states:
   *     - buy[i] = max profit after buying for ith transaction
   *     - sell[i] = max profit after selling for ith transaction
   *  2. Initialize buy states to -prices[0] (cost of first buy)
   *  3. For each price:
   *     - Update buy[i]: either keep previous state or buy using sell[i-1] profit
   *     - Update sell[i]: either keep previous state or sell using buy[i]
   *  4. Process transactions in order (1 to k) for each price
   *  5. Return sell[k] (profit after k complete transactions)
   *
   * State Transitions:
   *   Initial → buy[1] → sell[1] → buy[2] → sell[2] → ... → buy[k] → sell[k]
   *
   * Key Insight:
   * Each transaction has two states: bought and sold. We track maximum profit
   * at each state. buy[i] uses profit from sell[i-1], ensuring transactions
   * are sequential and non-overlapping.
   *
   * Time Complexity: O(n*k), iterate through n prices with k transactions.
   * Space Complexity: O(k), two arrays of size k.
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
