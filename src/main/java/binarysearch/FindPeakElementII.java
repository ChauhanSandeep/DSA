package binarysearch;

/**
 * Find a Peak Element II
 *
 * Problem Statement:
 * Given a 0-indexed m x n matrix mat where no two adjacent cells are equal,
 * find any peak element mat[i][j] and return the length 2 array [i,j].
 * A peak element is an element that is strictly greater than all of its
 * adjacent neighbors (left, right, top, bottom). Elements on the boundary
 * are compared only with their existing neighbors.
 *
 * Example:
 * Input: mat = [[1,4],[3,2]]
 * Output: [0,1]
 * Explanation: Element at position [0,1] is 4, which is greater than its
 * neighbors 1 and 2, making it a peak element.
 *
 * LeetCode: https://leetcode.com/problems/find-a-peak-element-ii/
 *
 * Follow-up Questions that FAANG might ask:
 * 1. Can you optimize for better time complexity?
 *    Answer: The binary search approach achieves O(m log n) or O(n log m),
 *    which is optimal for this problem.
 *
 * 2. What if we need to find all peak elements?
 *    Answer: Would require O(mn) traversal as binary search optimization
 *    wouldn't apply when finding all peaks.
 *
 * 3. How would you handle a 3D matrix?
 *    Answer: Similar binary search approach but with more complex neighbor
 *    comparisons and potentially higher time complexity.
 *
 * 4. What if adjacent elements could be equal?
 *    Answer: Problem becomes more complex as we'd need to handle plateau
 *    regions and modify peak definition.
 *
 * Related Problems:
 * - Find Peak Element (1D version): https://leetcode.com/problems/find-peak-element/
 */
public class FindPeakElementII {

  /**
   * Finds a peak element in a 2D matrix using binary search approach.
   *
   * Algorithm:
   * 1. Apply binary search on rows (or columns)
   * 2. For each middle column, find the row with maximum element
   * 3. Check if this maximum element is greater than elements left and right
   * 4. If yes, it's a peak. If not, move to the half containing larger neighbor
   * 5. Continue until we find a peak
   *
   * Time Complexity: O(m * log n) where m is number of rows and n is number of columns
   * Space Complexity: O(1) - only using constant extra space
   *
   * @param mat the input 2D matrix
   * @return array containing [row, column] coordinates of any peak element
   */
  public int[] findPeakGrid(int[][] mat) {
    int left = 0;
    int right = mat[0].length - 1;

    while (left <= right) {
      int midCol = left + (right - left) / 2;
      int maxRowIndex = findMaxRowInColumn(mat, midCol);

      // Check if current element is a peak horizontally
      boolean isGreaterThanLeft = (midCol == 0) || (mat[maxRowIndex][midCol] > mat[maxRowIndex][midCol - 1]);
      boolean isGreaterThanRight = (midCol == mat[0].length - 1) || (mat[maxRowIndex][midCol] > mat[maxRowIndex][midCol + 1]);

      if (isGreaterThanLeft && isGreaterThanRight) {
        return new int[]{maxRowIndex, midCol};
      }

      // Move towards the direction of larger neighbor
      if (!isGreaterThanLeft) {
        // Peak must be in left half
        right = midCol - 1;
      } else {
        // Peak must be in right half
        left = midCol + 1;
      }
    }

    return new int[]{-1, -1};
  }

  // Helper method to find row index of maximum element in a column
  private int findMaxRowInColumn(int[][] mat, int col) {
    int maxIndex = 0;
    for (int i = 1; i < mat.length; i++) {
      if (mat[i][col] > mat[maxIndex][col]) {
        maxIndex = i;
      }
    }
    return maxIndex;
  }
}
