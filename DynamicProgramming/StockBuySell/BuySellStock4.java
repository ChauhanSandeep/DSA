package DynamicProgramming.StockBuySell;

/**
 * LeetCode Problem: Best Time to Buy and Sell Stock IV
 * Link: https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iv/
 *
 * Given the stock prices for each day and a maximum of 'k' transactions,
 * find the maximum profit that can be achieved.
 *
 * Approach:
 * - Uses Dynamic Programming to store intermediate profits.
 * - If k >= n/2, it reduces to the unlimited transactions case (greedy approach).
 * - Uses a DP table where profit[i][j] represents the max profit with at most i transactions till day j.
 * 
 * Time Complexity: 
 * - O(k * n^2) for the naive DP solution.
 * - O(k * n) for the optimized DP approach.
 * 
 * Space Complexity:
 * - O(k * n) due to the DP table.
 */

public class BuySellStock4 {

    public static void main(String[] args) {
        int[] prices = {19230, 13765, 6863, 3840, 8367, 15603, 16327, 15140, 5582, 12937, 9472, 14190, 
                        9541, 4126, 2757, 400, 19685, 15908, 4929, 18136, 16050, 6622, 13439, 265, 
                        5846, 3188, 8756, 4960, 18781, 11139, 5152, 12314 };
        int k = 100000089;

        BuySellStock4 stockSolver = new BuySellStock4();
        System.out.println(stockSolver.maxProfit(prices, k));
    }

    /**
     * Naive Dynamic Programming Approach
     * Uses a DP table where profit[i][j] stores max profit with at most i transactions till day j.
     * 
     * Time Complexity: O(k * n^2)
     * Space Complexity: O(k * n)
     */
    public int naiveMaxProfit(int[] prices, int k) {
        if (prices.length <= 1 || k <= 0) return 0;

        int n = prices.length;
        int[][] profit = new int[k + 1][n];

        for (int i = 1; i <= k; i++) {
            for (int j = 1; j < n; j++) {
                profit[i][j] = Math.max(profit[i][j - 1], findMaxProfit(prices, profit, i - 1, j));
            }
        }

        return profit[k][n - 1];
    }

    /**
     * Helper function to calculate the maximum possible profit for a given day.
     * Iterates through previous days to determine the best transaction.
     */
    private int findMaxProfit(int[] prices, int[][] profit, int transactions, int day) {
        int maxProfit = 0;
        for (int prevDay = 0; prevDay < day; prevDay++) {
            maxProfit = Math.max(maxProfit, profit[transactions][prevDay] + (prices[day] - prices[prevDay]));
        }
        return maxProfit;
    }

    /**
     * Optimized Dynamic Programming Approach
     * Uses an additional variable 'prevMax' to avoid redundant computations.
     * 
     * Time Complexity: O(k * n)
     * Space Complexity: O(k * n)
     */
    public int maxProfit(int[] prices, int k) {
        int n = prices.length;
        if (n <= 1 || k <= 0) return 0;

        // If transactions allowed are greater than possible transactions, use quickSolve (greedy)
        if (k >= n / 2) return quickSolve(prices);

        int[][] profit = new int[k + 1][n];

        for (int transaction = 1; transaction <= k; transaction++) {
            int prevMax = -prices[0]; // Tracks the best balance after buying a stock
            for (int day = 1; day < n; day++) {
                // Either don't trade today or sell at today's price for max profit
                profit[transaction][day] = Math.max(profit[transaction][day - 1], prices[day] + prevMax);

                // Update prevMax to consider buying at the best possible price
                prevMax = Math.max(prevMax, profit[transaction - 1][day - 1] - prices[day]);
            }
        }

        return profit[k][n - 1];
    }

    /**
     * QuickSolve: Greedy approach for unlimited transactions (k >= n/2).
     * If transactions are unrestricted, simply sum up all profitable trades.
     * 
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     */
    private int quickSolve(int[] prices) {
        int maxProfit = 0;
        for (int i = 1; i < prices.length; i++) {
            // If there's a profit opportunity, take it
            if (prices[i] > prices[i - 1]) {
                maxProfit += prices[i] - prices[i - 1];
            }
        }
        return maxProfit;
    }
}
