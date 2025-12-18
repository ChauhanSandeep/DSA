package dynamicprogramming.StockBuySell;

/**
 * Best Time to Buy and Sell Stock III
 *
 * Problem Statement:
 * You are given an array prices where prices[i] is the price of a given stock on the ith day.
 * Find the maximum profit you can achieve. You may complete at most two transactions.
 * Note: You may not engage in multiple transactions simultaneously (i.e., you must sell the
 * stock before you buy again).
 *
 * Example:
 * Input: prices = [3,3,5,0,0,3,1,4]
 * Output: 6
 * Explanation: Buy on day 4 (price = 0) and sell on day 6 (price = 3), profit = 3-0 = 3.
 * Then buy on day 7 (price = 1) and sell on day 8 (price = 4), profit = 4-1 = 3.
 * Total profit = 3 + 3 = 6.
 *
 * LeetCode: https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iii/
 *
 * Follow-up Questions for FAANG Interviews:
 * 1. What if we can make at most k transactions?
 *  - LeetCode 188: https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iv/
 * 2. How to optimize space complexity?
 *  - Use state machine DP with O(1) space
 * 3. What if transactions have different fees?
 *  - Extend DP states to include variable fees
 * 4. How to find actual transaction days?
 *  - Track buy/sell decisions during DP computation
 * 5. What if we want to minimize risk while maximizing profit?
 *  - Add risk factor to profit calculation
 * 6. Handle very large price arrays efficiently?
 *  - Use divide and conquer or sliding window optimizations
 */
public class BuySellStock3 {

  public static void main(String[] args) {
    BuySellStock3 solution = new BuySellStock3();
    int[] stockPrices = {3, 3, 5, 0, 0, 3, 1, 4};

    System.out.println("Two-pass approach result: " + solution.maxProfitWithDays(stockPrices)); // Output: 6
  }

  /**
   * Main method: Left-Right DP approach (Most intuitive for interviews).
   * Step-by-step:
   *  1. Split problem: max profit = best of (first transaction in [0,i] + second in [i+1,n-1])
   *  2. First pass (left to right):
   *     - leftProfit[i] = max profit achievable using prices[0..i] with one transaction
   *     - Track minimum price seen so far for optimal buying point
   *  3. Second pass (right to left):
   *     - rightProfit[i] = max profit achievable using prices[i..n-1] with one transaction
   *     - Track maximum price seen so far for optimal selling point
   *  4. Third pass: combine results
   *     - For each split point i: profit = leftProfit[i] + rightProfit[i+1]
   *     - Return maximum across all split points
   *
   * Key Insight:
   * Two transactions don't overlap, so we can split the timeline and compute
   * best single transaction for left and right parts independently. The split
   * point represents the gap between two transactions.
   *
   * Algorithm: Two-pass Dynamic Programming with arrays.
   * Time Complexity: O(n), three linear passes through array.
   * Space Complexity: O(n) for leftProfit and rightProfit arrays.
   */
  public int maxProfit(int[] prices) {
      if (prices == null || prices.length < 2) {
          return 0;
      }

      int length = prices.length;
      int[] leftProfit = new int[length];
      int[] rightProfit = new int[length];
      
      // Left pass: compute max profit for first transaction ending at or before i
      int minPrice = prices[0];
      leftProfit[0] = 0;
      
      for (int i = 1; i < length; i++) {
          leftProfit[i] = Math.max(leftProfit[i - 1], prices[i] - minPrice);
          minPrice = Math.min(minPrice, prices[i]);
      }

      // Right pass: compute max profit for second transaction starting at or after i
      int maxPrice = prices[length - 1];
      rightProfit[length - 1] = 0;
      
      for (int i = length - 2; i >= 0; i--) {
          rightProfit[i] = Math.max(rightProfit[i + 1], maxPrice - prices[i]);
          maxPrice = Math.max(maxPrice, prices[i]);
      }

      // Combine: find best split point between two transactions
      int maxProfit = 0;
      for (int i = 0; i < length; i++) {
          int right = (i + 1 < length) ? rightProfit[i + 1] : 0;
          maxProfit = Math.max(maxProfit, leftProfit[i] + right);
      }
      
      return maxProfit;
  }

  /**
   * Finds maximum profit using state machine dynamic programming approach
   *
   * Key Insight:
   * Think of it as a state machine with 4 states. Each state transition represents
   * a trading action. We track maximum profit achievable at each state as we
   * process prices. The transitions naturally handle "at most 2" constraint.
   *
   * 
   * Algorithm: State Machine Dynamic Programming
   * Steps:
   * *  1. Track 4 states representing profit at each stage:
   *     - profitAfterFirstBuy: max profit after first buy (negative, spent money)
   *     - profitAfterFirstSell: max profit after first sell
   *     - profitAfterSecondBuy: max profit after second buy
   *     - profitAfterSecondSell: max profit after second sell (final answer)
   *  2. For each price, update states in order:
   *     a. profitAfterFirstBuy =  either keep previous profitAfterFirstBuy or buy today
   *     b. profitAfterFirstSell = either keep previous profitAfterFirstSell or sell today
   *     c. profitAfterSecondBuy = either keep previous profitAfterSecondBuy or buy again
   *     d. profitAfterSecondSell = either keep previous profitAfterSecondSell or sell again
   *  3. Return profitAfterSecondSell (maximum profit after at most 2 transactions)
   *
   *
   * Time Complexity: O(n)
   * Space Complexity: O(1) - only using constant extra space
   */
  public int[] maxProfitWithDays(int[] prices) {
    if (prices == null || prices.length < 2) {
      return new int[]{0, -1, -1, -1, -1}; // No profit, invalid days
    }

    int length = prices.length;
    int profitAfterFirstBuy = -prices[0];
    int profitAfterFirstSell = 0;
    int profitAfterSecondBuy = -prices[0];
    int profitAfterSecondSell = 0;

    int firstBuyDay = 0;
    int firstSellDay = -1;
    int secondBuyDay = 0;
    int secondSellDay = -1;

    for (int day = 1; day < length; day++) {

      if (-prices[day] > profitAfterFirstBuy) {
        // if price today is lower, then buy today
        profitAfterFirstBuy = -prices[day];
        firstBuyDay = day;
      }
      if (profitAfterFirstBuy + prices[day] > profitAfterFirstSell) {
        // if selling today gives better profit, then sell today
        profitAfterFirstSell = profitAfterFirstBuy + prices[day];
        firstSellDay = day;
      }
      if (profitAfterFirstSell - prices[day] > profitAfterSecondBuy) {
        // If buying today gives better profit after first sell, then buy today
        profitAfterSecondBuy = profitAfterFirstSell - prices[day];
        secondBuyDay = day;
      }
      if (profitAfterSecondBuy + prices[day] > profitAfterSecondSell) {
        // if selling today gives better profit after second buy, then sell today
        profitAfterSecondSell = profitAfterSecondBuy + prices[day];
        secondSellDay = day;
      }
    }

    // Returns: [maxProfit, firstBuyDay, firstSellDay, secondBuyDay, secondSellDay]
    return new int[]{profitAfterSecondSell, firstBuyDay, firstSellDay, secondBuyDay, secondSellDay};
  }
}