package dynamicprogramming.stockbuysell;

import java.util.Arrays;

/**
 * Problem: Best Time to Buy and Sell Stock with Cooldown
 *
 * Return the maximum profit from unlimited transactions when selling today prevents buying tomorrow. The cooldown creates separate resting and just-sold states.
 *
 * Leetcode: https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-cooldown/ (Medium)
 * Rating:   not available (not a contest problem)
 * Pattern:  Dynamic programming | Stock state machine | Cooldown constraint
 *
 * Example:
 *   Input:  prices = [1, 2, 3, 0, 2]
 *   Output: 3
 *   Why:    buy, sell, cooldown, buy, sell earns 1 + 2.
 *
 * Follow-ups:
 *   1. How would you return an actual solution, not only the value?
 *      Store predecessor or choice information while filling the same states.
 *   2. How can space be reduced?
 *      Keep only the previous row or active states when the recurrence allows it.
 *   3. How would constraints such as fees, limits, or weights change it?
 *      Add the constraint to the state or transition and keep the same invariant.
 *
 * Related: Stock II (122), Stock IV (188), Transaction Fee (714).
 */
public class BestTimeToBuyAndSellStockWithCooldown {

        public static void main(String[] args) {
        BestTimeToBuyAndSellStockWithCooldown solution = new BestTimeToBuyAndSellStockWithCooldown();
        int[][] priceCases = { {1, 2, 3, 0, 2}, {1}, {10, 20, 15, 30} };
        int[] expected = {3, 0, 20};
        for (int i = 0; i < priceCases.length; i++) {
            int got = solution.maxProfit(priceCases[i]);
            System.out.printf("prices=%s -> %d  expected=%d%n", Arrays.toString(priceCases[i]), got, expected[i]);
        }
    }

        /**
     * Intuition: the day ends in hasStock, noStock, or justSold. hasStock can keep holding or buy from noStock; noStock can rest or finish cooldown; justSold can only come from selling yesterday's held stock.
     *
     * Algorithm:
     *   1. Return 0 for null or one-day inputs.
     *   2. Initialize the three states for day 0.
     *   3. For each later day, save previous states.
     *   4. Apply the three original state transitions.
     *   5. Return the better no-stock ending state.
     *
     * Time:  O(n) - one scan over prices.
     * Space: O(1) - three states are stored.
     *
     * @param prices daily prices
     * @return maximum profit with cooldown
     */
public int maxProfit(int[] prices) {
        if (prices == null || prices.length <= 1) {
            return 0;
        }

        int hasStock = -prices[0];  // Bought stock on day 0
        int noStock = 0;            // Didn't buy anything
        int justSold = 0;           // Can't sell on day 0 (no stock to sell)

        for (int i = 1; i < prices.length; i++) {
            int prevHasStock = hasStock;
            int prevNoStock = noStock;
            int prevJustSold = justSold;

            // Update hasStock: either keep holding or buy today from noStock state
            hasStock = Math.max(prevHasStock, prevNoStock - prices[i]);

            // Update noStock: either continue resting or finish cooldown from justSold
            noStock = Math.max(prevNoStock, prevJustSold);

            // Update justSold: sell stock we were holding
            justSold = prevHasStock + prices[i];
        }

        // Maximum profit when we don't hold any stock at the end
        return Math.max(noStock, justSold);
    }

    /**
     * Alternative approach using simplified two-state DP.
     *
     * Algorithm:
     * States:
     * - buy[i]: Max profit after buying or holding stock on day i
     * - sell[i]: Max profit after selling or not having stock on day i
     *
     * Transitions:
     * - buy[i] = max(buy[i-1], sell[i-2] - prices[i]) // Must use sell[i-2] to account for cooldown day
     * - sell[i] = max(sell[i-1], buy[i-1] + prices[i])
     *
     * Time Complexity: O(N) where N is the number of days.
     * Space Complexity: O(N) for buy and sell arrays. Can be optimized to O(1).
     *
     * @param prices array of stock prices per day
     * @return maximum profit achievable with cooldown constraint
     */
    public int maxProfitTwoState(int[] prices) {
        int length = prices.length;
        if (length <= 1) return 0;

        int[] buy = new int[length];
        int[] sell = new int[length];

        buy[0] = -prices[0]; // Bought stock on day 0
        buy[1] = Math.max(-prices[0], -prices[1]); // Either keep day 0 buy or buy today

        sell[0] = 0; // No stock sold on day 0
        sell[1] = Math.max(0, prices[1] - prices[0]); // Either no transaction or sell today from day 0 buy

        for (int i = 2; i < length; i++) {
            // Can buy today only if we were in sell state 2 days ago (after cooldown)
            buy[i] = Math.max(buy[i - 1], sell[i - 2] - prices[i]);

            // Can sell today if we held stock yesterday
            sell[i] = Math.max(sell[i - 1], buy[i - 1] + prices[i]);
        }

        return sell[length - 1];
    }
}
