package dynamicprogramming;

import java.util.List;
import java.util.Arrays;


/**
 * Problem: Minimum Path Sum in a Triangle
 *
 * Given a triangle array, find the minimum path sum from **top to bottom**.
 * Each step, you may move to adjacent numbers in the row below.
 *
 * Example:
 * Input:
 * [
 *      [2],
 *     [3, 4],
 *    [6, 5, 7],
 *   [4, 1, 8, 3]
 * ]
 * Output: 11  (Path: 2 → 3 → 5 → 1)
 *
 * LeetCode Link: https://leetcode.com/problems/triangle/
 *
 * Follow-up Questions for FAANG Interviews:
 * 1. Q: Can the solution be optimized for space?
 *    A: Yes, from O(n²) to O(n) using a single 1D DP array (bottom-up in-place update).
 *
 * 2. Q: Can recursion with memoization (top-down) be applied here?
 *    A: Yes, results in O(n²) time and space, easier to reason recursively.
 *
 * 3. Q: How about returning the actual path, not just sum?
 *    A: Maintain a parent-pointer array or reconstruct path by retracing DP decisions.
 */
public class MinimumPathSumTriangle {

  public static void main(String[] args) {
    List<List<Integer>> triangle =
        Arrays.asList(Arrays.asList(2), Arrays.asList(3, 4), Arrays.asList(6, 5, 7), Arrays.asList(4, 1, 8, 3));
    System.out.println("Optimized DP: " + minimumTotalDP(triangle));   // 11
    System.out.println("Memoized Recursion: " + minimumTotalRecursive(triangle)); // 11
  }

  /**
   * Approach 1: Bottom-Up Dynamic Programming with O(n) space optimization.
   *
   * Steps:
   * 1. Initialize a dp array with the values of the bottom row.
   * 2. Traverse upwards from the second-last row to the top.
   * 3. For each row and col: dp[col] = triangle[row][col] + min(dp[col], dp[col+1])
   * 4. dp[0] will contain the minimum path sum at the top.
   *
   * Time Complexity: O(n²), where n = number of rows in triangle
   * Space Complexity: O(n)
   */
  public static int minimumTotalDP(List<List<Integer>> triangle) {
    int n = triangle.size();
    int[] dp = new int[n];

    // Initialize with the last row
    for (int i = 0; i < n; i++) {
      dp[i] = triangle.get(n - 1).get(i);
    }

    // Bottom-up calculation
    for (int row = n - 2; row >= 0; row--) {
      for (int col = 0; col <= row; col++) {
        dp[col] = triangle.get(row).get(col) + Math.min(dp[col], dp[col + 1]);
      }
    }

    return dp[0];
  }

  /**
   * Approach 2: Recursive Top-Down with Memoization.
   *
   * Steps:
   * 1. Use recursion from the top of the triangle.
   * 2. At each position (row, col), recursively calculate the min path sum:
   *       minPath(row, col) = triangle[row][col] + min(minPath(row+1,col), minPath(row+1,col+1))
   * 3. Memoize results to avoid recomputation.
   *
   * Time Complexity: O(n²)
   * Space Complexity: O(n²) recursion + memo storage
   */
  public static int minimumTotalRecursive(List<List<Integer>> triangle) {
    int n = triangle.size();
    Integer[][] memo = new Integer[n][n];
    return dfs(triangle, 0, 0, memo);
  }

  private static int dfs(List<List<Integer>> triangle, int row, int col, Integer[][] memo) {
    // Base case: last row
    if (row == triangle.size() - 1) {
      return triangle.get(row).get(col);
    }

    if (memo[row][col] != null) {
      return memo[row][col];
    }

    int leftPath = dfs(triangle, row + 1, col, memo);
    int rightPath = dfs(triangle, row + 1, col + 1, memo);

    memo[row][col] = triangle.get(row).get(col) + Math.min(leftPath, rightPath);
    return memo[row][col];
  }
}
