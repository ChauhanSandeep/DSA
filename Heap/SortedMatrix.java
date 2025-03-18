package Heap;

import java.util.PriorityQueue;

/**
 * LeetCode: https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/
 *
 * Given an n x n matrix where each row and column is sorted in ascending order,
 * return the k-th smallest element in the matrix.
 *
 * Approach 1: Min-Heap (Priority Queue)
 * - Use a Min-Heap to extract the smallest element k times.
 * - Insert the first element of each row into the heap.
 * - Continue extracting and inserting until the k-th smallest element is found.
 *
 * Time Complexity: O(k log n) → Extracting from heap k times
 * Space Complexity: O(n) → Storing at most n elements in the heap
 *
 * Approach 2: Binary Search (Optimized)
 * - Search for the k-th smallest number within the matrix range.
 * - Use binary search on values instead of indices.
 * - Count elements ≤ mid and adjust the search range.
 *
 * Time Complexity: O(n log(max-min)) → Faster for large matrices
 * Space Complexity: O(1) → No extra space used
 */
public class SortedMatrix {

    public static void main(String[] args) {
        int[][] matrix = {
                {1, 5, 9},
                {10, 11, 13},
                {12, 13, 15}
        };
        int k = 8;

        System.out.println("Kth Smallest (Min-Heap): " + kthSmallestHeap(matrix, k)); // Expected: 13
        System.out.println("Kth Smallest (Binary Search): " + kthSmallestBinarySearch(matrix, k)); // Expected: 13
    }

    /**
     * Finds the k-th smallest element using a Min-Heap (Priority Queue).
     * @param matrix Sorted matrix (n x n).
     * @param k The k-th smallest element to find.
     * @return The k-th smallest element.
     */
    public static int kthSmallestHeap(int[][] matrix, int k) {
        int n = matrix.length;
        PriorityQueue<MatrixNode> minHeap = new PriorityQueue<>((a, b) -> Integer.compare(a.value, b.value));

        // Insert the first element of each row into the minHeap
        for (int row = 0; row < n; row++) {
            minHeap.offer(new MatrixNode(row, 0, matrix[row][0]));
        }

        // Extract the minimum element k times
        for (int count = 1; count < k; count++) {
            MatrixNode node = minHeap.poll();

            if (node.col + 1 < n) { // If next element exists in the row, insert it
                minHeap.offer(new MatrixNode(node.row, node.col + 1, matrix[node.row][node.col + 1]));
            }
        }

        return minHeap.poll().value;
    }

    /**
     * Optimized Binary Search approach to find k-th smallest element.
     * @param matrix Sorted matrix (n x n).
     * @param k The k-th smallest element to find.
     * @return The k-th smallest element.
     */
    public static int kthSmallestBinarySearch(int[][] matrix, int k) {
        int n = matrix.length;
        int left = matrix[0][0], right = matrix[n - 1][n - 1];

        while (left < right) {
            int mid = left + (right - left) / 2;
            int count = countLessOrEqual(matrix, mid);

            if (count < k) {
                left = mid + 1; // Need to search for larger values
            } else {
                right = mid; // Potential answer, keep searching for a smaller one
            }
        }

        return left;
    }

    /**
     * Counts how many numbers are ≤ target in the sorted matrix.
     * @param matrix Sorted matrix (n x n).
     * @param target The value to compare against.
     * @return The count of elements ≤ target.
     */
    private static int countLessOrEqual(int[][] matrix, int target) {
        int n = matrix.length;
        int row = n - 1, col = 0, count = 0;

        while (row >= 0 && col < n) {
            if (matrix[row][col] <= target) {
                count += (row + 1); // All numbers above this row in current column are smaller
                col++; // Move right
            } else {
                row--; // Move up
            }
        }

        return count;
    }
}

/**
 * Helper class to store matrix elements in the min-heap.
 */
class MatrixNode {
    int row;
    int col;
    int value;

    public MatrixNode(int row, int col, int value) {
        this.row = row;
        this.col = col;
        this.value = value;
    }
}
