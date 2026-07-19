package arrays.sorting;

import java.util.*;
/**
 * Problem: Sort the Matrix Diagonally
 *
 * Given an m x n matrix, sort every top-left to bottom-right diagonal in ascending
 * order and return the matrix. Each diagonal is independent from every other
 * diagonal.
 *
 * Leetcode: https://leetcode.com/problems/sort-the-matrix-diagonally/ (Medium)
 * Rating:   1548 (Weekly Contest 172)
 * Pattern:  Matrix | Diagonal grouping | Priority queue
 *
 * Example:
 *   Input:  mat = [[3,3,1,1],[2,2,1,2],[1,1,1,2]]
 *   Output: [[1,1,1,1],[1,2,2,2],[1,2,3,3]]
 *   Why:    values are sorted only along cells that share the same row - col diagonal key.
 *
 * Follow-ups:
 *   1. Sort anti-diagonals?
 *      Group by row + col instead of row - col.
 *   2. Sort descending?
 *      Use a max heap or reverse the sorted diagonal values before writing back.
 *   3. Matrix too large for memory?
 *      Stream and sort one diagonal at a time if diagonal access is available.
 *
 * Related: Diagonal Traverse (498), Toeplitz Matrix (766).
 */
public class SortTheMatrixDiagonally {

    public static void main(String[] args) {
        SortTheMatrixDiagonally solver = new SortTheMatrixDiagonally();

        int[][][] inputs = {
            { {3, 3, 1, 1}, {2, 2, 1, 2}, {1, 1, 1, 2} },
            { {11, 25, 66, 1}, {23, 55, 17, 45}, {75, 31, 36, 44} }
        };
        int[][][] expected = {
            { {1, 1, 1, 1}, {1, 2, 2, 2}, {1, 2, 3, 3} },
            { {11, 17, 45, 1}, {23, 36, 25, 66}, {75, 31, 55, 44} }
        };

        for (int i = 0; i < inputs.length; i++) {
            int[][] input = new int[inputs[i].length][];
            for (int row = 0; row < inputs[i].length; row++) {
                input[row] = inputs[i][row].clone();
            }
            int[][] got = solver.diagonalSortPriorityQueue(input);
            System.out.printf("mat=%s -> output=%s  expected=%s%n",
                Arrays.deepToString(inputs[i]), Arrays.deepToString(got), Arrays.deepToString(expected[i]));
        }
    }

/**
 * Intuition: every cell on the same diagonal has the same row - col value. Group
 * those values into min-heaps, then revisit the matrix in row-major order and pop
 * each diagonal heap to write back values in ascending diagonal order.
 *
 * Algorithm:
 *   1. Create a map from diagonal key to a min-heap of values.
 *   2. Visit every cell and offer its value to the heap for i - j.
 *   3. Visit every cell again and poll from its diagonal heap.
 *   4. Return the mutated matrix.
 *
 * Time:  O(m * n * log(min(m, n))) - each cell enters and leaves one diagonal heap.
 * Space: O(m * n) - heaps store all matrix values by diagonal.
 *
 * @param mat matrix whose diagonals are sorted in place
 * @return the same matrix with sorted diagonals
 */
    public int[][] diagonalSortPriorityQueue(int[][] mat) {
        int rows = mat.length;
        int cols = mat[0].length;

        Map<Integer, PriorityQueue<Integer>> diagonalElementsMap = new HashMap<>();

        // Collect elements in min heaps
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int diagonal = i - j;
                diagonalElementsMap.computeIfAbsent(diagonal, k -> new PriorityQueue<>()).offer(mat[i][j]);
            }
        }

        // Extract sorted elements
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int diagonal = i - j;
                mat[i][j] = diagonalElementsMap.get(diagonal).poll();
            }
        }

        return mat;
    }

    /**
     * In-place sorting of each diagonal using selection sort.
     *
     * Intuitive Steps:
     * 1. For each diagonal starting from the first row and first column:
     *    a. Collect all positions (row, col) that belong to the diagonal.
     *    b. For each position in the diagonal, find the minimum element among the remaining positions.
     *    c. Swap the current element with the minimum found, sorting the diagonal in ascending order.
     * 2. Repeat for all diagonals, ensuring each is sorted independently.
     *
     * Time Complexity: O(m*n*min(m, n)), where m and n are matrix dimensions.
     * Space Complexity: O(1), as sorting is done in-place.
     */
    public int[][] diagonalSortInPlace(int[][] mat) {
        int rows = mat.length;
        int cols = mat[0].length;

        // Sort each diagonal starting from first row
        for (int col = 0; col < cols; col++) {
            selectionSortDiagonal(mat, 0, col, rows, cols);
        }

        // Sort diagonals starting from first column (excluding [0,0])
        for (int row = 1; row < rows; row++) {
            selectionSortDiagonal(mat, row, 0, rows, cols);
        }

        return mat;
    }

    /**
     * Steps
     * 1. Collect all positions on the diagonal starting from (startRow, startCol).
     * 2. Use selection sort to sort the elements at these positions.
     * 3. Swap elements in the matrix to sort the diagonal in place.
     */
    private void selectionSortDiagonal(int[][] matrix, int startRow, int startCol, int rows, int cols) {
        List<int[]> positions = new ArrayList<>();

        // Collect all positions on this diagonal
        int row = startRow, col = startCol;
        while (row < rows && col < cols) {
            positions.add(new int[]{row, col});
            row++;
            col++;
        }

        // Sort using selection sort
        for (int i = 0; i < positions.size() - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < positions.size(); j++) {
                int[] posI = positions.get(minIdx);
                int[] posJ = positions.get(j);
                if (matrix[posJ[0]][posJ[1]] < matrix[posI[0]][posI[1]]) {
                    minIdx = j;
                }
            }

            // Swap elements
            if (minIdx != i) {
                int[] pos1 = positions.get(i);
                int[] pos2 = positions.get(minIdx);
                int temp = matrix[pos1[0]][pos1[1]];
                matrix[pos1[0]][pos1[1]] = matrix[pos2[0]][pos2[1]];
                matrix[pos2[0]][pos2[1]] = temp;
            }
        }
    }
}
