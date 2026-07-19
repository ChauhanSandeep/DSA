package heaps;

import java.util.PriorityQueue;
import java.util.Arrays;


/**
 * Problem: Kth Smallest Element in a Sorted Matrix
 *
 * Given an n x n matrix where every row and every column is sorted ascending,
 * return the kth smallest value. The value-range binary search uses the matrix
 * ordering to count how many entries are less than or equal to a guess.
 *
 * Leetcode: https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/ (Medium)
 * Rating:   acceptance 64.8% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Heap | Binary search on answer | Sorted matrix counting
 *
 * Example:
 *   Input:  matrix = [[1,5,9],[10,11,13],[12,13,15]], k = 8
 *   Output: 13
 *   Why:    the sorted order is [1,5,9,10,11,12,13,13,15], so item 8 is 13.
 *
 * Follow-ups:
 *   1. What if k is tiny compared with n*n?
 *      Use the heap method and expand only the smallest frontier cells.
 *   2. What if the matrix is rectangular?
 *      Generalize row and column bounds in both heap expansion and counting.
 *   3. What if many kth queries are asked on the same matrix?
 *      Precompute a sorted flattened array if memory is acceptable.
 *   4. Can you use O(1) extra space?
 *      Use binary search on values with bottom-left counting.
 *
 * Related: Kth Largest Element in an Array (215), Find K Pairs with Smallest Sums (373).
 */

public class KthSmallestInSortedMatrix {

  public static void main(String[] args) {
    int[][][] matrices = { {{1, 5, 9}, {10, 11, 13}, {12, 13, 15}}, {{-5}} };
    int[] kValues = {8, 1};
    int[] expected = {13, -5};

    for (int i = 0; i < matrices.length; i++) {
      int heapGot = findKthSmallestOptimizedHeap(matrices[i], kValues[i]);
      int binaryGot = findKthSmallestUsingBinarySearch(matrices[i], kValues[i]);
      System.out.printf("heap matrix=%s k=%d -> %d  expected=%d%n",
          Arrays.deepToString(matrices[i]), kValues[i], heapGot, expected[i]);
      System.out.printf("binary matrix=%s k=%d -> %d  expected=%d%n",
          Arrays.deepToString(matrices[i]), kValues[i], binaryGot, expected[i]);
    }
  }

    /**
   * Intuition: the matrix is a sorted grid, so the next smallest unseen value is
   * always on the right/down frontier of cells already removed. A min heap orders
   * that frontier, and visited prevents reaching the same cell from two paths.
   *
   * Algorithm:
   *   1. Validate input, then seed the heap with matrix[0][0].
   *   2. Poll the heap k times, remembering the value just removed.
   *   3. After each poll, offer the unvisited right and bottom neighbors.
   *   4. Return the value from the kth extraction.
   *
   * Time:  O(k log k) - each extraction may add frontier cells to the heap.
   * Space: O(k) - the heap and visited frontier grow with processed cells.
   *
   * @param matrix row- and column-sorted matrix
   * @param k one-based rank to find
   * @return kth smallest value in the matrix
   */

  public static int findKthSmallestOptimizedHeap(int[][] matrix, int k) {
    if (matrix == null || matrix.length == 0 || k <= 0) {
      throw new IllegalArgumentException("Invalid input parameters");
    }

    int numRows = matrix.length;
    int numCols = matrix[0].length;

    PriorityQueue<MatrixElement> minHeap = new PriorityQueue<>((a, b) -> Integer.compare(a.value, b.value));
    boolean[][] visited = new boolean[numRows][numCols];

    // Start with top-left element
    minHeap.offer(new MatrixElement(0, 0, matrix[0][0]));
    visited[0][0] = true;

    int kthSmallestElement = -1;

    for (int extractionCount = 0; extractionCount < k; extractionCount++) {
      MatrixElement currentElement = minHeap.poll();
      kthSmallestElement = currentElement.value;

      int currentRow = currentElement.rowIndex;
      int currentCol = currentElement.columnIndex;

      // Add right neighbor
      if (currentCol + 1 < numCols && !visited[currentRow][currentCol + 1]) {
        minHeap.offer(new MatrixElement(currentRow, currentCol + 1, matrix[currentRow][currentCol + 1]));
        visited[currentRow][currentCol + 1] = true;
      }

      // Add bottom neighbor
      if (currentRow + 1 < numRows && !visited[currentRow + 1][currentCol]) {
        minHeap.offer(new MatrixElement(currentRow + 1, currentCol, matrix[currentRow + 1][currentCol]));
        visited[currentRow + 1][currentCol] = true;
      }
    }

    return kthSmallestElement;
  }

  /**
   * Finds the k-th smallest element using Binary Search on value range.
   *
   * Algorithm: Binary Search on Values
   * Steps:
   * 1. Set searchLow = matrix[0][0], searchHigh = matrix[n-1][n-1]
   * 2. While searchLow < searchHigh:
   *    - Calculate midValue = (searchLow + searchHigh) / 2
   *    - Count elements <= midValue using efficient counting
   *    - If count < k, search in higher half
   *    - Else search in lower half
   * 3. Return searchLow when searchLow == searchHigh
   *
   * Time Complexity: O(n * log(max - min))
   * Space Complexity: O(1)
   *
   * @param matrix sorted n x n matrix
   * @param k the position of smallest element to find
   * @return k-th smallest element
   */
  public static int findKthSmallestUsingBinarySearch(int[][] matrix, int k) {
    if (matrix == null || matrix.length == 0 || k <= 0) {
      throw new IllegalArgumentException("Invalid input parameters");
    }

    int matrixSize = matrix.length;
    int searchLow = matrix[0][0];
    int searchHigh = matrix[matrixSize - 1][matrixSize - 1];

    while (searchLow < searchHigh) {
      int midValue = searchLow + (searchHigh - searchLow) / 2;
      int elementsLessOrEqual = countElementsLessOrEqualToTarget(matrix, midValue);

      if (elementsLessOrEqual < k) {
        searchLow = midValue + 1; // Search in higher value range
      } else {
        searchHigh = midValue; // midValue could be answer, search in lower range
      }
    }

    return searchLow;
  }

  /** Counts values less than or equal to target by walking from bottom-left. */
  private static int countElementsLessOrEqualToTarget(int[][] matrix, int target) {
    int count = 0;
    int matrixSize = matrix.length;
    int currentRow = matrixSize - 1; // Start from bottom-left corner
    int currentCol = 0;

    while (currentRow >= 0 && currentCol < matrixSize) {
      if (matrix[currentRow][currentCol] <= target) {
        // All elements above current position in this column are also <= target
        count += (currentRow + 1);
        currentCol++; // Move to next column
      } else {
        currentRow--; // Move up to find smaller elements
      }
    }

    return count;
  }
}

/**
 * Helper class representing a matrix element with its coordinates.
 * Used for maintaining positional information in heap-based solutions.
 */
class MatrixElement {
  int rowIndex;
  int columnIndex;
  int value;

  public MatrixElement(int rowIndex, int columnIndex, int value) {
    this.rowIndex = rowIndex;
    this.columnIndex = columnIndex;
    this.value = value;
  }
}
