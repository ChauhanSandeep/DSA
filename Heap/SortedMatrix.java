package Heap;

import java.util.PriorityQueue;


/**
 * Problem: Kth Smallest Element in a Sorted Matrix
 * LeetCode: https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/
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
 * Follow-up Questions (FAANG-style):
 * 1. Can you optimize it for very large matrix where n is large and k is small?
 *    - Yes, use a min-heap and only track minimal number of elements (heap size limited to number of rows).
 * 2. What if matrix is not square?
 *    - Modify heap logic accordingly to support m x n.
 * 3. Can you solve it using constant space?
 *    - Yes, Binary Search on value range uses O(1) extra space.
 * 4. Can you find the kth largest element instead of smallest?
 *    - Yes, adapt the heap or invert the matrix index logic.
 *    - Related: https://leetcode.com/problems/kth-largest-element-in-an-array/
 */
public class SortedMatrix {

  public static void main(String[] args) {
    int[][] matrix = {{1, 5, 9}, {10, 11, 13}, {12, 13, 15}};
    int k = 8;

    System.out.println("Kth Smallest (Min-Heap): " + findKthSmallestUsingMinHeap(matrix, k)); // Expected: 13
    System.out.println("Kth Smallest (Binary Search): " + findKthSmallestUsingBinarySearch(matrix, k)); // Expected: 13
  }

  /**
   * Finds the k-th smallest element in a sorted matrix using a Min-Heap.
   *
   * Steps:
   * 1. Insert the first element of each row into a min-heap.
   * 2. Repeatedly extract the smallest element from heap.
   * 3. For each extracted element, insert the next element from the same row (if exists).
   * 4. After k extractions, return the last extracted element.
   *
   * Time Complexity: O(k * log n), where n is the number of rows
   * Space Complexity: O(n) for the heap
   */
  public static int findKthSmallestUsingMinHeap(int[][] matrix, int k) {
    int numRows = matrix.length;
    PriorityQueue<HeapNode> minHeap = new PriorityQueue<>((a, b) -> Integer.compare(a.value, b.value));

    // Insert the first element of each row
    for (int row = 0; row < numRows; row++) {
      minHeap.offer(new HeapNode(row, 0, matrix[row][0]));
    }

    int extractedValue = -1;
    for (int count = 0; count < k; count++) {
      HeapNode current = minHeap.poll();
      extractedValue = current.value;

      // Insert the next element in the same row if it exists
      if (current.colIndex + 1 < matrix[0].length) {
        int nextCol = current.colIndex + 1;
        minHeap.offer(new HeapNode(current.rowIndex, nextCol, matrix[current.rowIndex][nextCol]));
      }
    }

    return extractedValue;
  }

  /**
   * Finds the k-th smallest element using a Binary Search on value range.
   *
   * Steps:
   * 1. Set low = smallest element, high = largest element.
   * 2. While low < high:
   *    - mid = (low + high) / 2
   *    - Count number of elements ≤ mid
   *    - If count < k, move low to mid+1
   *    - Else move high to mid
   * 3. When low == high, it's the k-th smallest element.
   *
   * Time Complexity: O(n * log(max - min))
   * Space Complexity: O(1)
   */
  public static int findKthSmallestUsingBinarySearch(int[][] matrix, int k) {
    int length = matrix.length;
    int low = matrix[0][0];
    int high = matrix[length - 1][length - 1];

    while (low < high) {
      int mid = low + (high - low) / 2;
      // Count how many elements are less than or equal to mid
      int count = countLessThanOrEqual(matrix, mid);

      if (count < k) {
        low = mid + 1; // Move right to find larger values
      } else {
        high = mid; // Possible candidate, look for smaller
      }
    }

    return low;
  }

  /**
   * Helper method to count how many elements in matrix are ≤ target.
   *
   * Time: O(n)
   * Space: O(1)
   */
  private static int countLessThanOrEqual(int[][] matrix, int target) {
    int count = 0;
    int length = matrix.length;
    int row = length - 1; // Start from bottom-left
    int col = 0;

    while (row >= 0 && col < length) {
      if (matrix[row][col] <= target) {
        count += (row + 1); // All elements above current row are also <= target
        col++; // Move to next column
      } else {
        row--; // Move up
      }
    }

    return count;
  }
}

/**
 * Helper class representing a cell in the matrix.
 * Used for maintaining row and column information along with the value.
 */
class HeapNode {
  int rowIndex;
  int colIndex;
  int value;

  public HeapNode(int rowIndex, int colIndex, int value) {
    this.rowIndex = rowIndex;
    this.colIndex = colIndex;
    this.value = value;
  }
}
