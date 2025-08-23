package Array;

import java.util.*;

/**
 * Sort The Matrix Diagonally
 * 
 * Problem: Sort matrix elements along each diagonal from top-left to bottom-right.
 * 
 * Example: mat = [[3,3,1,1],[2,2,1,2],[1,1,1,2]] -> [[1,1,1,1],[1,2,2,2],[1,2,3,3]]
 * Each diagonal is sorted independently.
 * 
 * LeetCode: https://leetcode.com/problems/sort-the-matrix-diagonally
 * 
 * Follow-up Questions:
 * - How to sort anti-diagonals instead? (Change coordinate transformation)
 * - What if we want to sort only specific diagonals? (Add diagonal selection logic)
 * - Can we do this in-place with O(1) extra space? (Complex but possible with careful swapping)
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
    public int[][] diagonalSort(int[][] mat) {
        int m = mat.length;
        int n = mat[0].length;

        // Group elements by diagonal (i-j value)
        Map<Integer, List<Integer>> diagonals = new HashMap<>();

        // Collect all elements organized by diagonal
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int diagonal = i - j;
                diagonals.computeIfAbsent(diagonal, k -> new ArrayList<>()).add(mat[i][j]);
            }
        }

        // Sort each diagonal
        for (List<Integer> diagonal : diagonals.values()) {
            Collections.sort(diagonal);
        }

        // Place sorted elements back
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int diagonal = i - j;
                List<Integer> sortedDiagonal = diagonals.get(diagonal);
                mat[i][j] = sortedDiagonal.remove(0); // Remove first element
            }
        }

        return mat;
    }

    /**
     * Alternative approach processing each diagonal individually
     * Time Complexity: O(m*n*log(min(m,n))), Space Complexity: O(min(m,n))
     */
    public int[][] diagonalSortAlternative(int[][] mat) {
        int m = mat.length;
        int n = mat[0].length;

        // Sort diagonals starting from first row
        for (int col = 0; col < n; col++) {
            sortDiagonal(mat, 0, col, m, n);
        }

        // Sort diagonals starting from first column (skip [0,0])
        for (int row = 1; row < m; row++) {
            sortDiagonal(mat, row, 0, m, n);
        }

        return mat;
    }

    // Helper method to sort a specific diagonal
    private void sortDiagonal(int[][] mat, int startRow, int startCol, int m, int n) {
        List<Integer> diagonal = new ArrayList<>();

        // Collect diagonal elements
        int row = startRow, col = startCol;
        while (row < m && col < n) {
            diagonal.add(mat[row][col]);
            row++;
            col++;
        }

        // Sort the diagonal
        Collections.sort(diagonal);

        // Place sorted elements back
        row = startRow;
        col = startCol;
        int index = 0;
        while (row < m && col < n) {
            mat[row][col] = diagonal.get(index++);
            row++;
            col++;
        }
    }

    /**
     * In-place sorting using selection sort for each diagonal
     * Time Complexity: O(m*n*min(m,n)), Space Complexity: O(1)
     */
    public int[][] diagonalSortInPlace(int[][] mat) {
        int m = mat.length;
        int n = mat[0].length;

        // Sort each diagonal starting from first row
        for (int col = 0; col < n; col++) {
            selectionSortDiagonal(mat, 0, col, m, n);
        }

        // Sort diagonals starting from first column (excluding [0,0])
        for (int row = 1; row < m; row++) {
            selectionSortDiagonal(mat, row, 0, m, n);
        }

        return mat;
    }

    // Helper method for in-place diagonal sorting
    private void selectionSortDiagonal(int[][] mat, int startRow, int startCol, int m, int n) {
        List<int[]> positions = new ArrayList<>();

        // Collect all positions on this diagonal
        int row = startRow, col = startCol;
        while (row < m && col < n) {
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
                if (mat[posJ[0]][posJ[1]] < mat[posI[0]][posI[1]]) {
                    minIdx = j;
                }
            }

            // Swap elements
            if (minIdx != i) {
                int[] pos1 = positions.get(i);
                int[] pos2 = positions.get(minIdx);
                int temp = mat[pos1[0]][pos1[1]];
                mat[pos1[0]][pos1[1]] = mat[pos2[0]][pos2[1]];
                mat[pos2[0]][pos2[1]] = temp;
            }
        }
    }

    /**
     * Using priority queue for each diagonal
     * Time Complexity: O(m*n*log(min(m,n))), Space Complexity: O(m*n)
     */
    public int[][] diagonalSortPriorityQueue(int[][] mat) {
        int m = mat.length;
        int n = mat[0].length;

        Map<Integer, PriorityQueue<Integer>> diagonals = new HashMap<>();

        // Collect elements in min heaps
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int diagonal = i - j;
                diagonals.computeIfAbsent(diagonal, k -> new PriorityQueue<>()).offer(mat[i][j]);
            }
        }

        // Extract sorted elements
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int diagonal = i - j;
                mat[i][j] = diagonals.get(diagonal).poll();
            }
        }

        return mat;
    }

    /**
     * Sorting anti-diagonals instead of main diagonals
     * Time Complexity: O(m*n*log(min(m,n))), Space Complexity: O(m*n)
     */
    public int[][] sortAntiDiagonally(int[][] mat) {
        int m = mat.length;
        int n = mat[0].length;

        Map<Integer, List<Integer>> diagonals = new HashMap<>();

        // For anti-diagonals, use i+j as key
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int diagonal = i + j;
                diagonals.computeIfAbsent(diagonal, k -> new ArrayList<>()).add(mat[i][j]);
            }
        }

        // Sort each anti-diagonal
        for (List<Integer> diagonal : diagonals.values()) {
            Collections.sort(diagonal);
        }

        // Place sorted elements back
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int diagonal = i + j;
                List<Integer> sortedDiagonal = diagonals.get(diagonal);
                mat[i][j] = sortedDiagonal.remove(0);
            }
        }

        return mat;
    }

    /**
     * Helper method to print matrix (for debugging)
     */
    private void printMatrix(int[][] mat, String title) {
        System.out.println(title + ":");
        for (int[] row : mat) {
            System.out.println(Arrays.toString(row));
        }
        System.out.println();
    }

    /**
     * Helper method to create a copy of matrix
     */
    private int[][] copyMatrix(int[][] mat) {
        int m = mat.length;
        int n = mat[0].length;
        int[][] copy = new int[m][n];
        for (int i = 0; i < m; i++) {
            System.arraycopy(mat[i], 0, copy[i], 0, n);
        }
        return copy;
    }
}
