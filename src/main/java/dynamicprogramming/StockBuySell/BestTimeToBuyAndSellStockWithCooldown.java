package dynamicprogramming.stockbuysell;

/**
 * Problem: Best Time to Buy and Sell Stock with Cooldown
 *
 * You are given an array prices where prices[i] is the price of a given stock on the ith day.
 * Find the maximum profit you can achieve. You may complete as many transactions as you like
 * (i.e., buy one and sell one share of the stock multiple times) with the following restrictions:
 * - After you sell your stock, you cannot buy stock on the next day (i.e., cooldown one day).
 *
 * Example 1:
 * Input: prices = [1,2,3,0,2]
 * Output: 3
 * Explanation: transactions = [buy, sell, cooldown, buy, sell]
 *
 * Example 2:
 * Input: prices = [1]
 * Output: 0
 *
 * LeetCode Link: https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-cooldown/
 *
 * Follow-up Questions for FAANG Interviews:
 * 1. Q: Can you solve with O(n²) space using 2D DP? A: Yes, dp[i][0/1] for hold/sold states
 * 2. Q: What if cooldown is k days instead of 1? A: Modify state to track cooldown days remaining
 * 3. Q: How to handle transaction fees? A: https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/
 * 4. Q: Limited transactions version? A: https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iv/
 * 5. Q: Return actual buy/sell days, not just profit? A: Backtrack through DP states to reconstruct path
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class BestTimeToBuyAndSellStockWithCooldown {

    public static void main(String[] args) {
        BestTimeToBuyAndSellStockWithCooldown solution = new BestTimeToBuyAndSellStockWithCooldown();

        int[] prices1 = {1, 2, 3, 0, 2};
        System.out.println("Max profit: " + solution.maxProfit(prices1)); // 3

        int[] prices2 = {1};
        System.out.println("Max profit: " + solution.maxProfit(prices2)); // 0

        int[] prices3 = {10, 20, 15, 30};
        System.out.println("Max profit: " + solution.maxProfit(prices3)); // 25
    }

    /**
     * Finds maximum profit using state machine DP with three states.
     *
     * Algorithm:
     * States represent different positions:
     * - hasStock: Currently holding a stock
     * - noStock: Not holding stock and can buy tomorrow
     * - justSold: Just sold stock, must cooldown tomorrow
     *
     * State transitions:
     * 1. hasStock[i] = max(hasStock[i-1], noStock[i-1] - prices[i])
     *    - Keep holding previous stock, or buy today from noStock state
     * 2. noStock[i] = max(noStock[i-1], justSold[i-1])
     *    - Continue not having stock, or complete cooldown from justSold
     * 3. justSold[i] = hasStock[i-1] + prices[i]
     *    - Sell the stock we were holding
     *
     * Key insight: Must track three states because after selling, we cannot immediately
     * buy (cooldown). This requires separating "no stock but can buy" from "just sold,
     * must cooldown".
     *
     * Time Complexity: O(N) where N is the number of days. Single pass through prices.
     * Space Complexity: O(1) using only three variables for current states.
     *
     * @param prices array of stock prices per day
     * @return maximum profit achievable with cooldown constraint
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
