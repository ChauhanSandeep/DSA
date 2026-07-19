package dynamicprogramming.MiscellaneousDP;

/**
 * Problem: Integer Break
 *
 * Break n into at least two positive integers and maximize the product of those
 * parts. Return the maximum product.
 *
 * Leetcode: https://leetcode.com/problems/integer-break/
 * Rating:   acceptance 62.5% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Dynamic programming | Integer partition | Greedy math optimization
 *
 * Example:
 *   Input:  n = 10
 *   Output: 36
 *   Why:    10 = 3 + 3 + 4, and 3*3*4 = 36 is better than any other required split.
 *
 * Follow-ups:
 *   1. Return the actual partition?
 *      Store split choices in DP or greedily output mostly 3s with the remainder rule.
 *   2. What if the product must be returned modulo a number for huge n?
 *      Use the greedy 3s insight with fast modular exponentiation.
 *   3. What if exactly k parts are required?
 *      Distribute n as evenly as possible across k parts, or run DP with part count.
 *
 * Related: Cutting Rope, 2 Keys Keyboard (650).
 */
public class IntegerBreak {

    /**
     * Intuition (interview default): products grow fastest when parts are close to
     * 3. For any remaining value greater than 4, cutting off a 3 improves or keeps
     * the product better than leaving that value whole. We stop at 4 because 2 + 2
     * is better than 3 + 1. The small n cases are special because the problem
     * requires at least two parts.
     *
     * Algorithm:
     *   1. Return the required small-n answers for 2, 3, and 4.
     *   2. While n is greater than 4, multiply product by 3 and reduce n by 3.
     *   3. Multiply by the final remaining n and return the product.
     *
     * Time:  O(n) - the loop removes 3 each iteration.
     * Space: O(1) - only the product and remaining value are stored.
     *
     * @param n integer to break
     * @return maximum product after at least one split
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

    public static void main(String[] args) {
        IntegerBreak solver = new IntegerBreak();
        int[] inputs = {2, 3, 10, 8};
        int[] expected = {1, 2, 36, 18};

        for (int i = 0; i < inputs.length; i++) {
            int got = solver.integerBreak(inputs[i]);
            System.out.printf("n=%d -> %d  expected=%d%n", inputs[i], got, expected[i]);
        }
    }
}