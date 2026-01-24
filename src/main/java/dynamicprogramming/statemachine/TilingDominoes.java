package dynamicprogramming.statemachine;

/**
 * Problem: Count the Number of Ways to Tile a 2 x n Board Using 2 x 1 Dominoes
 *
 * Given a 2 x n board, count the number of ways to completely cover it using 2 x 1 dominoes.
 * Dominoes can be placed either vertically (covering one column) or horizontally (covering two adjacent columns).
 *
 * The problem reduces to a Fibonacci recurrence:
 *   ways[n] = ways[n-1] + ways[n-2]
 *   where:
 *     ways[1] = 1 (only vertical placement)
 *     ways[2] = 2 (either two vertical or two horizontal dominoes)
 *
 * Example:
 *   Input: n = 5
 *   Output: 8
 *   Explanation: There are 8 ways to tile a 2 x 5 board.
 *
 * Related Leetcode: https://leetcode.com/problems/domino-and-tromino-tiling/
 */
public class TilingDominoes {

    public static void main(String[] args) {
        int n = 5;
        System.out.println("Iterative DP: Ways to tile 2 x " + n + " board = " + countWaysIterative(n));
        System.out.println("Recursive with Memoization: Ways to tile 2 x " + n + " board = " + countWaysRecursive(n));
    }

    /**
     * Iterative dynamic programming approach with constant space.
     *
     * Algorithm
     * - Use two variables to store the number of ways to tile boards of length n-1 and n-2.
     * - Iterate from 3 to n, updating these two variables to get the current number of ways.
     *
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     *
     * @param n the length of the board
     * @return number of ways to tile the board
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
     * Recursive approach with memoization (top-down dynamic programming).
     *
     * @param n the length of the board
     * @return number of ways to tile the board
     */
    public static int countWaysRecursive(int n) {
        if (n <= 0) return 0;
        int[] memo = new int[n + 1]; // memo[i] will store the number of ways to tile 2 x i board
        return countWaysRecursiveHelper(n, memo);
    }

    // Helper for recursive approach with memoization
    private static int countWaysRecursiveHelper(int n, int[] memo) {
        if (n == 1) return 1;
        if (n == 2) return 2;
        if (memo[n] != 0) return memo[n];

        // Place a vertical domino (reduces problem to n-1) or two horizontal dominoes (reduces to n-2)
        memo[n] = countWaysRecursiveHelper(n - 1, memo) + countWaysRecursiveHelper(n - 2, memo);
        return memo[n];
    }
}