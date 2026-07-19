package dynamicprogramming.matrixchainmultiplication;

import java.util.Arrays;

import java.util.Stack;

/**
 * Problem: Minimum Cost Tree From Leaf Values
 *
 * Given leaf values in inorder order, build a binary tree where every internal
 * node has value max(left leaves) * max(right leaves). Return the minimum
 * possible sum of internal-node values.
 *
 * Leetcode: https://leetcode.com/problems/minimum-cost-tree-from-leaf-values/
 * Rating:   1919 (zerotrac Elo)
 * Pattern:  Dynamic Programming | Interval DP | Monotonic decreasing stack
 *
 * Example:
 *   Input:  arr = [6,2,4]
 *   Output: 32
 *   Why:    pairing 2 with 4 first costs 8, then pairing that subtree with 6
 *           costs 24, for total 32 instead of 36.
 *
 * Follow-ups:
 *   1. Can you also return the tree structure?
 *      Use the interval DP split table or keep parent links while applying the stack idea.
 *   2. Why does the stack solution work for positive values?
 *      Each smaller leaf should be multiplied by the smaller of its first greater neighbors.
 *   3. What if leaf values may be negative?
 *      The greedy stack property no longer holds; interval DP needs min and max subtree values.
 *
 * Related: Minimum Cost to Merge Stones (1000), Burst Balloons (312).
 */
public class MinimumCostTreeFromLeafValues {
        /**
     * Intuition: every non-leaf cost charges two leaf values when the smaller one is
     * finally paired with a greater neighbor. A decreasing stack lets each leaf pay
     * with the cheaper of its nearest greater neighbors.
     *
     * Algorithm:
     *   1. Keep a decreasing stack of leaf values.
     *   2. When a new value is larger, pop smaller leaves and charge them with the cheaper neighbor.
     *   3. Push the new value onto the stack.
     *   4. Merge the remaining stack values from the top down.
     *
     * Time:  O(n) - each value is pushed and popped at most once.
     * Space: O(n) - stack of decreasing leaf values.
     *
     * @param arr leaf values in inorder order
     * @return minimum possible non-leaf sum
     */
    public int mctFromLeafValues(int[] arr) {
        int result = 0;
        Stack<Integer> stack = new Stack<>();
        stack.push(Integer.MAX_VALUE); // Sentinel value

        for (int num : arr) {
            // While current element is greater than stack top
            while (stack.peek() <= num) {
                int mid = stack.pop();
                // Add the product of current element and stack top (or previous element if smaller)
                result += mid * Math.min(stack.peek(), num);
            }
            stack.push(num);
        }

        // Process remaining elements in the stack
        while (stack.size() > 2) {
            result += stack.pop() * stack.peek();
        }

        return result;
    }

        /**
     * Intuition: for any interval, choosing the root split joins the best tree on
     * the left with the best tree on the right. The added non-leaf value is the max
     * leaf on the left times the max leaf on the right.
     *
     * Algorithm:
     *   1. Precompute maxLeaf[i][j] for every interval.
     *   2. Fill dp[start][end] by increasing interval length.
     *   3. Try each split as the final join of two subtrees.
     *   4. Store the minimum cost for the full interval.
     *
     * Time:  O(n^3) - every interval tries every split.
     * Space: O(n^2) - DP and max-leaf tables.
     *
     * @param arr leaf values in inorder order
     * @return minimum possible non-leaf sum
     */
    public int mctFromLeafValuesDP(int[] arr) {
        int n = arr.length;
        // dp[i][j] = min sum for arr[i..j]
        int[][] dp = new int[n][n];
        // max[i][j] = max value in arr[i..j]
        int[][] max = new int[n][n];

        // Initialize max values for all ranges
        for (int i = 0; i < n; i++) {
            max[i][i] = arr[i];
            for (int j = i + 1; j < n; j++) {
                max[i][j] = Math.max(max[i][j - 1], arr[j]);
            }
        }

        // Fill dp table for different lengths
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i + len <= n; i++) {
                int j = i + len - 1;
                dp[i][j] = Integer.MAX_VALUE;

                // Try all possible partitions
                for (int k = i; k < j; k++) {
                    dp[i][j] = Math.min(dp[i][j],
                                      dp[i][k] + dp[k + 1][j] +
                                      max[i][k] * max[k + 1][j]);
                }
            }
        }

        return dp[0][n - 1];
    }


    public static void main(String[] args) {
        MinimumCostTreeFromLeafValues solver = new MinimumCostTreeFromLeafValues();
        int[][] inputs = { {4, 11}, {6, 2, 4}, {7, 12, 8, 10} };
        int[] expected = {44, 32, 284};

        for (int i = 0; i < inputs.length; i++) {
            int output = solver.mctFromLeafValues(inputs[i]);
            System.out.printf("arr=%s  ->  %d  expected=%d%n",
                Arrays.toString(inputs[i]), output, expected[i]);
        }
    }

}
