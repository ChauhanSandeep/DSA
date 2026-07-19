package dynamicprogramming.stockbuysell;

import java.util.Arrays;

/**
 * Problem: Best Time to Buy and Sell Stock III
 *
 * Return the maximum profit from at most two complete transactions. The two transactions cannot overlap.
 *
 * Leetcode: https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iii/ (Hard)
 * Rating:   not available (not a contest problem)
 * Pattern:  Dynamic programming | Prefix/suffix profit | Two transactions
 *
 * Example:
 *   Input:  prices = [3, 3, 5, 0, 0, 3, 1, 4]
 *   Output: 6
 *   Why:    buy at 0, sell at 3, then buy at 1 and sell at 4.
 *
 * Follow-ups:
 *   1. How would you return an actual solution, not only the value?
 *      Store predecessor or choice information while filling the same states.
 *   2. How can space be reduced?
 *      Keep only the previous row or active states when the recurrence allows it.
 *   3. How would constraints such as fees, limits, or weights change it?
 *      Add the constraint to the state or transition and keep the same invariant.
 *
 * Related: Stock I (121), Stock II (122), Stock IV (188).
 */
public class BuySellStock3 {

    public static void main(String[] args) {
    BuySellStock3 solution = new BuySellStock3();
    int[][] priceCases = { {3, 3, 5, 0, 0, 3, 1, 4}, {1, 2, 3, 4, 5}, {1} };
    int[] expected = {6, 4, 0};
    for (int i = 0; i < priceCases.length; i++) {
      int got = solution.maxProfit(priceCases[i]);
      System.out.printf("prices=%s -> %d  expected=%d%n", Arrays.toString(priceCases[i]), got, expected[i]);
    }
  }

    /**
   * Intuition: every two-transaction answer has a split point. leftProfit[i] is the best first transaction ending by i, and rightProfit[i] is the best second transaction starting at i; combining them checks every split.
   *
   * Algorithm:
   *   1. Return 0 for invalid inputs.
   *   2. Build leftProfit with a left-to-right min price scan.
   *   3. Build rightProfit with a right-to-left max price scan.
   *   4. Combine leftProfit[i] with rightProfit[i + 1].
   *   5. Return the maximum combined profit.
   *
   * Time:  O(n) - three linear passes.
   * Space: O(n) - two profit arrays.
   *
   * @param prices daily prices
   * @return maximum profit from at most two transactions
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