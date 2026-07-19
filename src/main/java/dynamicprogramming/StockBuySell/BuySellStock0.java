package dynamicprogramming.stockbuysell;

import java.util.Arrays;

/**
 * Problem: Best Time to Buy and Sell Stock
 *
 * Choose one buy day and one later sell day to maximize profit. If no profitable transaction exists, return 0.
 *
 * Leetcode: https://leetcode.com/problems/best-time-to-buy-and-sell-stock/ (Easy)
 * Rating:   not available (not a contest problem)
 * Pattern:  Greedy | Prefix minimum | One-pass scan
 *
 * Example:
 *   Input:  prices = [7, 1, 5, 3, 6, 4]
 *   Output: 5
 *   Why:    buying at 1 and selling later at 6 gives the best profit.
 *
 * Follow-ups:
 *   1. How would you return an actual solution, not only the value?
 *      Store predecessor or choice information while filling the same states.
 *   2. How can space be reduced?
 *      Keep only the previous row or active states when the recurrence allows it.
 *   3. How would constraints such as fees, limits, or weights change it?
 *      Add the constraint to the state or transition and keep the same invariant.
 *
 * Related: Stock II (122), Stock III (123), Stock IV (188).
 */
public class BuySellStock0 {

    public static void main(String[] args) {
    BuySellStock0 solution = new BuySellStock0();
    int[][] priceCases = { {7, 1, 5, 3, 6, 4}, {7, 6, 4, 3, 1}, {5} };
    int[] expected = {5, 0, 0};
    for (int i = 0; i < priceCases.length; i++) {
      int got = solution.maxProfit(priceCases[i]);
      System.out.printf("prices=%s -> %d  expected=%d%n", Arrays.toString(priceCases[i]), got, expected[i]);
    }
  }

    /**
   * Intuition: if today is the sell day, the best buy is the minimum price seen so far. Maintaining that minimum lets each price test its best sale profit in one pass.
   *
   * Algorithm:
   *   1. Return 0 when no transaction is possible.
   *   2. Track minimumPriceSeenSoFar.
   *   3. For each price, update the minimum.
   *   4. Compute selling-today profit and update the best.
   *   5. Return maximumProfitAchievable.
   *
   * Time:  O(n) - one scan.
   * Space: O(1) - stores two integers.
   *
   * @param stockPrices daily prices
   * @return best single-transaction profit
   */
public int maxProfit(int[] stockPrices) {
    // Handle edge cases: null array or insufficient days for transaction
    if (stockPrices == null || stockPrices.length < 2) {
      return 0;
    }

    int minimumPriceSeenSoFar = Integer.MAX_VALUE;
    int maximumProfitAchievable = 0;

    // Process each day's price to find optimal buy-sell combination
    for (int currentDayPrice : stockPrices) {
      // Update minimum price if current day offers better buying opportunity
      if (currentDayPrice < minimumPriceSeenSoFar) {
        minimumPriceSeenSoFar = currentDayPrice;
      }

      // Calculate profit if we sell at current day's price
      int profitFromSellingToday = currentDayPrice - minimumPriceSeenSoFar;

      // Update maximum profit if selling today gives better result
      if (profitFromSellingToday > maximumProfitAchievable) {
        maximumProfitAchievable = profitFromSellingToday;
      }
    }

    return maximumProfitAchievable;
  }

  /**
   * Additional method that returns both maximum profit and the buy/sell days
   * Useful for interview follow-up questions
   *
   * Time Complexity: O(n)
   * Space Complexity: O(1)
   *
   * @param stockPrices array of stock prices
   * @return array containing [maxProfit, buyDay, sellDay] or [0, -1, -1] if no profit
   */
  public int[] maxProfitWithDays(int[] stockPrices) {
    if (stockPrices == null || stockPrices.length < 2) {
      return new int[]{0, -1, -1};
    }

    int minimumPrice = Integer.MAX_VALUE;
    int maximumProfit = 0;
    int bestBuyDay = 0;
    int bestSellDay = 0;
    int currentBuyDay = 0;

    for (int day = 0; day < stockPrices.length; day++) {
      int currentPrice = stockPrices[day];

      if (currentPrice < minimumPrice) {
        minimumPrice = currentPrice;
        currentBuyDay = day;
      }

      int currentProfit = currentPrice - minimumPrice;
      if (currentProfit > maximumProfit) {
        maximumProfit = currentProfit;
        bestBuyDay = currentBuyDay;
        bestSellDay = day;
      }
    }

    return new int[]{maximumProfit, bestBuyDay, bestSellDay};
  }
}