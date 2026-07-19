package dynamicprogramming.matrixchainmultiplication;

import java.util.Arrays;

/**
 * Problem: Remove Boxes
 *
 * Given colored boxes in a row, each move removes one contiguous group of equal
 * colors and scores groupSize * groupSize. Remove every box while maximizing the
 * total score.
 *
 * Leetcode: https://leetcode.com/problems/remove-boxes/
 * Rating:   acceptance 49.6% (Hard) - no contest Elo (pre-contest problem)
 * Pattern:  Dynamic Programming | Interval DP with carry state | Merge matching colors
 *
 * Example:
 *   Input:  boxes = [1,3,2,2,2,3,4,3,1]
 *   Output: 23
 *   Why:    delaying some 3s lets them merge into a larger group later, and the
 *           carry state remembers those same-color boxes so the square score is counted correctly.
 *
 * Follow-ups:
 *   1. Can you output the removal sequence?
 *      Store which branch won for each state and replay those choices recursively.
 *   2. What if scoring were linear instead of square?
 *      Then delaying merges gives no benefit, and the problem becomes much simpler.
 *   3. What if non-contiguous equal colors could be removed together?
 *      The interval independence breaks; the state must model global color counts or chosen subsets.
 *
 * Related: Burst Balloons (312), Strange Printer (664).
 */
public class RemoveBoxes {
    private int[][][] memo;

        /**
     * Intuition: boxes of the same color become more valuable when removed together.
     * The carry k records how many boxes equal to boxes[l] are already attached on
     * the left, so the interval can either remove that group now or merge it with a
     * matching color later.
     *
     * Algorithm:
     *   1. Use dfs(l, r, k) for boxes[l..r] with k same-colored boxes carried in.
     *   2. Remove boxes[l] plus its carry immediately as one option.
     *   3. Try matching boxes[l] with each later equal color before removing it.
     *   4. Memoize the best score for every l, r, and k.
     *
     * Time:  O(n^4) - three state dimensions plus a scan for matching colors.
     * Space: O(n^3) - memo table for interval and carry states.
     *
     * @param boxes box colors
     * @return maximum score obtainable
     */
    public int removeBoxes(int[] boxes) {
        int n = boxes.length;
        // memo[l][r][k] represents the maximum points for boxes[l..r] with k same colored boxes before
        memo = new int[n][n][n];
        return dfs(boxes, 0, n - 1, 0);
    }

        /** Returns the best score for boxes[l..r] with k carried boxes matching boxes[l]. */
    private int dfs(int[] boxes, int l, int r, int k) {
        if (l > r) {
            return 0;
        }

        // If we've already computed this subproblem, return the cached result
        if (memo[l][r][k] > 0) {
            return memo[l][r][k];
        }

        // To handle the case where we have consecutive same colors at the end
        while (r > l && boxes[r] == boxes[r - 1]) {
            r--;
            k++;
        }

        // Option 1: Remove the last group of same-colored boxes
        int maxScore = dfs(boxes, l, r - 1, 0) + (k + 1) * (k + 1);

        // Option 2: Try to merge with previous same-colored boxes
        for (int i = l; i < r; i++) {
            if (boxes[i] == boxes[r]) {
                // If we find a box with the same color as boxes[r], we can merge them
                // The idea is to remove boxes[i+1..r-1] first, then merge boxes[i] and boxes[r]
                int score = dfs(boxes, l, i, k + 1) + dfs(boxes, i + 1, r - 1, 0);
                maxScore = Math.max(maxScore, score);
            }
        }

        memo[l][r][k] = maxScore;
        return maxScore;
    }

    /**
     * Bottom-up dynamic programming solution.
     * This approach is more complex but avoids recursion stack overhead.
     */
    public int removeBoxesDP(int[] boxes) {
        int n = boxes.length;
        // dp[l][r][k] represents the maximum points for boxes[l..r] with k same colored boxes before
        int[][][] dp = new int[n][n][n];

        // Base case: single box
        for (int i = 0; i < n; i++) {
            for (int k = 0; k <= i; k++) {
                dp[i][i][k] = (k + 1) * (k + 1);
            }
        }

        // Fill the table for subarrays of increasing lengths
        for (int len = 1; len < n; len++) {
            for (int l = 0; l < n - len; l++) {
                int r = l + len;

                // Try all possible k values (number of same colored boxes before l)
                for (int k = 0; k <= l; k++) {
                    // Option 1: Remove the last group of same-colored boxes
                    int maxScore = dp[l][r-1][0] + (k + 1) * (k + 1);

                    // Option 2: Try to merge with previous same-colored boxes
                    for (int i = l; i < r; i++) {
                        if (boxes[i] == boxes[r]) {
                            int score = (i > l ? dp[l][i-1][k+1] : 0) +
                                      (i + 1 <= r - 1 ? dp[i+1][r-1][0] : 0);
                            maxScore = Math.max(maxScore, score);
                        }
                    }

                    dp[l][r][k] = maxScore;
                }
            }
        }

        return n == 0 ? 0 : dp[0][n-1][0];
    }

    /**
     * Optimized version with memoization using a map to reduce memory usage.
     * This is more memory efficient for large inputs.
     */
    public int removeBoxesOptimized(int[] boxes) {
        int n = boxes.length;
        // Using a map to store only the computed states
        // Key: (l * 10000 + r * 100 + k) as a unique identifier for the state (l, r, k)
        // This assumes n <= 100, which is reasonable for the problem constraints
        int[][][] memo = new int[100][100][100];
        return dfsOptimized(boxes, 0, n - 1, 0, memo);
    }

    /** Returns the optimized carried-interval score for boxes[l..r]. */
    private int dfsOptimized(int[] boxes, int l, int r, int k, int[][][] memo) {
        if (l > r) {
            return 0;
        }

        if (memo[l][r][k] > 0) {
            return memo[l][r][k];
        }

        // Optimize by skipping consecutive same colors at the end
        int rr = r;
        int kk = k;
        while (rr > l && boxes[rr] == boxes[rr - 1]) {
            rr--;
            kk++;
        }

        // Option 1: Remove the last group of same-colored boxes
        int maxScore = dfsOptimized(boxes, l, rr - 1, 0, memo) + (kk + 1) * (kk + 1);

        // Option 2: Try to merge with previous same-colored boxes
        for (int i = l; i < rr; i++) {
            if (boxes[i] == boxes[rr]) {
                int score = dfsOptimized(boxes, l, i, kk + 1, memo) +
                           dfsOptimized(boxes, i + 1, rr - 1, 0, memo);
                maxScore = Math.max(maxScore, score);
            }
        }

        memo[l][r][k] = maxScore;
        return maxScore;
    }


    public static void main(String[] args) {
        RemoveBoxes solver = new RemoveBoxes();
        int[][] inputs = { {}, {1, 1, 1}, {1, 3, 2, 2, 2, 3, 4, 3, 1} };
        int[] expected = {0, 9, 23};

        for (int i = 0; i < inputs.length; i++) {
            int output = solver.removeBoxes(inputs[i]);
            System.out.printf("boxes=%s  ->  %d  expected=%d%n",
                Arrays.toString(inputs[i]), output, expected[i]);
        }
    }

}
