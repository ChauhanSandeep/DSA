package dynamicprogramming.gridpath;

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
 * 1. How would you find the actual path, not just the minimum sum?
 *    Answer: Store parent pointers or indices during DP calculation. After computing minimum,
 *    backtrack from bottom to top using stored indices to reconstruct the path.
 *
 * 2. What if you can move to any position in the next row, not just adjacent?
 *    Answer: For each position, consider all positions in next row instead of just two.
 *    Time complexity increases to O(n^3) as we check n positions for each of n^2 cells.
 *
 * 3. How would you solve if you need maximum path sum instead of minimum?
 *    Answer: Change min() to max() in the recurrence relation. The algorithm structure
 *    remains identical, only the comparison operator changes.
 *    Related problem: https://leetcode.com/problems/maximum-path-sum-in-triangle/
 *
 * 4. What if you can move up as well as down (bidirectional)?
 *    Answer: This becomes a graph problem requiring BFS/Dijkstra since you can revisit
 *    positions. Need to track visited states and find shortest path from top to any bottom position.
 *
 * 5. How would you handle very large triangles that don't fit in memory?
 *    Answer: Process row by row from bottom to top, keeping only current and next row in memory.
 *    This is already achieved by the O(n) space solution.
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class MinimumPathSumTriangle {

  public static void main(String[] args) {
    List<List<Integer>> triangle =
        Arrays.asList(Arrays.asList(2), Arrays.asList(3, 4), Arrays.asList(6, 5, 7), Arrays.asList(4, 1, 8, 3));
    System.out.println("Optimized DP: " + minimumTotal(triangle));   // 11
    System.out.println("Memoized Recursion: " + minimumTotalRecursive(triangle)); // 11
  }

  /**
   * Approach 1: Bottom-Up Dynamic Programming (In-Place Modification)
   *
   * Steps:
   * 1. Start from the second-to-last row and move upwards to the top.
   * 2. For each element, add the minimum of the two adjacent elements from the row below.
   * 3. By the time we reach the top, the top element contains the minimum path sum.
   * 
   * Time Complexity: O(N^2) where N is the number of rows. Each element is processed once.
   * Space Complexity: O(1) additional space since we modify the triangle in place.
   * 
   * @param triangle list of lists representing the triangle
   * @return minimum path sum from top to bottom
   */
  public static int minimumTotal(List<List<Integer>> triangle) {
        for (int row = triangle.size() - 2; row >= 0; row--) {
            for (int col = 0; col <= row; col++) {
                int bestBelow = Math.min(
                    triangle.get(row + 1).get(col),
                    triangle.get(row + 1).get(col + 1)
                );
                triangle
                    .get(row)
                    .set(col, bestBelow + triangle.get(row).get(col));
            }
        }
        return triangle.get(0).get(0);
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
    int length = triangle.size();
    Integer[][] memo = new Integer[length][length];
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
