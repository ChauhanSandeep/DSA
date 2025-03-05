package DynamicProgramming.StockBuySell;

/**
 * Problem: Best Time to Buy and Sell Stock with Transaction Fee
 * Given an array `prices[]` where `prices[i]` represents the stock price on day `i`,
 * and a transaction fee, find the maximum profit possible with **infinite** transactions.
 * A transaction fee must be paid upon selling a stock.
 *
 * Approach:
 * - Use **Dynamic Programming** to maintain two states:
 *   1. `buyStateProfit` → Maximum profit when holding a stock.
 *   2. `sellStateProfit` → Maximum profit when not holding a stock.
 * - Transition states based on whether we buy or sell at each step.
 *
 * Time Complexity: O(N) → Single pass through the array.
 * Space Complexity: O(1) → Only a few extra variables used.
 *
 * LeetCode Link: https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/
 */
public class BuySellStock1 {

    public static void main(String[] args) {
        int[] prices = {10, 20, 5, 40};
        int fee = 3;
        int profit = getMaxProfit(prices, fee);
        System.out.println("Max profit gained with fee of " + fee + " is " + profit);
    }

    /**
     * Calculates the maximum profit possible with infinite transactions and a transaction fee.
     *
     * @param prices Array of stock prices.
     * @param fee    Transaction fee to be paid when selling a stock.
     * @return Maximum profit possible.
     */
    public static int getMaxProfit(int[] prices, int fee) {
        if (prices == null || prices.length < 2) {
            return 0; // No transactions possible
        }

        int buyStateProfit = -prices[0]; // Initial state: buying stock on day 0
        int sellStateProfit = 0;         // Initial state: no stock, zero profit

        for (int i = 1; i < prices.length; i++) {
            // newSellStateProfit is the maximum profit when not holding a stock
            int newSellStateProfit = Math.max(sellStateProfit, buyStateProfit + prices[i] - fee);
            // newBuyStateProfit is the maximum profit when holding a stock
            int newBuyStateProfit = Math.max(buyStateProfit, sellStateProfit - prices[i]);

            sellStateProfit = newSellStateProfit;
            buyStateProfit = newBuyStateProfit;
        }

        return sellStateProfit;
    }
}
