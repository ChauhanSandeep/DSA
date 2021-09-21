package Heap;

import java.util.PriorityQueue;

/**
 * Given an n x n matrix where each of the rows and columns are sorted in ascending order,
 * return the kth smallest element in the matrix.
 */
public class SortedMatrix {

    public static void main(String[] args) {
        int[][] matrix = {
                {1,5,9},
                {10,11,13},
                {12,13,15}
        };
        int result = new SortedMatrix().kthSmallest(matrix, 8);
        System.out.println(result);

    }

    /**
     * This is same as returning kth smallest element in n lists
     */
    public int kthSmallest(int[][] matrix, int k) {
        PriorityQueue<Node> maxHeap = new PriorityQueue<>((a, b) -> a.val - b.val);
        int count = 1;

        for(int i=0; i<matrix.length; i++) {
            maxHeap.offer(new Node(i, 0, matrix[i][0]));
        }

        while(count < k) {
            Node node = maxHeap.poll();
            if( node.col + 1 < matrix[0].length) {
                maxHeap.offer(new Node(node.row, node.col + 1, matrix[node.row][node.col + 1]));
            }
            count++;
        }
        return maxHeap.poll().val;
    }
}

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
