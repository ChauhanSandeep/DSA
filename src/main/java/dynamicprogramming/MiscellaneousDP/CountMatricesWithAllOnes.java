package dynamicprogramming.MiscellaneousDP;

import java.util.*;


/**
 * Problem: Count Submatrices with All Ones
 *
 * Given a rows x cols binary matrix filled with 0's and 1's, find the number of submatrices that have all ones.
 *
 * Example:
 * Input: matrix = [[1,0,1],
 *                  [1,1,0],
 *                  [1,1,0]]
 * Output: 13
 * Explanation: There are 6 rectangles of side 1x1.
 *              There are 2 rectangles of side 1x2.
 *              There are 3 rectangles of side 2x1.
 *              There are 1 rectangle of side 2x2.
 *              There are 1 rectangle of side 3x1.
 *              Total number of rectangles = 6 + 2 + 3 + 1 + 1 = 13.
 *
 * LeetCode Problem Link: https://leetcode.com/problems/count-submatrices-with-all-ones/
 *
 * Follow-up Questions:
 *
 * 1. How would you modify this to count only square submatrices with all ones?
 *    Answer: Use DP where dp[i][j] represents the side length of the largest square
 *    ending at (i,j). Sum all dp values to get the count of square submatrices.
 *    Related problem: https://leetcode.com/problems/count-square-submatrices-with-all-ones/
 *
 * 2. What if you need to find the largest submatrix area instead of counting all?
 *    Answer: Use similar histogram approach but track maximum area instead of counting.
 *    For each row's histogram, find the largest rectangle area using monotonic stack.
 *    Related problem: https://leetcode.com/problems/maximal-rectangle/
 *
 * 3. How would you handle a 3D matrix instead of 2D?
 *    Answer: Extend to 3D by treating each 2D slice along one dimension. For each slice,
 *    build cumulative counts in the third dimension and apply 2D submatrix counting.
 *
 * 4. Can you optimize space if the matrix is very large but sparse?
 *    Answer: Use sparse matrix representation with coordinates and values. Process only
 *    non-zero regions, skipping areas known to contain zeros.
 *
 * 5. What if you need to count submatrices with at least k ones instead of all ones?
 *    Answer: Use sliding window or prefix sum 2D approach. For each submatrix, calculate
 *    sum using prefix sums and count those with sum >= k.
 */
public class CountMatricesWithAllOnes {

  public static void main(String[] args) {
    int[][] binaryMatrix = {{0, 1, 1, 0}, {0, 1, 1, 1}, {1, 1, 1, 0}};
    CountMatricesWithAllOnes solver = new CountMatricesWithAllOnes();
    System.out.println("Total Submatrices with All Ones: " + solver.numSubmatrix(binaryMatrix));
  }

  /**
   * Optimized space approach using single height array instead of full height matrix.
   *
   * Algorithm Steps:
   * 1. Process matrix row by row
   * 2. Update height array for current row based on previous heights
   * 3. Calculate rectangles for current histogram and add to total
   * 4. Reuse same height array for next iteration
   *
   * Time Complexity: O(rows * cols)
   * Space Complexity: O(cols) - only single height array needed
   *
   * @param matrix Binary matrix containing only 0s and 1s
   * @return Total count of submatrices containing all ones
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
