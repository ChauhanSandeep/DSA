package arrays;

import java.util.*;

/**
 * Problem: Check if Every Row and Column Contains All Numbers
 *
 * Given an n x n matrix, decide whether every row and every column contains each
 * number from 1 through n exactly once. Any missing value, duplicate value, or
 * value outside the range makes the matrix invalid.
 *
 * Leetcode: https://leetcode.com/problems/check-if-every-row-and-column-contains-all-numbers/ (Easy)
 * Rating:   1264 (zerotrac Elo)
 * Pattern:  Array | Matrix validation | Seen flags
 *
 * Example:
 *   Input:  [[1,2,3],[3,1,2],[2,3,1]]
 *   Output: true
 *   Why:    every row and every column contains exactly the set {1,2,3} once.
 *
 * Follow-ups:
 *   1. Validate diagonals too, like a Sudoku-style variant?
 *      Run the same seen-array check on the two diagonals after rows and columns.
 *   2. Reduce the extra space for n <= 32?
 *      Use an integer bit mask instead of a boolean array.
 *   3. Return the first invalid row or column?
 *      Keep the same checks but return a small result object when a duplicate or range error appears.
 *
 * Related: Valid Sudoku (36), Find Missing and Repeated Values (2965).
 */
public class CheckIfEveryRowAndColumnContainsAllNumbers {

    public static void main(String[] args) {
        CheckIfEveryRowAndColumnContainsAllNumbers solver = new CheckIfEveryRowAndColumnContainsAllNumbers();

        int[][][] inputs = {
            { {1, 2, 3}, {3, 1, 2}, {2, 3, 1} },
            { {1, 1}, {1, 2} }
        };
        boolean[] expected = { true, false };

        for (int i = 0; i < inputs.length; i++) {
            boolean got = solver.checkValidOptimized(inputs[i]);
            System.out.printf("matrix=%s  ->  %s  expected=%s%n",
                Arrays.deepToString(inputs[i]), got, expected[i]);
        }
    }

    /**
     * Intuition: a valid n x n matrix row or column must be a permutation of 1..n. A
     * boolean seen array gives an exact checklist: any value outside the range or any
     * repeated value proves that row or column cannot contain every required number
     * exactly once.
     *
     * Algorithm:
     *   1. For each row, mark values in a length n + 1 seen array and reject invalid or repeated values.
     *   2. For each column, repeat the same seen-array validation.
     *   3. Return true only if every row and column passes.
     *
     * Time:  O(n^2) - every matrix cell is checked once by row and once by column.
     * Space: O(n) - one seen array of size n + 1 is used at a time.
     *
     * @param matrix square matrix to validate
     * @return true when every row and column contains exactly 1 through n
     */
    public boolean checkValidOptimized(int[][] matrix) {
        int length = matrix.length;

        // Check rows
        for (int i = 0; i < length; i++) {
            boolean[] seen = new boolean[length + 1]; // Index 0 unused
            for (int j = 0; j < length; j++) {
                int value = matrix[i][j];
                if (value < 1 || value > length || seen[value]) {
                    return false;
                }
                seen[value] = true;
            }
        }

        // Check columns
        for (int j = 0; j < length; j++) {
            boolean[] seen = new boolean[length + 1]; // Index 0 unused
            for (int i = 0; i < length; i++) {
                int value = matrix[i][j];
                if (value < 1 || value > length || seen[value]) {
                    return false;
                }
                seen[value] = true;
            }
        }

        return true;
    }
}
