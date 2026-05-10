package arrays.twopointers;

import java.util.*;

/**
 * Leetcode Problem 279: Perfect Squares
 * https://leetcode.com/problems/perfect-squares/
 *
 * Problem Statement:
 * Given an integer n, return the least number of perfect square numbers that
 * sum to n.
 * A perfect square is an integer that is the square of an integer; in other
 * words,
 * it is the product of some integer with itself. For example, 1, 4, 9, and 16
 * are perfect squares
 * since they are 1×1, 2×2, 3×3, and 4×4, respectively.
 *
 * Example:
 * Input: n = 12
 * Output: 3
 * Explanation: 12 = 4 + 4 + 4 (three perfect squares: 2², 2², 2²)
 *
 * Input: n = 13
 * Output: 2
 * Explanation: 13 = 4 + 9 (two perfect squares: 2² + 3²)
 *
 * Follow-up Questions (FAANG interview style):
 * 1. What's the theoretical maximum number of perfect squares needed for any
 * number?
 * - By Lagrange's Four-Square Theorem, every positive integer can be
 * represented as the sum of at most four perfect squares.
 * 2. Can you optimize this further using mathematical theorems?
 * - Yes, using Legendre's Three-Square Theorem: a number can be expressed as
 * sum of three squares if and only if it's not of the form 4^a(8b+7).
 * 3. How would you handle very large numbers efficiently?
 * - Mathematical approaches (checking if n is perfect square, sum of two
 * squares, etc.) can be faster than DP for large n.
 * 4. What if we needed to return the actual squares used, not just the count?
 * - We'd need to backtrack through the DP table or use a different approach to
 * reconstruct the solution.
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class PerfectSquares {
  public static void main(String[] args) {
    PerfectSquares solution = new PerfectSquares();
    System.out.println("DP Solution: " + solution.numSquaresDp(13));
    System.out.println("BFS Solution: " + solution.numSquaresBFS(13));
  }

  /**
   * Main solution method - Dynamic Programming (Bottom-up).
   *
   * Algorithm Overview:
   * 1. Create a DP array where dp[i] represents minimum perfect squares needed to
   * sum to i.
   * 2. Initialize dp[0] = 0 (base case: 0 squares needed for sum 0).
   * 3. For each number i from 1 to n, try all perfect squares j² ≤ i.
   * 4. Update dp[i] = min(dp[i], dp[i - j²] + 1) for all valid j.
   * 5. Return dp[n] as the minimum squares needed for n.
   *
   * Time Complexity: O(n * √n) - for each number up to n, we check all perfect
   * squares up to √n
   * Space Complexity: O(n) - DP array of size n+1
   *
   * @param target the target number to represent as sum of perfect squares
   * @return minimum number of perfect squares that sum to n
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
