package dynamicprogramming.MiscellaneousDP;

/**
 * 343. Integer Break
 *
 * Problem: Given an integer n, break it into the sum of k positive integers
 * where k >= 2, and maximize the product of those integers.
 *
 * Example:
 * Input: n = 10
 * Output: 36
 * Explanation: 10 = 3 + 3 + 4, 3 × 3 × 4 = 36
 *
 * LeetCode: https://leetcode.com/problems/integer-break
 *
 * Follow-up questions:
 * Q: What if we want to minimize the number of parts k?
 * A: Use as many 3s as possible, then handle remainders with 2s and 4s.
 *
 * Q: Can we extend this to real numbers?
 * A: Yes, optimal split approaches e ≈ 2.718, so use 3s in discrete case.
 *
 * Q: How to handle very large n efficiently?
 * A: Use mathematical formula with modular exponentiation.
 */
public class IntegerBreak {

    /**
     * Mathematical solution based on the fact that optimal splits use mostly 3s.
     *
     * Algorithm: Greedy approach using mathematical insight
     * - For n > 4, optimal solution uses as many 3s as possible
     * - Handle remainders: if remainder = 1, use one less 3 and add 4
     * - if remainder = 2, add one 2
     * - Special cases for n ≤ 4
     *
     * Time Complexity: O(log n) for exponentiation
     * Space Complexity: O(1)
     */
    public int integerBreak(int n) {
        if (n == 2) return 1;
        if (n == 3) return 2;
        if (n == 4) return 4;

        int product = 1;

        // Use as many 3s as possible
        while (n > 4) {
            product *= 3;
            n -= 3;
        }

        // Handle remaining part
        product *= n;

        return product;
    }

    /**
     * Dynamic Programming approach for understanding the recurrence relation.
     * Builds solution bottom-up from smaller subproblems.
     */
    public int integerBreakDP(int n) {
        int[] dp = new int[n + 1];

        // Base cases
        dp[0] = 0;
        dp[1] = 1;

        for (int i = 2; i <= n; i++) {
            // Try all possible first splits
            for (int j = 1; j < i; j++) {
                // Either don't split j further, or use optimal split of j
                int maxJ = Math.max(j, dp[j]);
                int remaining = i - j;
                int maxRemaining = Math.max(remaining, dp[remaining]);

                dp[i] = Math.max(dp[i], maxJ * maxRemaining);
            }
        }

        return dp[n];
    }

    /**
     * Optimized DP that considers whether to split each part further.
     * More explicit about the decision to split or not split.
     */
    public int integerBreakDPOptimized(int n) {
        int[] dp = new int[n + 1];

        for (int i = 2; i <= n; i++) {
            for (int j = 1; j < i; j++) {
                // j * (i-j): don't split either part further
                // j * dp[i-j]: split the remaining part optimally
                // dp[j] * (i-j): split j optimally, don't split remaining
                dp[i] = Math.max(dp[i], Math.max(j * (i - j), j * dp[i - j]));
            }
        }

        return dp[n];
    }

    /**
     * Recursive solution with memoization.
     * Demonstrates top-down approach to the problem.
     */
    public int integerBreakMemo(int n) {
        Integer[] memo = new Integer[n + 1];
        return breakHelper(n, memo);
    }

    private int breakHelper(int n, Integer[] memo) {
        if (n <= 4) {
            return n == 4 ? 4 : n - 1;  // For n<=3, optimal is n-1
        }

        if (memo[n] != null) {
            return memo[n];
        }

        int maxProduct = 0;

        // Try all possible splits
        for (int i = 2; i < n; i++) {
            int product = i * breakHelper(n - i, memo);
            maxProduct = Math.max(maxProduct, product);
        }

        memo[n] = maxProduct;
        return maxProduct;
    }

    /**
     * Mathematical formula-based solution.
     * Uses closed form based on the insight that 3 is optimal.
     */
    public int integerBreakFormula(int n) {
        if (n == 2) return 1;
        if (n == 3) return 2;

        // Number of 3s to use
        int quotient = n / 3;
        int remainder = n % 3;

        if (remainder == 0) {
            // n is divisible by 3
            return (int) Math.pow(3, quotient);
        } else if (remainder == 1) {
            // n = 3k + 1, use (k-1) 3s and one 4
            return (int) Math.pow(3, quotient - 1) * 4;
        } else {
            // n = 3k + 2, use k 3s and one 2
            return (int) Math.pow(3, quotient) * 2;
        }
    }

    /**
     * Iterative approach that explicitly builds the optimal partition.
     * Returns both the maximum product and the actual partition.
     */
    public Result integerBreakWithPartition(int n) {
        if (n == 2) return new Result(1, new int[]{1, 1});
        if (n == 3) return new Result(2, new int[]{1, 2});

        java.util.List<Integer> partition = new java.util.ArrayList<>();
        int remaining = n;

        // Add as many 3s as possible
        while (remaining > 4) {
            partition.add(3);
            remaining -= 3;
        }

        // Handle the remainder
        if (remaining == 4) {
            partition.add(2);
            partition.add(2);
        } else {
            partition.add(remaining);
        }

        // Calculate product
        int product = 1;
        for (int part : partition) {
            product *= part;
        }

        return new Result(product, partition.stream().mapToInt(i -> i).toArray());
    }

    // Helper class to return both product and partition
    public static class Result {
        public final int maxProduct;
        public final int[] partition;

        public Result(int maxProduct, int[] partition) {
            this.maxProduct = maxProduct;
            this.partition = partition;
        }
    }

    /**
     * Brute force recursive solution for small inputs.
     * Explores all possible ways to break the number.
     */
    public int integerBreakBruteForce(int n) {
        return breakRecursive(n, false);
    }

    private int breakRecursive(int n, boolean mustBreak) {
        if (n == 1) return mustBreak ? 0 : 1;
        if (n <= 0) return 0;

        int maxProduct = mustBreak ? 0 : n;

        // Try all possible first parts
        for (int i = 1; i < n; i++) {
            int product = i * breakRecursive(n - i, false);
            maxProduct = Math.max(maxProduct, product);
        }

        return maxProduct;
    }

    /**
     * Solution that works for very large n using modular arithmetic.
     * Handles cases where the result might overflow.
     */
    public long integerBreakLarge(int n, int mod) {
        if (n == 2) return 1;
        if (n == 3) return 2;

        long result = 1;

        // Use as many 3s as possible
        while (n > 4) {
            result = (result * 3) % mod;
            n -= 3;
        }

        result = (result * n) % mod;

        return result;
    }

    /**
     * Generic solution that finds optimal k (number of parts).
     * Explores different values of k to find the best partition.
     */
    public int integerBreakOptimalK(int n) {
        int maxProduct = 0;

        // Try different number of parts k
        for (int k = 2; k <= n; k++) {
            int product = calculateProductForK(n, k);
            maxProduct = Math.max(maxProduct, product);
        }

        return maxProduct;
    }

    // Calculate maximum product when splitting into exactly k parts
    private int calculateProductForK(int n, int k) {
        int quotient = n / k;
        int remainder = n % k;

        // remainder parts get (quotient + 1), others get quotient
        int product = 1;

        for (int i = 0; i < k - remainder; i++) {
            product *= quotient;
        }

        for (int i = 0; i < remainder; i++) {
            product *= (quotient + 1);
        }

        return product;
    }
}