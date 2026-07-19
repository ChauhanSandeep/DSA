package dynamicprogramming.MiscellaneousDP;

/**
 * Problem: Count Number of Ways to Place Houses
 *
 * There are n plots on each side of a street. A house may be placed or not placed
 * on each plot, but adjacent plots on the same side cannot both contain houses.
 * Return the total number of valid two-sided placements modulo 1,000,000,007.
 *
 * Leetcode: https://leetcode.com/problems/count-number-of-ways-to-place-houses/
 * Rating:   1608 (zerotrac Elo)
 * Pattern:  Dynamic programming | Fibonacci counting | Matrix exponentiation alternative
 *
 * Example:
 *   Input:  n = 2
 *   Output: 9
 *   Why:    one side has 3 valid patterns: 00, 01, and 10; the two sides are independent, so 3*3 = 9.
 *
 * Follow-ups:
 *   1. What if there are k independent sides?
 *      Raise the one-side count to the kth power modulo MOD.
 *   2. What if some plots are blocked?
 *      Reset or modify the one-side DP around blocked positions.
 *   3. What if n is extremely large?
 *      Use matrix exponentiation to compute the Fibonacci-style count in O(log n).
 *
 * Related: House Robber (198), Fibonacci Number (509), Count Vowels Permutation (1220).
 */
public class CountNumberOfWaysToPlaceHouses {
    private static final int MOD = 1_000_000_007;

    /**
     * Intuition (interview default): solve one side first. For a prefix of plots,
     * either the last plot is empty, so any valid arrangement of the previous prefix
     * works, or the last plot has a house, so the previous plot must be empty and
     * any valid arrangement up to two plots back works. That gives the Fibonacci
     * recurrence ways[i] = ways[i - 1] + ways[i - 2]. The two street sides do not
     * constrain each other, so the final answer is oneSideWays squared.
     *
     * Algorithm:
     *   1. Handle plotCount 0 and 1 exactly as the original base cases do.
     *   2. Let dp[i] be the number of valid arrangements for one side with i plots.
     *   3. Fill dp[i] = dp[i-1] + dp[i-2] because plot i is either empty or has a house after an empty plot.
     *   4. Square dp[plotCount] for the two independent sides and take MOD.
     *
     * Time:  O(n) - each plot count is computed once.
     * Space: O(n) - the original DP table stores one-side counts for every plot count.
     *
     * @param plotCount number of plots on each side of the street
     * @return number of valid two-sided placements modulo 1,000,000,007
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

    public static void main(String[] args) {
        CountNumberOfWaysToPlaceHouses solver = new CountNumberOfWaysToPlaceHouses();
        int[] inputs = {1, 2, 3};
        int[] expected = {4, 9, 25};

        for (int i = 0; i < inputs.length; i++) {
            int got = solver.countHousePlacements(inputs[i]);
            System.out.printf("n=%d -> %d  expected=%d%n", inputs[i], got, expected[i]);
        }
    }
}
