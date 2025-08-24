package heap;

import java.util.PriorityQueue;


/**
 * Problem: Kth Smallest Element in a Sorted Matrix
 *
 * Given an n x n matrix where each row and each column is sorted in ascending order,
 * return the k-th smallest element in the matrix.
 *
 * Example:
 * Input:
 * matrix = [
 *     [1, 5, 9],
 *     [10, 11, 13],
 *     [12, 13, 15]
 * ]
 * k = 8
 * Output: 13
 *
 * LeetCode: https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/
 *
 * Follow-up Questions (FAANG-style):
 * 1. Can you optimize it for very large matrix where n is large and k is small?
 *    - Yes, use a min-heap and only track minimal number of elements (heap size limited to number of rows).
 * 2. What if matrix is not square (m x n)?
 *    - Modify heap logic accordingly to support rectangular matrices.
 * 3. Can you solve it using constant space?
 *    - Yes, Binary Search on value range uses O(1) extra space.
 * 4. Can you find the kth largest element instead of smallest?
 *    - Yes, adapt the heap or invert the matrix index logic.
 *    - Related: https://leetcode.com/problems/kth-largest-element-in-an-array/
 * 5. What if we need to find kth smallest in multiple queries?
 *    - Preprocess matrix into a sorted array or use segment tree for range queries.
 * 6. How would you handle updates to matrix elements?
 *    - Rebuild heap or use balanced BST with lazy propagation for dynamic updates.
 */
public class KthSmallestInSortedMatrix {

  public static void main(String[] args) {
    int[][] matrix = {{1, 5, 9}, {10, 11, 13}, {12, 13, 15}};
    int k = 8;

    System.out.println("Kth Smallest (Min-Heap): " + findKthSmallestUsingMinHeap(matrix, k)); // Expected: 13
    System.out.println("Kth Smallest (Binary Search): " + findKthSmallestUsingBinarySearch(matrix, k)); // Expected: 13
    System.out.println("Kth Smallest (Optimized Heap): " + findKthSmallestOptimizedHeap(matrix, k)); // Expected: 13
  }

  /**
   * Finds the k-th smallest element in a sorted matrix using a Min-Heap approach.
   *
   * Algorithm: Priority Queue (Min-Heap)
   * Steps:
   * 1. Initialize min-heap with first element of each row
   * 2. Extract minimum element k times from heap
   * 3. For each extracted element, add next element from same row (if exists)
   * 4. Return the k-th extracted element
   *
   * Time Complexity: O(k * log(min(n, k)))
   * Space Complexity: O(min(n, k)) for the heap
   *
   * @param matrix sorted n x n matrix
   * @param k the position of smallest element to find
   * @return k-th smallest element
   */
  public static int findKthSmallestUsingMinHeap(int[][] matrix, int k) {
    if (matrix == null || matrix.length == 0 || k <= 0) {
      throw new IllegalArgumentException("Invalid input parameters");
    }

    int numRows = matrix.length;
    int numCols = matrix[0].length;

    // Min-heap to store matrix elements with their coordinates
    PriorityQueue<MatrixElement> minHeap = new PriorityQueue<>((a, b) -> Integer.compare(a.value, b.value));

    // Initialize heap with first element of each row
    for (int row = 0; row < numRows; row++) {
      minHeap.offer(new MatrixElement(row, 0, matrix[row][0]));
    }

    int kthSmallestElement = -1;

    // Extract k elements from heap
    for (int extractionCount = 0; extractionCount < k; extractionCount++) {
      MatrixElement currentElement = minHeap.poll();
      kthSmallestElement = currentElement.value;

      // Add next element from same row if it exists
      if (currentElement.columnIndex + 1 < numCols) {
        int nextColumn = currentElement.columnIndex + 1;
        int nextValue = matrix[currentElement.rowIndex][nextColumn];
        minHeap.offer(new MatrixElement(currentElement.rowIndex, nextColumn, nextValue));
      }
    }

    return kthSmallestElement;
  }

  /**
   * Optimized heap approach that maintains heap size proportional to min(k, n).
   *
   * Algorithm: Space-Optimized Min-Heap
   * Steps:
   * 1. Use visited array to avoid duplicate processing
   * 2. Start with only matrix[0][0] in heap
   * 3. For each extraction, add valid neighbors (right and down)
   * 4. Track visited cells to prevent duplicates
   *
   * Time Complexity: O(k * log k)
   * Space Complexity: O(k) for heap and visited set
   *
   * @param matrix sorted n x n matrix
   * @param k the position of smallest element to find
   * @return k-th smallest element
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

  /**
   * Efficiently counts elements in sorted matrix that are less than or equal to target.
   *
   * Algorithm: Two-pointer technique starting from bottom-left
   * Steps:
   * 1. Start from bottom-left corner (row = n-1, col = 0)
   * 2. If current element <= target, all elements above are also <= target
   * 3. Add (row + 1) to count and move right
   * 4. If current element > target, move up
   *
   * Time Complexity: O(n)
   * Space Complexity: O(1)
   *
   * @param matrix sorted n x n matrix
   * @param target value to compare against
   * @return count of elements <= target
   */
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
