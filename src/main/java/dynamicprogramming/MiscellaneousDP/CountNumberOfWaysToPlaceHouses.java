package dynamicprogramming.MiscellaneousDP;

/**
 * Problem: Count Number Of Ways To Place Houses (LeetCode #2320)
 *
 * Problem Statement:
 * There is a street with n * 2 plots, where there are n plots on each side of the street. The plots on each side are
 * numbered from 1 to n. On each plot, a house can be placed. Return the number of ways houses can be placed such
 * that no two houses are adjacent to each other on the same side of the street. The answer may be very large, so
 * return it modulo 10^9 + 7.
 *
 * Example 1:
 * Input: n = 1
 * Output: 4
 * Explanation: Possible arrangements:
 * 1. All plots are empty.
 * 2. A house is placed on one side.
 * 3. A house is placed on the other side.
 * 4. Two houses (one on each side) are placed.
 *
 * Example 2:
 * Input: n = 2
 * Output: 9
 *
 * Approach:
 * This problem can be broken down into counting the number of ways to place houses on one side of the street,
 * and then squaring that number since both sides are independent. The key insight is recognizing that the problem
 * for one side is similar to the House Robber problem but for counting valid arrangements rather than maximizing profit.
 *
 * For one side of the street:
 * - If we place a house at position i, we cannot place at i-1
 * - If we don't place a house at position i, we can make a choice for i-1
 *
 * We can use dynamic programming where dp[i] represents the number of ways to arrange houses up to position i.
 * The recurrence relation is: dp[i] = dp[i-1] + dp[i-2] (similar to Fibonacci)
 *
 * Time Complexity: O(n) - We compute the result in a single pass
 * Space Complexity: O(1) - We only use constant extra space
 *
 * Follow-up Questions:
 * 1. What if the street has k sides instead of 2?
 *    Answer: The result would be (ways_for_one_side)^k, as each side is independent.
 *
 * 2. What if there are additional constraints, like certain plots being unavailable?
 *    Answer: We would need to modify the DP approach to skip unavailable plots and adjust the recurrence relation accordingly.
 *
 * 3. What if we need to count the number of houses placed in each valid arrangement?
 *    Answer: We could extend the DP state to track both the count of arrangements and the total number of houses placed.
 *
 * LeetCode: https://leetcode.com/problems/count-number-of-ways-to-place-houses/
 * LeetCode Contest Rating: 1608
 */
public class CountNumberOfWaysToPlaceHouses {
    private static final int MOD = 1_000_000_007;

    /**
     * Calculates the number of ways to place houses on both sides of the street.
     *
     * Steps to solve:
     * 1. For one side of the street, the problem reduces to counting binary strings of length plotCount with no two consecutive 1's.
     * 2. We can use dynamic programming where dp[i] represents the number of ways to arrange houses up to position i.
     * 3. The recurrence relation is: dp[i] = dp[i-1] + dp[i-2] (similar to Fibonacci)
     * 4. For the other side of the street, the count would be the same since both sides are identical.
     * 5. The total number of ways is (ways_for_one_side * ways_for_other_side) % MOD.
     *
     * Time Complexity: O(n)
     * Space Complexity: O(n) for the DP array, can be optimized to O(1) with two variables.
     *
     * @param plotCount The number of plots on each side of the street
     * @return The number of ways to place houses on both sides of the street modulo 10^9 + 7
     */
    public int countHousePlacements(int plotCount) {
        if (plotCount == 0) {
            return 0;
        }
        if (plotCount == 1) {
            return 4; // Base case: 2^2 = 4 possible arrangements
        }

        // For one side of the street
        long[] dp = new long[plotCount + 1];
        dp[0] = 1; // Empty arrangement
        dp[1] = 2; // Either place a house or not at position 1

        for (int i = 2; i <= plotCount; i++) {
            // Either place a house at position i (then can't place at i-1)
            // or don't place at position i (then can make any choice for i-1)
            dp[i] = (dp[i-1] + dp[i-2]) % MOD;
        }

        // Total ways is (ways for one side) * (ways for other side)
        return (int)((dp[plotCount] * dp[plotCount]) % MOD);
    }

    /**
     * Space-optimized version of countHousePlacements using constant space
     *
     * Steps to solve:
     * 1. Instead of using an array to store all previous values, we only need the last two values.
     * 2. We maintain two variables to keep track of the count for the previous two positions.
     * 3. For each position i, the current count is the sum of the counts for i-1 and i-2.
     * 4. We update the previous two values as we iterate through the positions.
     * 5. The result is the square of the count for one side, modulo 10^9 + 7.
     *
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     */
    public int countHousePlacementsOptimized(int plotCount) {
        if (plotCount == 0) {
            return 0;
        }
        if (plotCount == 1) {
            return 4;
        }

        long prevPrev = 1; // dp[0] = 1 (empty arrangement)
        long prev = 2;     // dp[1] = 2 (place or not place at position 1)

        for (int i = 2; i <= plotCount; i++) {
            long current = (prev + prevPrev) % MOD;
            prevPrev = prev;
            prev = current;
        }

        return (int)((prev * prev) % MOD);
    }

    /**
     * Alternative approach using matrix exponentiation for O(log n) time complexity
     *
     * Steps to solve:
     * 1. Recognize that the problem follows the Fibonacci sequence.
     * 2. The nth Fibonacci number can be computed in O(log n) time using matrix exponentiation.
     * 3. The number of ways for one side is (n+2)th Fibonacci number.
     * 4. The result is the square of this value, modulo 10^9 + 7.
     *
     * Time Complexity: O(log n)
     * Space Complexity: O(1)
     */
    public int countHousePlacementsMatrixExp(int n) {
        if (n == 0) return 0;
        if (n == 1) return 4;

        // The number of ways for one side is (n+2)th Fibonacci number
        long fibNPlus2 = matrixFib(n + 2);
        return (int)((fibNPlus2 * fibNPlus2) % MOD);
    }

    /**
     * Helper method to compute the nth Fibonacci number using matrix exponentiation
     */
    private long matrixFib(int n) {
        if (n <= 1) return n;

        long[][] matrix = {{1, 1}, {1, 0}};
        matrixPower(matrix, n - 1);
        return matrix[0][0];
    }

    /**
     * Raises the matrix to the power of n using exponentiation by squaring
     */
    private void matrixPower(long[][] matrix, int power) {
        if (power <= 1) return;

        long[][] result = {{1, 0}, {0, 1}}; // Identity matrix

        while (power > 0) {
            if (power % 2 == 1) {
                multiplyMatrices(result, matrix);
            }
            multiplyMatrices(matrix, matrix);
            power /= 2;
        }

        // Copy result back to original matrix
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                matrix[i][j] = result[i][j];
            }
        }
    }

    /**
     * Multiplies two 2x2 matrices under modulo MOD
     */
    private void multiplyMatrices(long[][] a, long[][] b) {
        long x = (a[0][0] * b[0][0] + a[0][1] * b[1][0]) % MOD;
        long y = (a[0][0] * b[0][1] + a[0][1] * b[1][1]) % MOD;
        long z = (a[1][0] * b[0][0] + a[1][1] * b[1][0]) % MOD;
        long w = (a[1][0] * b[0][1] + a[1][1] * b[1][1]) % MOD;

        a[0][0] = x;
        a[0][1] = y;
        a[1][0] = z;
        a[1][1] = w;
    }
}
