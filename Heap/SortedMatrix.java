package Heap;

import java.util.PriorityQueue;

/**
 * LeetCode: https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/
 *
 * Given an n x n matrix where each of the rows and columns are sorted in ascending order,
 * return the k-th smallest element in the matrix.
 *
 * Algorithm:
 * - Use a min-heap (PriorityQueue) to store elements from the matrix.
 * - Extract the minimum element from the heap k times to get the k-th smallest element.
 *
 * Time Complexity: O(k log n), where n is the row size.
 * Space Complexity: O(n) for storing at most n elements in the heap.
 */
public class SortedMatrix {

    public static void main(String[] args) {
        int[][] matrix = {
                {1, 5, 9},
                {10, 11, 13},
                {12, 13, 15}
        };
        int result = new SortedMatrix().kthSmallest(matrix, 8);
        System.out.println(result); // Expected output: 13
    }

    /**
     * Returns the k-th smallest element in a sorted matrix.
     *
     * @param matrix The n x n sorted matrix.
     * @param k The k-th smallest element to find.
     * @return The k-th smallest element in the matrix.
     */
    public int kthSmallest(int[][] matrix, int k) {
        PriorityQueue<Node> minHeap = new PriorityQueue<>((a, b) -> a.val - b.val);

        // Add the first element of each row into the minHeap
        for (int i = 0; i < matrix.length; i++) {
            minHeap.offer(new Node(i, 0, matrix[i][0]));
        }

        // Extract the minimum element k times
        for (int count = 1; count < k; count++) {
            Node node = minHeap.poll();
            if (node.col + 1 < matrix[0].length) {
                minHeap.offer(new Node(node.row, node.col + 1, matrix[node.row][node.col + 1]));
            }
        }
        return minHeap.poll().val;
    }
}

/**
 * Helper class to store matrix elements in the min-heap.
 */
class Node {
    int row;
    int col;
    int val;

    public Node(int row, int col, int val) {
        this.row = row;
        this.col = col;
        this.val = val;
    }
}