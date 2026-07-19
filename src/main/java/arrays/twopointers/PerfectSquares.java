package arrays.twopointers;

import java.util.*;

/**
 * Problem: Perfect Squares
 *
 * Given an integer n, return the fewest perfect square numbers whose sum is n.
 * The primary method builds the answer bottom up for every value from 0 through
 * the target.
 *
 * Leetcode: https://leetcode.com/problems/perfect-squares/ (Medium)
 * Rating:   acceptance 56.7% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Dynamic programming | BFS shortest path | Perfect-square transitions
 *
 * Example:
 *   Input:  n = 12
 *   Output: 3
 *   Why:    12 = 4 + 4 + 4, and it cannot be represented with fewer squares.
 *
 * Follow-ups:
 *   1. What is the maximum answer for any positive n?
 *      Lagrange's four-square theorem says at most four squares are needed.
 *   2. Can this be solved with number theory faster?
 *      Check one square, two squares, and Legendre's three-square theorem cases.
 *   3. Return the actual squares used?
 *      Store predecessor choices in the DP table and reconstruct from n.
 *
 * Related: Coin Change (322), Sum of Square Numbers (633).
 */
public class PerfectSquares {
public static void main(String[] args) {
  PerfectSquares solver = new PerfectSquares();
  int[] inputs = { 12, 13 };
  int[] expected = { 3, 2 };

  for (int i = 0; i < inputs.length; i++) {
    int got = solver.numSquaresDp(inputs[i]);
    System.out.printf("n=%d -> %d  expected=%d%n", inputs[i], got, expected[i]);
  }
}

  /**
 * Intuition: think of target as a shortest coin-change problem where the coins
 * are perfect squares. If square is the last square used for sum i, then the
 * best count is dp[i - square] + 1. Trying every square gives the minimum.
 *
 * Algorithm:
 *   1. Create dp[0..target] and initialize unknown values high.
 *   2. Mark every perfect square as reachable in one step.
 *   3. For each i, try every square j * j <= i.
 *   4. Minimize dp[i] using dp[i - square] + 1, then return dp[target].
 *
 * Time:  O(n * sqrt(n)) - each value tries all squares up to its square root.
 * Space: O(n) - dp stores one best count for each value up to target.
 *
 * @param target number to represent as a sum of perfect squares
 * @return minimum number of perfect squares needed
 */
  public int numSquaresDp(int target) {
    int[] dp = new int[target + 1]; // dp[i] will hold the least number of perfect squares that sum to i
    Arrays.fill(dp, Integer.MAX_VALUE);

    // Perfect square numbers require only 1 step
    for (int i = 1; i * i <= target; i++) {
      dp[i * i] = 1;
    }

    dp[0] = 0; // Base case

    for (int i = 1; i <= target; i++) {
      for (int j = 1; j * j <= i; j++) {
        int square = j * j;

        // Calculate the candidate value: using one more perfect square (square) to
        // reach i
        int candidate = dp[i - square] + 1; // Find the minimum squares for the remainder (i - square) and add 1 for the
                                            // current square

        // Update dp[i] if this candidate uses fewer squares than the current value
        dp[i] = Math.min(dp[i], candidate);
      }
    }

    return dp[target];
  }

  /**
   * Optimized BFS Approach (Shortest Path)
   * Approach:
   * - Use BFS to explore the number of perfect squares that sum up to the target.
   * - Each node in the BFS represents a remainder after subtracting a perfect
   * square.
   * - The level of BFS indicates how many perfect squares have been used.
   * - When we reach a remainder of 0, we return the current level as the answer.
   *
   * Algorithm Steps:
   * 1. Initialize a queue for BFS and a set to track visited remainders.
   * 2. Start from the target number and enqueue it.
   * 3. For each number in the queue, try subtracting all perfect squares less
   * than or equal to it.
   * 4. If subtracting a perfect square results in 0, return the current level
   * (number of squares used).
   *
   * Time Complexity: O(n * √n) - In the worst case, we may explore all numbers up
   * to n, and for each number, we check all perfect squares up to √n.
   * Space Complexity: O(n) - The queue and visited set can hold up to n elements
   * in the worst case.
   */
  public static int numSquaresBFS(int target) {
    Queue<Integer> queue = new LinkedList<>();
    Set<Integer> visited = new HashSet<>();

    queue.offer(target);
    visited.add(target);

    int level = 0; // Number of perfect squares used so far

    while (!queue.isEmpty()) {
      int size = queue.size();
      level++; // Going one level deeper — one more square used

      for (int i = 0; i < size; i++) {
        int current = queue.poll();

        // Try subtracting every perfect square ≤ current
        for (int j = 1; j * j <= current; j++) {
          int next = current - (j * j);

          if (next == 0) {
            return level; // Found combination that sums to n
          }

          if (!visited.contains(next)) {
            visited.add(next);
            queue.offer(next);
          }
        }
      }
    }

    return level;
  }
}
