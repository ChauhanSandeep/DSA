package arrays.binarysearch;

import java.util.Arrays;

/**
 * Problem: Find a Peak Element II
 *
 * Given a matrix with no equal adjacent cells, return any cell greater than its up, down, left, and right neighbors. Boundary cells compare only with existing neighbors.
 *
 * Leetcode: https://leetcode.com/problems/find-a-peak-element-ii/ (Medium)
 * Rating:   acceptance 55.3% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Binary search on columns | Column maximum | Move toward larger neighbor
 *
 * Example:
 *   Input:  mat = [[1,4],[3,2]]
 *   Output: [0,1]
 *   Why:    4 is greater than its left and lower neighbors.
 *
 * Follow-ups:
 *   1. Search rows instead? Symmetrically find the max column in a middle row.
 *   2. Return all peaks? Traverse every cell.
 *   3. Allow equal adjacent values? Compress plateaus or change strictness.
 *   4. Extend to 3D? Search a middle plane and move toward a larger neighbor plane.
 *
 * Related: Find Peak Element (162).
 */
public class FindPeakElementII {

  public static void main(String[] args) {
    FindPeakElementII solver = new FindPeakElementII();
    int[][][] inputs = { {{1, 4}, {3, 2}}, {{10}} };
    int[][] expected = { {1, 0}, {0, 0} };
    for (int i = 0; i < inputs.length; i++) {
      int[] got = solver.locatePeakElement(inputs[i]);
      System.out.printf("mat=%s -> %s  expected=%s%n", Arrays.deepToString(inputs[i]), Arrays.toString(got), Arrays.toString(expected[i]));
    }
  }


    /**
   * Intuition: The maximum in a column already beats vertical neighbors in that column. If it loses horizontally, a peak is guaranteed toward the larger horizontal neighbor.
   *
   * Algorithm:
   *   1. Binary search columns with inclusive bounds.
   *   2. Find the row of the maximum value in midCol.
   *   3. Return it if it beats left and right neighbors.
   *   4. Otherwise move toward the larger neighbor side.
   *
   * Time:  O(m log n) - each probe scans m rows and halves columns.
   * Space: O(1) - only indexes are stored.
   *
   * @param mat matrix with no equal adjacent cells
   * @return coordinates [row, col] of a peak, or [-1, -1]
   */
  public int[] locatePeakElement(int[][] mat) {
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

  /** Returns the row index of the maximum value in a column. */
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
