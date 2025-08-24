package dynamicprogramming.StockBuySell;

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
     * Calculates maximum profit with cooldown using state machine DP approach.
     *
     * Algorithm Steps:
     * 1. Define three states: held (holding stock), sold (just sold stock), rest (cooldown/no stock)
     * 2. Initialize states for day 0
     * 3. For each day, compute new states based on previous day's states:
     *    - held: max(previous held, previous rest - current price)
     *    - sold: previous held + current price
     *    - rest: max(previous rest, previous sold)
     * 4. Return max of sold and rest (not holding stock at end)
     *
     * Time Complexity: O(n) where n is number of days
     * Space Complexity: O(1) using state variables
     *
     * @param prices array of stock prices for each day
     * @return maximum profit achievable
     */
    public int maxProfit(int[] prices) {
        if (prices == null || prices.length <= 1) {
            return 0;
        }

        // State definitions:
        // heldProfit: maximum profit when holding a stock
        // soldProfit: maximum profit after selling stock today (must cooldown tomorrow)
        // restProfit: maximum profit when not holding stock and can buy
        int heldProfit = -prices[0];  // Buy on first day
        int soldProfit = 0;           // Cannot sell on first day
        int restProfit = 0;           // No action on first day

        for (int dayIndex = 1; dayIndex < prices.length; dayIndex++) {
            int currentPrice = prices[dayIndex];

            // Calculate new states based on current day
            int newHeldProfit = Math.max(heldProfit, restProfit - currentPrice);
            int newSoldProfit = heldProfit + currentPrice;
            int newRestProfit = Math.max(restProfit, soldProfit);

            // Update states for next iteration
            heldProfit = newHeldProfit;
            soldProfit = newSoldProfit;
            restProfit = newRestProfit;
        }

        // Return max profit when not holding stock (either sold or rest state)
        return Math.max(soldProfit, restProfit);
    }

    /**
     * Alternative approach using explicit 2D DP table for clarity.
     *
     * Algorithm Steps:
     * 1. Create 2D DP table where dp[i][state] represents max profit on day i in given state
     * 2. States: 0 = rest/cooldown, 1 = held, 2 = sold
     * 3. Fill table based on state transitions
     * 4. Return dp[n-1][0] or dp[n-1][2] (not holding stock)
     *
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     *
     * @param prices array of stock prices for each day
     * @return maximum profit achievable
     */
    public int maxProfitDP(int[] prices) {
        if (prices == null || prices.length <= 1) {
            return 0;
        }

        int numDays = prices.length;
        // dp[i][0] = rest, dp[i][1] = held, dp[i][2] = sold
        int[][] dp = new int[numDays][3];

        // Initialize first day
        dp[0][0] = 0;           // rest
        dp[0][1] = -prices[0];  // held (bought today)
        dp[0][2] = 0;           // sold (impossible on day 0)

        for (int day = 1; day < numDays; day++) {
            // Rest: either continue resting or cooldown after selling yesterday
            dp[day][0] = Math.max(dp[day - 1][0], dp[day - 1][2]);

            // Held: either continue holding or buy today (only from rest state)
            dp[day][1] = Math.max(dp[day - 1][1], dp[day - 1][0] - prices[day]);

            // Sold: sell stock held from previous day
            dp[day][2] = dp[day - 1][1] + prices[day];
        }

        // Maximum profit when not holding stock
        return Math.max(dp[numDays - 1][0], dp[numDays - 1][2]);
    }
}
