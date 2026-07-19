package arrays.binarysearch;

/**
 * Problem: Guess Number Higher or Lower II
 *
 * Guess a number from 1..n, paying x dollars whenever guess x is wrong. Return the minimum money that guarantees a win in the worst case.
 *
 * Leetcode: https://leetcode.com/problems/guess-number-higher-or-lower-ii/ (Medium)
 * Rating:   acceptance 53.1% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Interval DP | Minimax | Worst-case optimization
 *
 * Example:
 *   Input:  n = 10
 *   Output: 16
 *   Why:    the optimal decision tree has worst-case paid guesses totaling 16.
 *
 * Follow-ups:
 *   1. Recover the strategy? Store the best pivot for each interval.
 *   2. Different guess costs? Replace pivot with cost[pivot] in the recurrence.
 *   3. Minimize expected cost? Use probabilities and expected-value DP.
 *   4. Many n queries? Build the DP table up to the maximum n once.
 *
 * Related: Guess Number Higher or Lower (374), Predict the Winner (486).
 */
public class GuessingGame2 {

    public static void main(String[] args) {
        GuessingGame2 solver = new GuessingGame2();
        int[] inputs = { 1, 2, 10 };
        int[] expected = { 0, 1, 16 };
        for (int i = 0; i < inputs.length; i++) {
            int got = solver.getMinimumMoneyAmountIterative(inputs[i]);
            System.out.printf("n=%d -> %d  expected=%d%n", inputs[i], got, expected[i]);
        }
    }


    /**
     * Main method to run and test the minimum cost computation.
     */


    /**
     * Public method to initiate the recursive guessing game.
     *
     * @param n The upper bound of the guessing range.
     * @return The minimum money required to guarantee a win.
     */
    public int getMinimumMoneyAmountRecursive(int n) {
        int[][] memo = new int[n + 1][n + 1];
        return computeCostRec(1, n, memo);
    }

    /**
     * Recursively computes the minimum cost to guarantee a win in the range [start, end].
     *
     * Steps:
     *  1. Base Case: If start >= end, no cost needed.
     *  2. Check if already computed in memo.
     *  3. Try all possible pivots from start to end.
     *  4. For each pivot, calculate:
     *      - cost of pivot
     *      - worst-case cost of left and right subranges
     *  5. Choose the pivot that results in the minimum total cost.
     *
     * Algorithm:
     *  - Top-down DP (recursion + memoization), following a minimax strategy.
     *
     * Time Complexity: O(n³) 
     *  - memo table: (n+1) x (n+1) → O(n²)
     *  - for each entry, we try up to n pivots → O(n)
     * Space Complexity: O(n²) for the memo table.
     *
     * @param start Starting point of the range.
     * @param end Ending point of the range.
     * @param memo Memoization table.
     * @return Minimum cost to guarantee a win in [start, end].
     */
    private int computeCostRec(int start, int end, int[][] memo) {
        if (start >= end) return 0; // No cost when only one number or invalid range

        if (memo[start][end] != 0) return memo[start][end];

        int minCost = Integer.MAX_VALUE;

        for (int pivot = start; pivot <= end; pivot++) {
            // Cost of choosing pivot + worst-case of guessing left or right range
            int costLeft = computeCostRec(start, pivot - 1, memo);
            int costRight = computeCostRec(pivot + 1, end, memo);
            // Math.max is used to ensure we consider the worst-case scenario
            int currentCost = pivot + Math.max(costLeft, costRight);

            // Among all possible pivots in this range, pick the pivot that results in the smallest worst-case cost.
            minCost = Math.min(minCost, currentCost);
        }

        memo[start][end] = minCost; // Memoize the result
        return minCost;
    }

        /**
     * Intuition: For interval [left, right], choosing pivot costs pivot plus the worse remaining side. The best guaranteed cost minimizes that worst-case choice over all pivots.
     *
     * Algorithm:
     *   1. Let dp[left][right] store minimum guaranteed cost.
     *   2. Fill intervals by increasing length.
     *   3. Try each pivot from midpoint through right.
     *   4. Store the minimum worst-case cost and return dp[1][n].
     *
     * Time:  O(n^3) - length, left, and pivot loops are nested.
     * Space: O(n^2) - all interval costs are stored.
     *
     * @param n upper bound of the range
     * @return minimum money needed to guarantee a win
     */
    public int getMinimumMoneyAmountIterative(int n) {
        int[][] dp = new int[n + 1][n + 1];

        // len represents the length of the current range
        for (int len = 2; len <= n; len++) {
            // left is the starting point of the range
            for (int left = 1; left <= n - len + 1; left++) {
                int right = left + len - 1;
                dp[left][right] = Integer.MAX_VALUE;

                // Try every possible pivot (midpoint to right for optimality)
                for (int pivot = left + (right - left) / 2; pivot <= right; pivot++) {
                    // Cost of choosing pivot + worst-case cost from either subrange
                    int costLeft = (pivot > left) ? dp[left][pivot - 1] : 0;
                    int costRight = (pivot < right) ? dp[pivot + 1][right] : 0;
                    int currentCost = pivot + Math.max(costLeft, costRight);

                    // Update to minimum cost for the current range
                    dp[left][right] = Math.min(dp[left][right], currentCost);
                }
            }
        }

        return dp[1][n];
    }
}