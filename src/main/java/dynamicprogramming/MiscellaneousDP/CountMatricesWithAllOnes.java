package dynamicprogramming.MiscellaneousDP;

import java.util.*;


/**
 * Problem: Count Submatrices With All Ones
 *
 * Given a binary matrix, count every rectangular submatrix whose cells are all 1.
 * Rectangles of all sizes count, not just squares.
 *
 * Leetcode: https://leetcode.com/problems/count-submatrices-with-all-ones/
 * Rating:   1845 (zerotrac Elo)
 * Pattern:  Dynamic programming | Histogram DP | Monotonic stack
 *
 * Example:
 *   Input:  matrix = [[1,0,1],[1,1,0],[1,1,0]]
 *   Output: 13
 *   Why:    each row's vertical heights reveal all-one rectangles ending on that
 *           row; summing those row contributions gives 13 total rectangles.
 *
 * Follow-ups:
 *   1. Count only all-one squares?
 *      Use square DP where dp[row][col] is the largest square ending at that cell.
 *   2. Find the largest all-one rectangle instead of counting all?
 *      Reuse row histograms and run largest-rectangle-in-histogram per row.
 *   3. What if the matrix is sparse and huge?
 *      Store runs of ones or non-zero coordinates and avoid scanning known zero regions.
 *
 * Related: Count Square Submatrices With All Ones (1277), Maximal Rectangle (85).
 */
public class CountMatricesWithAllOnes {

    public static void main(String[] args) {
        CountMatricesWithAllOnes solver = new CountMatricesWithAllOnes();
        int[][][] inputs = {
            {{0}},
            {{1, 0, 1}, {1, 1, 0}, {1, 1, 0}},
            {{0, 1, 1, 0}, {0, 1, 1, 1}, {1, 1, 1, 0}}
        };
        int[] expected = {0, 13, 24};

        for (int i = 0; i < inputs.length; i++) {
            int got = solver.numSubmatrix(inputs[i]);
            System.out.printf("matrix=%s -> %d  expected=%d%n",
                Arrays.deepToString(inputs[i]), got, expected[i]);
        }
    }


  /**
     * Intuition: after processing a row, heights[col] tells how many consecutive
     * ones end at that row in each column. Any all-one submatrix ending at the
     * current row corresponds to a contiguous histogram window, with height equal
     * to the minimum bar in that window. The stack helper computes, for every right
     * edge, how many such rectangles end there by reusing the previous smaller bar's
     * count instead of scanning left one column at a time.
     *
     * Algorithm:
     *   1. Maintain heights[col] as consecutive ones ending at the current row.
     *   2. After each row update, treat heights as a histogram.
     *   3. Add the stack helper count for all rectangles whose bottom edge is this row.
     *
     * Time:  O(rows*cols) - each row updates heights and each column enters and leaves the stack once.
     * Space: O(cols) - heights, stack, and per-column counts are one row wide.
     *
     * @param matrix binary matrix
     * @return number of all-one submatrices
     */
  public int numSubmatrix(int[][] matrix) {
    if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
      return 0;
    }

    int rows = matrix.length;
    int cols = matrix[0].length;
    int[] heights = new int[cols]; // Height of 1s for current row
    int totalSubmatricesCount = 0;

    for (int rowIndex = 0; rowIndex < rows; rowIndex++) {
      // Update heights for current row
      for (int colIndex = 0; colIndex < cols; colIndex++) {
        heights[colIndex] = (matrix[rowIndex][colIndex] == 0) ? 0 : heights[colIndex] + 1;
      }

      // Count rectangles in current histogram
      totalSubmatricesCount += countAllRectanglesInHistogram(heights);
    }

    return totalSubmatricesCount;
  }

  /**
   * Counts number of rectangles in histogram using monotonic increasing stack approach.
   *
   * Algorithm Steps:
   * 1. Use monotonic increasing stack to find previous smaller element for each position
   * 2. For each position, calculate number of rectangles ending at that position
   * 3. Formula: height[i] * (i - previousSmallerIndex) + count[previousSmallerIndex]
   * 4. Sum all rectangle counts for the histogram
   *
   * Time Complexity: O(n) where n is length of heights array
   * Space Complexity: O(n) for stack and rectangle count array
   *
   * @param heights Array representing heights in histogram
   * @return Total count of rectangles in the histogram
   */
  public int countAllRectanglesInHistogram(int[] heights) {
    Deque<Integer> monotonicIncreasingStack = new ArrayDeque<>();
    int totalRectangles = 0;
    int[] rectCountAt = new int[heights.length];

    for (int currentIndex = 0; currentIndex < heights.length; currentIndex++) {
      while (!monotonicIncreasingStack.isEmpty() && heights[monotonicIncreasingStack.peek()] >= heights[currentIndex]) {
        monotonicIncreasingStack.pop();
      }

      /**
       * At this point, either the stack is empty (no smaller to left)
       * or the top of the stack is the index of the previous smaller element.
       * Now we can calculate the width and number of rectangles ending at currentIndex.
       */
      int previousSmallerIndex = monotonicIncreasingStack.isEmpty() ? -1 : monotonicIncreasingStack.peek();
      int width = currentIndex - previousSmallerIndex;

      // count rectangles ending at current bar
      rectCountAt[currentIndex] = heights[currentIndex] * width;
      if (previousSmallerIndex != -1) {
        rectCountAt[currentIndex] += rectCountAt[previousSmallerIndex];
      }

      totalRectangles += rectCountAt[currentIndex];
      monotonicIncreasingStack.push(currentIndex);
    }

    return totalRectangles;
  }
}
