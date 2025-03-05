package DynamicProgramming.StockBuySell;

/**
 * LeetCode Problem: https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iii/
 *
 * This program finds the maximum profit that can be made with at most two transactions.
 * - You may buy and sell a stock at most twice.
 * - You must sell the first stock before buying the second one.
 *
 * Approach:
 * - Use dynamic programming with two forward and backward passes:
 *   1. Left to right pass: Compute max profit if sold up to today.
 *   2. Right to left pass: Compute max profit if bought from today onwards.
 * - The final result is the maximum sum of both profits at each day.
 *
 * Time Complexity: O(N) - Two passes through the array.
 * Space Complexity: O(N) - Two extra arrays used.
 */
public class BuySellStock3 {

    public static void main(String[] args) {
        int[] prices = {10, 22, 5, 75, 65, 80};
        BuySellStock3 solution = new BuySellStock3();
        System.out.println("Max profit with at most 2 transactions: " + solution.getMaxProfit(prices));
    }

    public int getMaxProfit(int[] prices) {
        if (prices == null || prices.length < 2) return 0;

        int n = prices.length;

        // Left to Right: Max profit if sold up to today
        int minPriceSoFar = prices[0];
        int[] maxProfitIfSoldUntilToday = new int[n];
        
        for (int i = 1; i < n; i++) {
            minPriceSoFar = Math.min(minPriceSoFar, prices[i]);
            maxProfitIfSoldUntilToday[i] = Math.max(maxProfitIfSoldUntilToday[i - 1], prices[i] - minPriceSoFar);
        }

        // Right to Left: Max profit if bought from today onwards
        int maxPriceAfter = prices[n - 1];
        int[] maxProfitIfBoughtFromToday = new int[n];

        for (int i = n - 2; i >= 0; i--) {
            maxPriceAfter = Math.max(maxPriceAfter, prices[i]);
            maxProfitIfBoughtFromToday[i] = Math.max(maxProfitIfBoughtFromToday[i + 1], maxPriceAfter - prices[i]);
        }

        // Compute max profit with at most 2 transactions
        int maxProfit = 0;
        for (int i = 0; i < n; i++) {
            maxProfit = Math.max(maxProfit, maxProfitIfSoldUntilToday[i] + maxProfitIfBoughtFromToday[i]);
        }

        return maxProfit;
    }
}
