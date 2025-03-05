package DynamicProgramming.StockBuySell;

/**
 * LeetCode Problem: https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-cooldown/
 *
 * This program calculates the maximum profit from stock trading where:
 * - You can buy and sell multiple times.
 * - There is a mandatory cooldown period of one day after selling a stock.
 *
 * Approach:
 * - We use dynamic programming with three states:
 *   1. `holdProfit` (profit while holding a stock, meaning we have bought it and not sold yet)
 *   2. `sellProfit` (profit after selling a stock on that day)
 *   3. `cooldownProfit` (profit when we are in the cooldown period, meaning we cannot buy on that day)
 *
 * Transition Logic:
 * - `newHoldProfit`: Either keep holding from the previous day or buy today (only allowed if in cooldown state yesterday).
 * - `newSellProfit`: Sell the stock today, meaning we must have held it before.
 * - `newCooldownProfit`: Either stay in cooldown or enter cooldown after selling the stock yesterday.
 *
 * Final Result:
 * - The answer is the maximum of `sellProfit` and `cooldownProfit`, as we want the max profit when we're not holding a stock at the end.
 *
 * Time Complexity: O(N) - We iterate through the price array once.
 * Space Complexity: O(1) - Constant extra space used for state variables.
 */
public class BuySellStockCooldown {

    public static void main(String[] args) {
        int[] prices = {10, 20, 15, 30};
        BuySellStockCooldown solution = new BuySellStockCooldown();
        int maxProfit = solution.getMaxProfit(prices);
        System.out.println("Max profit with cooldown is: " + maxProfit);
    }

    public int getMaxProfit(int[] prices) {
        if (prices == null || prices.length == 0) return 0;

        // Initial states
        int holdProfit = -prices[0]; // We bought stock on the first day
        int sellProfit = 0;          // No stock sold initially
        int cooldownProfit = 0;      // No cooldown initially

        for (int i = 1; i < prices.length; i++) {
            // New state calculations
            int newHoldProfit = Math.max(holdProfit, cooldownProfit - prices[i]); // Either keep holding or buy today
            int newSellProfit = holdProfit + prices[i]; // Sell the stock today
            int newCooldownProfit = Math.max(cooldownProfit, sellProfit); // Either stay in cooldown or enter it after selling

            // Update states for the next iteration
            holdProfit = newHoldProfit;
            sellProfit = newSellProfit;
            cooldownProfit = newCooldownProfit;
        }
        
        // The maximum profit at the end must be in a non-holding state (sell or cooldown)
        return Math.max(sellProfit, cooldownProfit);
    }
}