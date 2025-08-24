package dynamicprogramming;

import java.util.Arrays;
import java.util.Stack;


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
 * 1. Q: What if we need to count submatrices with at most K zeros?
 *    A: Use sliding window technique with two pointers for each row in height matrix
 * 2. Q: How to handle very large matrices that don't fit in memory?
 *    A: Process matrix row by row using streaming approach, maintaining only current height array
 * 3. Q: Count submatrices with exactly K ones?
 *    A: Use prefix sum with hashmap to track subarray sums (https://leetcode.com/problems/subarray-sum-equals-k/)
 * 4. Q: What about counting square submatrices with all ones?
 *    A: Use DP where dp[i][j] represents side length of largest square ending at (i,j)
 *       (https://leetcode.com/problems/count-square-submatrices-with-all-ones/)
 */
public class CountMatricesWithAllOnes {

  public static void main(String[] args) {
    int[][] binaryMatrix = {{0, 1, 1, 0}, {0, 1, 1, 1}, {1, 1, 1, 0}};
    CountMatricesWithAllOnes solver = new CountMatricesWithAllOnes();
    System.out.println("Total Submatrices with All Ones: " + solver.numSubmat(binaryMatrix));
  }

  /**
   * Counts the number of submatrices that contain only ones using histogram approach.
   *
   * Algorithm Steps:
   * 1. Build height matrix where each cell represents consecutive 1s above current position
   * 2. For each row, treat heights as histogram and count valid rectangles
   * 3. Use monotonic stack to efficiently compute rectangles for each position
   *
   * Time Complexity: O(rows * cols) - each cell processed once for height matrix and once for stack
   * Space Complexity: O(rows * cols) for height matrix + O(cols) for stack
   *
   * @param binaryMatrix Binary matrix containing only 0s and 1s
   * @return Total count of submatrices containing all ones
   */
  public int numSubmat(int[][] binaryMatrix) {
    if (binaryMatrix == null || binaryMatrix.length == 0 || binaryMatrix[0].length == 0) {
      return 0;
    }

    int numRows = binaryMatrix.length;
    int numCols = binaryMatrix[0].length;

    // Build height matrix representing consecutive 1s in each column
    int[][] consecutiveOnesHeight = buildHeightMatrix(binaryMatrix, numRows, numCols);

    int totalSubmatricesCount = 0;

    // Process each row as histogram to count valid rectangles
    for (int rowIndex = 0; rowIndex < numRows; rowIndex++) {
      totalSubmatricesCount += countRectanglesInHistogram(consecutiveOnesHeight[rowIndex]);
    }

    return totalSubmatricesCount;
  }

  /**
   * Builds height matrix where each cell represents number of consecutive 1s above current position.
   *
   * Algorithm Steps:
   * 1. For each column, iterate through rows
   * 2. If current cell is 0, height is 0
   * 3. If current cell is 1, height is 1 + height of cell above (or 1 if first row)
   *
   * Time Complexity: O(rows * cols)
   * Space Complexity: O(rows * cols)
   *
   * @param binaryMatrix Input binary matrix
   * @param numRows Number of rows in matrix
   * @param numCols Number of columns in matrix
   * @return Height matrix with consecutive 1s count
   */
  private int[][] buildHeightMatrix(int[][] binaryMatrix, int numRows, int numCols) {
    int[][] consecutiveOnesHeight = new int[numRows][numCols];

    for (int colIndex = 0; colIndex < numCols; colIndex++) {
      for (int rowIndex = 0; rowIndex < numRows; rowIndex++) {
        if (binaryMatrix[rowIndex][colIndex] == 0) {
          consecutiveOnesHeight[rowIndex][colIndex] = 0;
        } else {
          consecutiveOnesHeight[rowIndex][colIndex] =
              (rowIndex == 0) ? 1 : consecutiveOnesHeight[rowIndex - 1][colIndex] + 1;
        }
      }
    }

    return consecutiveOnesHeight;
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
   * @param histogramHeights Array representing heights in histogram
   * @return Total count of rectangles in the histogram
   */
  private int countRectanglesInHistogram(int[] histogramHeights) {
    int histogramLength = histogramHeights.length;
    int[] rectangleCountAtPosition = new int[histogramLength];

    // Stack to maintain indices in increasing order of heights
    Stack<Integer> monotonicStack = new Stack<>();
    monotonicStack.push(-1); // Sentinel to handle edge cases

    for (int currentIndex = 0; currentIndex < histogramLength; currentIndex++) {
      // Pop elements until stack top has smaller height than current
      while (monotonicStack.peek() != -1 && histogramHeights[monotonicStack.peek()] >= histogramHeights[currentIndex]) {
        monotonicStack.pop();
      }

      int previousSmallerIndex = monotonicStack.peek();

      // Calculate rectangles ending at current position
      int widthFromPreviousSmaller = currentIndex - previousSmallerIndex;
      rectangleCountAtPosition[currentIndex] = histogramHeights[currentIndex] * widthFromPreviousSmaller;

      // Add rectangles from previous position if it exists
      if (previousSmallerIndex != -1) {
        rectangleCountAtPosition[currentIndex] += rectangleCountAtPosition[previousSmallerIndex];
      }

      monotonicStack.push(currentIndex);
    }

    return Arrays.stream(rectangleCountAtPosition).sum();
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
   * @param binaryMatrix Binary matrix containing only 0s and 1s
   * @return Total count of submatrices containing all ones
   */
  public int numSubmatOptimized(int[][] binaryMatrix) {
    if (binaryMatrix == null || binaryMatrix.length == 0 || binaryMatrix[0].length == 0) {
      return 0;
    }

    int numRows = binaryMatrix.length;
    int numCols = binaryMatrix[0].length;
    int[] currentRowHeights = new int[numCols];
    int totalSubmatricesCount = 0;

    for (int rowIndex = 0; rowIndex < numRows; rowIndex++) {
      // Update heights for current row
      for (int colIndex = 0; colIndex < numCols; colIndex++) {
        currentRowHeights[colIndex] = (binaryMatrix[rowIndex][colIndex] == 0) ? 0 : currentRowHeights[colIndex] + 1;
      }

      // Count rectangles in current histogram
      totalSubmatricesCount += countRectanglesInHistogram(currentRowHeights);
    }

    return totalSubmatricesCount;
  }
}
