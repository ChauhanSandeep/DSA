package arrays.sorting;

import java.util.*;

/**
 * Sort The Matrix Diagonally
 *
 * A matrix diagonal is a diagonal line of cells starting from some cell in either
 * the topmost row or leftmost column and going in the bottom-right direction until
 * reaching the matrix's end. 
 * Given an m x n matrix mat of integers, sort each
 * matrix diagonal in ascending order and return the resulting matrix.
 *
 * Example: mat = [
 *      [3,3,1,1],
 *      [2,2,1,2],
 *      [1,1,1,2]
 *  ] ->
 *  [
 *      [1,1,1,1],
 *      [1,2,2,2],
 *      [1,2,3,3]
 *  ]
 * Each diagonal is sorted independently.
 *
 * LeetCode Link: https://leetcode.com/problems/sort-the-matrix-diagonally/
 *
 * Follow-up Questions:
 * 1. What if we need to sort anti-diagonals (top-right to bottom-left) instead?
 *    Answer: Use (i + j) as the diagonal identifier instead of (i - j).
 * 2. How would you sort diagonals in different orders (some ascending, some descending)?
 *    Answer: Add a parameter or pattern to determine sort order for each diagonal.
 * 3. What if we need to sort only specific diagonals based on certain criteria?
 *    Answer: Add filtering logic before sorting to identify which diagonals to process.
 * 4. How to handle very large matrices that don't fit in memory?
 *    Answer: Process diagonals one at a time using streaming/chunking approaches.
 *
 * Related Problems:
 * - 498. Diagonal Traverse: https://leetcode.com/problems/diagonal-traverse/
 * - 1572. Matrix Diagonal Sum: https://leetcode.com/problems/matrix-diagonal-sum/
 * - 766. Toeplitz Matrix: https://leetcode.com/problems/toeplitz-matrix/
 * LeetCode Contest Rating: 1548
 */
public class SortTheMatrixDiagonally {

    /**
     * Sorts matrix diagonally using hash map to group diagonal elements.
     *
     * Algorithm:
     * 1. Group elements by diagonal using coordinate difference (i-j)
     * 2. Elements on same diagonal have same i-j value
     * 3. Sort each diagonal group independently
     * 4. Place sorted elements back in matrix
     *
     * Time Complexity: O(m*n*log(min(m,n))) where m,n are matrix dimensions
     * Space Complexity: O(m*n) for storing diagonal elements
     *
     * @param mat input matrix to sort diagonally
     * @return matrix with sorted diagonals
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
