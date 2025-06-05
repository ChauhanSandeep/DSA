package Array;

import java.util.*;


/**
 * Given an integer n, return the least number of perfect square numbers that sum to n.
 * Example:
 * Input: n = 13
 * Output: 2 (Explanation: 13 = 4 + 9)
 *
 * LeetCode: https://leetcode.com/problems/perfect-squares/
 *
 */
public class PerfectSquares {
  public static void main(String[] args) {
    PerfectSquares solution = new PerfectSquares();
    System.out.println("DP Solution: " + solution.numSquaresDp(13));
    System.out.println("BFS Solution: " + solution.numSquaresBFS(13));
  }

  /**
   * Dynamic Programming Approach
   *
   * Time Complexity: O(n * sqrt(n))
   * Space Complexity: O(n)
   */
  public int numSquaresDp(int n) {
    int[] dp = new int[n + 1]; // dp[i] will hold the least number of perfect squares that sum to i
    Arrays.fill(dp, Integer.MAX_VALUE);

    // Perfect square numbers require only 1 step
    for (int i = 1; i * i <= n; i++) {
      dp[i * i] = 1;
    }

    dp[0] = 0; // Base case

    for (int i = 1; i <= n; i++) {
      for (int j = 1; j * j <= i; j++) {
        // If i - j*j is a perfect square, we can reach i by adding j*j to it
        // Hence, we take the minimum of the current value and the value at dp[i - j*j] + 1
        dp[i] = Math.min(dp[i], dp[i - j * j] + 1);
      }
    }

    return dp[n];
  }

  /**
   * Optimized BFS Approach (Shortest Path)
   * Approach:
   * - Use BFS where each node represents the remaining number after subtracting a perfect square.
   * - Level in BFS represents the number of perfect squares used so far.
   * - Track visited states to prevent cycles.
   *
   * Time Complexity: O(n)
   * Space Complexity: O(n)
   */
  public static int numSquaresBFS(int n) {
    Queue<Integer> queue = new LinkedList<>();
    Set<Integer> visited = new HashSet<>();

    queue.offer(n);
    visited.add(n);

    int level = 0; // Number of perfect squares used so far

    while (!queue.isEmpty()) {
      int size = queue.size();
      level++; // Going one level deeper — one more square used

      for (int i = 0; i < size; i++) {
        int current = queue.poll();

        // Try subtracting every perfect square ≤ current
        for (int square = 1; square * square <= current; square++) {
          int next = current - square * square;

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
