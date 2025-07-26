package DynamicProgramming.StockBuySell;

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

    System.out.println("Two-pass approach result: " + solution.maxProfitStateMachine(stockPrices)); // Output: 6
  }

  /**
   * Finds maximum profit using state machine dynamic programming approach
   *
   * Algorithm: State Machine Dynamic Programming
   * Steps:
   * 1. Track 4 states: first buy, first sell, second buy, second sell
   * 2. For each day, update all states based on optimal decisions
   * 3. Each state represents maximum profit achievable in that state
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