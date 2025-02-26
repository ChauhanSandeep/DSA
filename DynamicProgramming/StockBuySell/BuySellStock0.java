package DynamicProgramming.StockBuySell;

/**
 * Problem: Best Time to Buy and Sell Stock
 * Given an array `prices[]` where `prices[i]` is the stock price on day `i`,
 * find the maximum profit possible with **one** transaction (buy one, sell one).
 *
 * Approach:
 * - Use a **single pass** to track the **minimum price so far** and compute the potential profit.
 * - Keep updating the **maximum profit** as we iterate through the array.
 *
 * Time Complexity: O(N) → Single pass through the array.
 * Space Complexity: O(1) → Only a few extra variables used.
 *
 * LeetCode Link: https://leetcode.com/problems/best-time-to-buy-and-sell-stock/
 */
public class BuySellStock0 {

    public static void main(String[] args) {
        int[] prices = {10, 22, 5, 75, 65, 80};
        int maxProfit = getMaxProfit(prices);
        System.out.println("Maximum Profit: " + maxProfit);
    }

    /**
     * Calculates the maximum profit possible with a single buy-sell transaction.
     *
     * @param prices Array of stock prices.
     * @return Maximum profit possible, or 0 if no profitable transaction exists.
     */
    public static int getMaxProfit(int[] prices) {
        if (prices == null || prices.length < 2) {
            return 0; // No transaction possible
        }

        int minPriceSoFar = Integer.MAX_VALUE;
        int maxProfit = 0;

        // Iterate through price array
        for (int price : prices) {
            // Track the minimum price encountered so far
            minPriceSoFar = Math.min(minPriceSoFar, price);
            // Compute the profit if we sell at this price
            maxProfit = Math.max(maxProfit, price - minPriceSoFar);
        }

        return maxProfit;
    }
}
