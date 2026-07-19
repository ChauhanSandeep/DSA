package arrays.matrix;

import java.util.Arrays;
/**
 * Problem: Check If Matrix Is X Matrix
 *
 * Given an n x n matrix, verify that the main diagonal and anti-diagonal
 * contain only non-zero values, while every other cell is zero. The scan must
 * reject the first cell that violates either X-matrix rule.
 *
 * Leetcode: https://leetcode.com/problems/check-if-matrix-is-x-matrix/ (Easy)
 * Rating:   1201 (Weekly Contest 299)
 * Pattern:  Matrix traversal | Diagonal checks | Simulation
 *
 * Example:
 *   Input:  matrix = [[2,0,0,1],[0,3,1,0],[0,5,2,0],[4,0,0,2]]
 *   Output: true
 *   Why:    both diagonals are non-zero and every off-diagonal cell is zero.
 *
 * Follow-ups:
 *   1. Check a plus-shaped matrix?
 *      Replace the diagonal predicates with middle-row and middle-column checks.
 *   2. Validate many fixed patterns?
 *      Store the required non-zero coordinates in a boolean mask and scan once.
 *   3. Can this be sublinear?
 *      No for an arbitrary matrix, because any unvisited cell could violate the rule.
 *
 * Related: Matrix Diagonal Sum (1572), Toeplitz Matrix (766).
 */
public class CheckIfMatrixIsXMatrix {

    public static void main(String[] args) {
        CheckIfMatrixIsXMatrix solver = new CheckIfMatrixIsXMatrix();

        int[][][] inputs = {
            { {2, 0, 0, 1}, {0, 3, 1, 0}, {0, 5, 2, 0}, {4, 0, 0, 2} },
            { {5, 7, 0}, {0, 3, 1}, {0, 5, 0} }
        };
        boolean[] expected = { true, false };

        for (int i = 0; i < inputs.length; i++) {
            boolean got = solver.checkXMatrix(inputs[i]);
            System.out.printf("matrix=%s -> output=%s  expected=%s%n",
                Arrays.deepToString(inputs[i]), got, expected[i]);
        }
    }

/**
 * Intuition: each cell has exactly one job. If it lies on either diagonal, it
 * must contribute to the X and therefore cannot be zero. If it lies anywhere
 * else, it would thicken the X and therefore must be zero.
 *
 * Algorithm:
 *   1. Let n be the matrix dimension.
 *   2. Visit every cell and compute whether it is on the main or anti-diagonal.
 *   3. Return false for a zero diagonal cell or a non-zero off-diagonal cell.
 *   4. Return true after all cells satisfy their role.
 *
 * Time:  O(n^2) - every matrix cell is inspected once.
 * Space: O(1) - only diagonal flags are stored.
 *
 * @param matrix square matrix to validate
 * @return true when matrix satisfies the X-matrix rules
 */
    public boolean checkXMatrix(int[][] matrix) {
        int n = matrix.length;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                boolean isDiagonal = i - j == 0;
                boolean isAntiDiagonal = i + j == n - 1;

                if (isDiagonal || isAntiDiagonal) {
                    // Diagonal elements must be non-zero
                    if (matrix[i][j] == 0) {
                        return false;
                    }
                } else {
                    // Non-diagonal elements must be zero
                    if (matrix[i][j] != 0) {
                        return false;
                    }
                }
            }
        }

        return true;
    }
}
