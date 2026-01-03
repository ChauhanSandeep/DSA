package dynamicprogramming.StockBuySell;

/**
 * Best Time to Buy and Sell Stock with Transaction Fee
 *
 * Problem Statement:
 * You are given an array prices where prices[i] is the price of a given stock on the ith day,
 * and an integer fee representing a transaction fee. Find the maximum profit you can achieve.
 * You may complete as many transactions as you like, but you need to pay the transaction fee
 * for each transaction. You may not engage in multiple transactions simultaneously
 * (i.e., you must sell the stock before you buy again).
 *
 * Example:
 * Input: prices = [1,3,2,8,4,9], fee = 2
 * Output: 8
 * Explanation: The maximum profit can be achieved by:
 * - Buying at prices[0] = 1
 * - Selling at prices[3] = 8
 * - Buying at prices[4] = 4
 * - Selling at prices[5] = 9
 * The total profit is ((8 - 1) - 2) + ((9 - 4) - 2) = 8.
 *
 * LeetCode: https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/
 *
 * Follow-up Questions for FAANG Interviews:
 * 1. What if fee is paid on both buy and sell? - Modify state transitions to include fee on buy
 * 2. What if different fees for different transaction amounts? - Use fee calculation function
 * 3. How to minimize number of transactions while maximizing profit? - Track transaction count in DP state
 * 4. What if there's a daily holding fee? - Add daily fee to holding state calculation
 * 5. How to handle percentage-based fees? - Calculate fee as percentage of transaction amount
 * 6. What if we want actual transaction details? - Maintain transaction log during DP
 */
public class BestTimeToBuyAndSellStockWithTransactionFee {

  public static void main(String[] args) {
    BestTimeToBuyAndSellStockWithTransactionFee solution = new BestTimeToBuyAndSellStockWithTransactionFee();

    int[] stockPrices = {1, 3, 2, 8, 4, 9};
    int transactionFee = 2;

    int maxProfitOptimized = solution.maxProfitOptimized(stockPrices, transactionFee);
    System.out.println("Optimized maximum profit: " + maxProfitOptimized); // Output: 8
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
   * Steps:
   * 1. Maintain two states: holding stock and not holding stock
   * 2. For each day, calculate maximum profit for both states
   * 3. Holding state: max of (keep holding, buy today)
   * 4. Not holding state: max of (keep not holding, sell today minus fee)
   * 5. Final answer is profit when not holding any stock
   *
   * Time Complexity: O(n)
   * Space Complexity: O(1)
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