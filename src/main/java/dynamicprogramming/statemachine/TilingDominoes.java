package dynamicprogramming.statemachine;

/**
 * Problem: Tile a 2 x n Board With Dominoes
 *
 * Count the ways to completely cover a 2 x n board using 2 x 1 dominoes. A
 * domino may be placed vertically in one column or horizontally across two
 * adjacent columns.
 *
 * Source: Classic domino tiling interview problem
 * Pattern:  Dynamic Programming | State machine | Fibonacci-style full-board state
 *
 * Example:
 *   Input:  n = 5
 *   Output: 8
 *   Why:    the last placement is either one vertical domino after a 2 x 4 board
 *           or two horizontal dominoes after a 2 x 3 board, giving ways[4] + ways[3].
 *
 * Follow-ups:
 *   1. What if the board is 3 x n?
 *      Use parity-specific recurrences or bitmask state compression.
 *   2. What if trominoes are also allowed?
 *      Add partial-board states, as in Domino and Tromino Tiling.
 *   3. Can n be extremely large?
 *      Use matrix exponentiation on the Fibonacci recurrence for O(log n) time.
 *
 * Related: Domino and Tromino Tiling (790).
 */
public class TilingDominoes {

        /**
     * Intuition: a 2 x n board can end with one vertical domino after tiling 2 x (n-1)
     * or with two horizontal dominoes after tiling 2 x (n-2). These two endings are
     * disjoint, giving the Fibonacci recurrence.
     *
     * Algorithm:
     *   1. Handle n = 0 and n = 1 base widths.
     *   2. Store ways for widths 0 and 1.
     *   3. For each larger width, add the previous two widths.
     *   4. Return ways[n].
     *
     * Time:  O(n) - one recurrence value per width.
     * Space: O(n) - DP array stores all widths.
     *
     * @param n board length
     * @return number of ways to tile a 2 x n board
     */
    public static int countWaysIterative(int n) {
        if (n <= 0) return 0;
        if (n == 1) return 1;
        if (n == 2) return 2;

        int prev2 = 1; // ways to tile 2 x (n-2)
        int prev1 = 2; // ways to tile 2 x (n-1)
        int current = 0;

        for (int i = 3; i <= n; i++) {
            // From (n-1) we can add a vertical domino and from (n-2) we can add two horizontal dominoes
            current = prev1 + prev2;
            prev2 = prev1;
            prev1 = current;
        }
        return current;
    }

        /**
     * Intuition: the same two endings define a recursion: place one vertical domino
     * at the end or two horizontal dominoes at the end. Memoization stores each
     * board length after it is computed once.
     *
     * Algorithm:
     *   1. Allocate memo values for widths up to n.
     *   2. Return base widths directly.
     *   3. Recursively add ways for n - 1 and n - 2.
     *   4. Cache each width before returning it.
     *
     * Time:  O(n) - memoization computes each width once.
     * Space: O(n) - memo array plus recursion depth.
     *
     * @param n board length
     * @return number of ways to tile a 2 x n board
     */
    public static int countWaysRecursive(int n) {
        if (n <= 0) return 0;
        int[] memo = new int[n + 1]; // memo[i] will store the number of ways to tile 2 x i board
        return countWaysRecursiveHelper(n, memo);
    }

    // Helper for recursive approach with memoization
    /** Returns memoized tiling count for a 2 x n board. */
    private static int countWaysRecursiveHelper(int n, int[] memo) {
        if (n == 1) return 1;
        if (n == 2) return 2;
        if (memo[n] != 0) return memo[n];

        // Place a vertical domino (reduces problem to n-1) or two horizontal dominoes (reduces to n-2)
        memo[n] = countWaysRecursiveHelper(n - 1, memo) + countWaysRecursiveHelper(n - 2, memo);
        return memo[n];
    }


    public static void main(String[] args) {
        int[] inputs = {0, 1, 5};
        int[] expected = {0, 1, 8};

        for (int i = 0; i < inputs.length; i++) {
            int output = countWaysIterative(inputs[i]);
            System.out.printf("n=%d  ->  %d  expected=%d%n", inputs[i], output, expected[i]);
        }
    }

}