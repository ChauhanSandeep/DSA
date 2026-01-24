package dynamicprogramming.stockbuysell;

/**
 * Best Time to Buy and Sell Stock I
 *
 * Problem Statement:
 * You are given an array prices where prices[i] is the price of a given stock on the ith day.
 * You want to maximize your profit by choosing a single day to buy one stock and choosing
 * a different day in the future to sell that stock. Return the maximum profit you can achieve
 * from this transaction. If you cannot achieve any profit, return 0.
 *
 * Example:
 * Input: prices = [7,1,5,3,6,4]
 * Output: 5
 * Explanation: Buy on day 2 (price = 1) and sell on day 5 (price = 6), profit = 6-1 = 5.
 * Note that buying on day 2 and selling on day 1 is not allowed because you must buy before you sell.
 *
 * LeetCode: https://leetcode.com/problems/best-time-to-buy-and-sell-stock/
 *
 * Follow-up Questions for FAANG Interviews:
 * 1. What if you can make multiple transactions?
 *  - LeetCode 122: https://leetcode.com/problems/best-time-to-buy-and-sell-stock-ii/
 * 2. What if you can make at most 2 transactions?
 *  - LeetCode 123: https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iii/
 * 3. What if you can make at most k transactions?
 *  - LeetCode 188: https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iv/
 * 4. What if there's a cooldown period after selling?
 *  - LeetCode 309: https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-cooldown/
 * 5. What if there's a transaction fee?
 *  - LeetCode 714: https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/
 * 6. How to find the actual buy and sell days?
 *  - Track indices during computation
 */
public class BuySellStock0 {

  public static void main(String[] args) {
    BuySellStock0 solution = new BuySellStock0();
    int[] stockPrices = {10, 22, 5, 75, 65, 80};

    int maxProfit = solution.maxProfit(stockPrices);
    System.out.println("Maximum Profit: " + maxProfit); // Output: 75

    int maxProfitOptimized = solution.maxProfit(stockPrices);
    System.out.println("Optimized Maximum Profit: " + maxProfitOptimized); // Output: 75
  }

  /**
   * Finds maximum profit from single buy-sell transaction using greedy approach
   *
   * Algorithm: Single Pass Greedy Algorithm
   * Steps:
   * 1. Track minimum price encountered so far as potential buy point
   * 2. For each price, calculate profit if we sell at current price
   * 3. Update maximum profit if current profit is better
   * 4. Continue until all prices are processed
   *
   * Time Complexity: O(n) where n is number of days
   * Space Complexity: O(1) - only using constant extra space
   *
   * @param stockPrices array of stock prices for each day
   * @return maximum profit achievable from single transaction, 0 if no profit possible
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