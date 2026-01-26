package arrays.binarysearch;

/**
 * Problem: Guess Number Higher or Lower II
 *
 * You are tasked to guess a number between 1 and n. Each time you guess a number x, you pay x dollars.
 * Your goal is to find the minimum amount of money required to guarantee a win regardless of what number is chosen.
 *
 * Example:
 *   Input: n = 10
 *   Output: 16
 *
 * Leetcode: https://leetcode.com/problems/guess-number-higher-or-lower-ii/
 *
 * Follow-up questions:
 * 1️⃣ How does this relate to the minimax approach?
 *     - It uses a variation of the minimax algorithm, ensuring the worst-case cost is minimized.
 * 2️⃣ Can you optimize further?
 *     - Since the decision range is continuous and costs accumulate, space optimization can be done by reusing a smaller matrix, but the time complexity remains.
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class GuessingGame2 {

    /**
     * Main method to run and test the minimum cost computation.
     */
    public static void main(String[] args) {
        GuessingGame2 game = new GuessingGame2();
        int result = game.getMinimumMoneyAmountIterative(10);
        System.out.println("Minimum money to guarantee a win for n=10: " + result);
    }

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
     * Iterative approach to determine the minimum amount of money required to guarantee a win.
     *
     * Steps:
     *  1. Initialize a DP table where dp[left][right] stores the minimum cost to guarantee a win in the range [left, right].
     *  2. Use bottom-up dynamic programming:
     *     - For each possible range length (from 2 to n), compute the minimum cost.
     *     - For each possible pivot in the range, compute the cost as:
     *          pivot + max(cost of guessing left subrange, cost of guessing right subrange)
     *     - Choose the pivot that results in the minimal cost.
     *  3. The final answer is dp[1][n].
     *
     * Algorithm:
     *  - Bottom-up dynamic programming with a minimax strategy.
     *
     * Time Complexity: O(n³) — triple nested loop: range length, start index, and pivot index.
     * Space Complexity: O(n²) — 2D DP array to store minimal costs.
     *
     * @param n The upper bound of the guessing range.
     * @return The minimum money required to guarantee a win.
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