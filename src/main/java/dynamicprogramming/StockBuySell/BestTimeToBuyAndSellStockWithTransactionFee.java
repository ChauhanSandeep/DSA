package dynamicprogramming.stockbuysell;

import java.util.Arrays;

/**
 * Problem: Best Time to Buy and Sell Stock with Transaction Fee
 *
 * Return the maximum profit from unlimited transactions when each sale pays a fixed fee. You cannot hold more than one share at once.
 *
 * Leetcode: https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/ (Medium)
 * Rating:   not available (not a contest problem)
 * Pattern:  Dynamic programming | Stock state machine | Transaction fee
 *
 * Example:
 *   Input:  prices = [1, 3, 2, 8, 4, 9], fee = 2
 *   Output: 8
 *   Why:    transactions 1->8 and 4->9 earn (7 - 2) + (5 - 2).
 *
 * Follow-ups:
 *   1. How would you return an actual solution, not only the value?
 *      Store predecessor or choice information while filling the same states.
 *   2. How can space be reduced?
 *      Keep only the previous row or active states when the recurrence allows it.
 *   3. How would constraints such as fees, limits, or weights change it?
 *      Add the constraint to the state or transition and keep the same invariant.
 *
 * Related: Stock II (122), Stock with Cooldown (309), Stock IV (188).
 */
public class BestTimeToBuyAndSellStockWithTransactionFee {

    public static void main(String[] args) {
    BestTimeToBuyAndSellStockWithTransactionFee solution = new BestTimeToBuyAndSellStockWithTransactionFee();
    int[][] priceCases = { {1, 3, 2, 8, 4, 9}, {1, 3, 7, 5, 10, 3}, {1} };
    int[] fees = {2, 3, 2};
    int[] expected = {8, 6, 0};
    for (int i = 0; i < priceCases.length; i++) {
      int got = solution.maxProfitOptimized(priceCases[i], fees[i]);
      System.out.printf("prices=%s fee=%d -> %d  expected=%d%n", Arrays.toString(priceCases[i]), fees[i], got, expected[i]);
    }
  }

  /**
   * Greedy with transaction rollback.
   * Step-by-step:
   *  1. Track minimum price seen so far (best buying point)
   *  2. For each price:
   *     a. If current price < minPrice: update minPrice (found better buy point)
   *     b. If (current price - minPrice) > fee: profitable to sell
   *        - Add profit: (price - minPrice - fee)
   *        - KEY: Set minPrice = price - fee (allows transaction extension)
   *  3. The minPrice adjustment enables "rolling back" a sale if price keeps rising
   *
   * Key Insight:
   * Greedy approach works because of transaction rollback mechanism. When we sell
   * at price P, setting minPrice = P - fee allows us to "extend" the transaction
   * if next price is higher. Example: [1,3,5] with fee=2:
   * - Sell at 3: profit=0, minPrice=1
   * - Sell at 5: profit=2 (equivalent to single transaction 1→5)
   * This mimics holding through intermediate prices without explicitly tracking state.
   *
   * Algorithm: Greedy with transaction extension.
   * Time Complexity: O(n), single pass through prices.
   * Space Complexity: O(1), only tracking minPrice and profit.
   */
  public int maxProfitGreedy(int[] prices, int fee) {
      if (prices == null || prices.length < 2) {
          return 0;
      }

      int minPrice = prices[0];
      int totalProfit = 0;
      
      for (int i = 1; i < prices.length; i++) {
          if (prices[i] < minPrice) {
              // Found cheaper buying point
              minPrice = prices[i];
          } else if (prices[i] - minPrice > fee) {
              // Profitable to sell now
              totalProfit += prices[i] - minPrice - fee;
              
              // Allow transaction extension: if price keeps rising,
              // we can "rollback" this sale and extend to next peak
              minPrice = prices[i] - fee;
          }
      }
      
      return totalProfit;
  }

    /**
   * Intuition: maxProfitAfterBuy is the best profit while holding stock, and maxProfitAfterSell is the best profit while not holding. Selling realizes held profit minus fee; buying spends today's price from the previous sell state.
   *
   * Algorithm:
   *   1. Return 0 for fewer than two prices.
   *   2. Initialize buy and sell states.
   *   3. For each day, compute the new sell state.
   *   4. Compute the new buy state.
   *   5. Return the final sell state.
   *
   * Time:  O(n) - each price is processed once.
   * Space: O(1) - stores two states.
   *
   * @param stockPrices daily prices
   * @param transactionFee fee per sale
   * @return maximum profit
   */
public int maxProfitOptimized(int[] stockPrices, int transactionFee) {
    if (stockPrices == null || stockPrices.length < 2) {
      return 0;
    }

    // maxProfitAfterBuy: maximum profit when we have stock in hand
    // maxProfitAfterSell: maximum profit when we don't have stock
    int maxProfitAfterBuy = -stockPrices[0];
    int maxProfitAfterSell = 0;

    for (int day = 1; day < stockPrices.length; day++) {
      int newMaxProfitAfterSell = Math.max(
        maxProfitAfterSell,                                     // Keep previous sell state 
        maxProfitAfterBuy + stockPrices[day] - transactionFee  // Sell today and pay fee
      );

      int newMaxProfitAfterBuy = Math.max(
        maxProfitAfterBuy,                        // Keep previous buy state
        maxProfitAfterSell - stockPrices[day]    // Buy today
      );

      maxProfitAfterSell = newMaxProfitAfterSell;
      maxProfitAfterBuy = newMaxProfitAfterBuy;
    }

    return maxProfitAfterSell;
  }

  /**
   * Method that also returns the number of transactions made
   * Useful for interview follow-up questions
   *
   * Time Complexity: O(n)
   * Space Complexity: O(1)
   *
   * @param stockPrices array of daily stock prices
   * @param transactionFee fee per transaction
   * @return array containing [maxProfit, numberOfTransactions]
   */
  public int[] maxProfitWithTransactionCount(int[] stockPrices, int transactionFee) {
    if (stockPrices == null || stockPrices.length < 2) {
      return new int[]{0, 0};
    }

    int maxProfitAfterBuy = -stockPrices[0];
    int maxProfitAfterSell = 0;
    int transactionCount = 0;
    boolean previouslyHadStock = true; // Started by buying stock

    for (int day = 1; day < stockPrices.length; day++) {
      int newMaxProfitAfterSell = Math.max(
          maxProfitAfterSell,                               // Keep previous sell state
          maxProfitAfterBuy + stockPrices[day] - transactionFee   // Sell today and pay fee
      );
      int newMaxProfitAfterBuy = Math.max(
          maxProfitAfterBuy,                        // Keep previous buy state
          maxProfitAfterSell - stockPrices[day]    // Buy today
      );

      // Count transactions (selling completes a transaction)
      if (newMaxProfitAfterSell > maxProfitAfterSell && previouslyHadStock) {
        transactionCount++;
        previouslyHadStock = false;
      } else if (newMaxProfitAfterBuy > maxProfitAfterBuy && !previouslyHadStock) {
        previouslyHadStock = true;
      }

      maxProfitAfterSell = newMaxProfitAfterSell;
      maxProfitAfterBuy = newMaxProfitAfterBuy;
    }

    return new int[]{maxProfitAfterSell, transactionCount};
  }
}