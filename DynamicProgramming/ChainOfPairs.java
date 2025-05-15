package DynamicProgramming;

import java.util.Arrays;

/**
 * LeetCode Problem: Maximum Length of Pair Chain
 * Link: https://leetcode.com/problems/maximum-length-of-pair-chain/
 *
 * Problem Statement:
 * - Given an array of pairs `(a, b)`, a pair `(c, d)` can follow `(a, b)` if `b < c`.
 * - Find the maximum length of a chain that can be formed by linking such pairs.
 *
 * Approach:
 * - **Dynamic Programming (DP) Solution**:
 *   - Sort the pairs based on the **first** element.
 *   - Use `dp[i]` to store the longest chain ending at index `i`.
 *   - Iterate through all previous pairs to find the longest valid chain.
 *
 * Time Complexity: **O(n^2)** (Nested loop for DP transition)
 * Space Complexity: **O(n)** (DP array storing results)
 */
public class ChainOfPairs {

    public static void main(String[] args) {
        int[][] pairs = {
                {5, 14},
                {39, 60},
                {15, 28},
                {27, 40},
                {50, 90}
        };
        ChainOfPairs solver = new ChainOfPairs();
        System.out.println("Maximum chain length: " + solver.maxLengthOfPairChain(pairs));
    }

    /**
     * Computes the maximum length of a chain that can be formed with given pairs.
     *
     * @param pairs 2D array where pairs[i] = {a, b}
     * @return Maximum length of the chain
     */
    public int maxLengthOfPairChain(int[][] pairs) {
        int len = pairs.length;
        if (len == 0) return 0;

        // Sort pairs based on the first element to ensure correct DP order
        Arrays.sort(pairs, (a, b) -> Integer.compare(a[0], b[0]));

        int[] dp = new int[len];
        Arrays.fill(dp, 1); // Each pair is a valid chain of at least length 1

        int maxChainLength = 1;

        // Compute the longest chain ending at each pair
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < i; j++) {
                if (pairs[j][1] < pairs[i][0]) { // Valid chain condition
                    // This stores the maximum length of the chain ending at i
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            maxChainLength = Math.max(maxChainLength, dp[i]);
        }

        return maxChainLength;
    }

    /**
     * Alternative approach using Greedy Algorithm:
     * - Sort the pairs based on the **second** element.
     * - Iterate through the pairs and select the next pair if it is valid.
     * - This approach is more efficient than the DP solution.
     *
     * Proof that is returns correct result:
     * - By sorting the pairs based on the second element, we ensure that we always extend the chain with the smallest possible end.
     * - This way, we maximize the number of pairs we can include in the chain.
     *
     * Time Complexity: **O(n log n)** (Sorting the pairs)
     * Space Complexity: **O(1)** (No extra space used)
     *
     */
    public int maxLengthOfPairChainGreedy(int[][] pairs) {
        Arrays.sort(pairs, (a, b) -> Integer.compare(a[1], b[1])); // Sort by second element
        int count = 0, lastEnd = Integer.MIN_VALUE;
        for (int[] pair : pairs) {
            if (pair[0] > lastEnd) {
                count++;
                lastEnd = pair[1];
            }
        }
        return count;
    }
}
