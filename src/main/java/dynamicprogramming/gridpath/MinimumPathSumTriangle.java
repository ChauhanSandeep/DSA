package dynamicprogramming.gridpath;

import java.util.List;
import java.util.Arrays;


/**
 * Problem: Triangle
 *
 * Given a triangle of numbers, return the minimum path sum from the top to the
 * bottom. Each move goes to one of the two adjacent positions in the next row.
 *
 * Leetcode: https://leetcode.com/problems/triangle/ (Medium)
 * Rating:   acceptance 59.9% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Dynamic Programming | Bottom-up triangle DP | In-place compression
 *
 * Example:
 *   Input:  triangle = [[2],[3,4],[6,5,7],[4,1,8,3]]
 *   Output: 11
 *   Why:    2 + 3 + 5 + 1 is the cheapest adjacent path from top to bottom.
 *
 * Follow-ups:
 *   1. Return the actual minimum path?
 *      Store the chosen child index at each cell or reconstruct from a separate DP table.
 *   2. Can the input be left unchanged?
 *      Copy the last row into a 1D DP array and update it bottom-up.
 *   3. What if movement can go to any cell in the next row?
 *      Each state needs the minimum over the whole next row, changing the transition.
 *
 * Related: Minimum Path Sum (64), Dungeon Game (174).
 */
public class MinimumPathSumTriangle {

  public static void main(String[] args) {
    List<List<Integer>> triangle1 = Arrays.asList(
        Arrays.asList(2), Arrays.asList(3, 4), Arrays.asList(6, 5, 7), Arrays.asList(4, 1, 8, 3));
    String input1 = triangle1.toString();
    int got1 = minimumTotal(triangle1);
    System.out.printf("triangle=%s -> %d  expected=%d%n", input1, got1, 11);

    List<List<Integer>> triangle2 = Arrays.asList(Arrays.asList(-10));
    String input2 = triangle2.toString();
    int got2 = minimumTotal(triangle2);
    System.out.printf("triangle=%s -> %d  expected=%d%n", input2, got2, -10);
  }


    /**
   * Intuition: after a lower row already stores the cheapest path from each of
   * its positions to the bottom, a cell in the row above needs only its two
   * adjacent children. The cheaper child is the best continuation, so adding the
   * current value turns the current cell into the cheapest bottom-reaching path
   * from that position.
   *
   * Algorithm:
   *   1. Start at the second-to-last row because the last row is already its own cost.
   *   2. For each cell, choose the smaller of the two adjacent values in the row below.
   *   3. Add that best child cost into the current cell and continue upward to the top.
   *
   * Time:  O(n^2) - every triangle element above the last row is updated once.
   * Space: O(1) - the input triangle itself stores the DP values.
   *
   * @param triangle triangle of row lists, modified in place
   * @return minimum top-to-bottom adjacent path sum
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
