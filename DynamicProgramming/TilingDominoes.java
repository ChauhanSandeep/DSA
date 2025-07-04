package DynamicProgramming;

/**
 * Problem: Count the Number of Ways to Tile a 2 x n Board Using 2 x 1 Dominoes
 *
 * You are given a 2 x n board. You can cover it using 2 x 1 dominoes
 * either vertically or horizontally. Return the total number of ways to fill the board completely.
 *
 * Note:
 * - A vertical domino covers 1 column.
 * - Two horizontal dominoes can cover two adjacent columns.
 *
 * Formula:
 *   dp[n] = dp[n-1] + dp[n-2]
 *
 * This is identical to Fibonacci sequence starting from:
 *   dp[1] = 1
 *   dp[2] = 2
 *
 * Leetcode Problem (related, extended version with tromino):
 * https://leetcode.com/problems/domino-and-tromino-tiling/
 *
 * Example:
 * Input: n = 5
 * Output: 8
 * Explanation:
 * Possible configurations:
 * - 5 vertical
 * - 3 vertical + 1 horizontal pair
 * - 1 vertical + 2 horizontal pairs, etc.
 *
 * Follow-Up Questions:
 * - Can you solve it using recursion with memoization?
 * - What if the board was 3 x n or n x n? (Requires matrix DP)
 * - What if dominoes had weight and cost?
 */
public class TilingDominoes {

  public static void main(String[] args) {
    int n = 5;
    System.out.println("Iterative DP: Ways to tile 2 x " + n + " board = " + countWaysIterative(n));
    System.out.println("Memoized Recursion: Ways to tile 2 x " + n + " board = " + countWaysRecursive(n));
  }

  /**
   * Iterative Dynamic Programming approach using constant space.
   *
   * Steps:
   * 1. Base cases: For length = 1 → 1 way, length = 2 → 2 ways.
   * 2. From length = 3 to length, apply Fibonacci-style recurrence.
   * 3. Use two variables to store intermediate states to save space.
   *
   * Time Complexity: O(length)
   * Space Complexity: O(1)
   */
  public static int countWaysIterative(int length) {
      if (length <= 0) return 0;
      if (length == 1) return 1;
      if (length == 2) return 2;

    int waysNMinusTwo = 1; // dp[1]
    int waysNMinusOne = 2; // dp[2]
    int currentWays = 0;

    for (int i = 3; i <= length; i++) {
      currentWays = waysNMinusOne + waysNMinusTwo;
      waysNMinusTwo = waysNMinusOne;
      waysNMinusOne = currentWays;
    }

    return currentWays;
  }

  /**
   * Recursive approach with memoization (Top-down DP)
   * Demonstrates how to use recursion + memo table to avoid recomputation.
   *
   * Time Complexity: O(length)
   * Space Complexity: O(length) (memo array + recursion stack)
   */
  public static int countWaysRecursive(int length) {
      if (length <= 0) {
          return 0;
      }
    int[] memo = new int[length + 1];
    return countWaysRecursiveHelper(length, memo);
  }

  private static int countWaysRecursiveHelper(int n, int[] memo) {
      if (n == 1) {
          return 1;
      }
      if (n == 2) {
          return 2;
      }

      if (memo[n] != 0) {
          return memo[n];
      }

    // Either place one vertical domino or two horizontal ones
    memo[n] = countWaysRecursiveHelper(n - 1, memo) + countWaysRecursiveHelper(n - 2, memo);
    return memo[n];
  }
}