package arrays.matrix;

/**
 * Check If Matrix Is X Matrix
 *
 * Problem: A matrix is an X-Matrix if all elements on the main diagonal and anti-diagonal
 * are non-zero, and all other elements are zero.
 *
 * Example: matrix = [
 *      [2,0,0,1],
 *      [0,3,1,0],
 *      [0,5,2,0],
 *      [4,0,0,2]
 *      ]
 *      -> Output: true
 * Diagonal and anti-diagonal elements are non-zero, others are zero.
 *
 * LeetCode: https://leetcode.com/problems/check-if-matrix-is-x-matrix
 *
 * Follow-up Questions:
 * - How to check other patterns like plus (+) matrix? (Modify diagonal conditions)
 * - What if we want to allow different non-zero values on diagonals? (Current solution already handles this)
 * - Can we solve without nested loops? (No, need to examine each element)
 */
public class CheckIfMatrixIsXMatrix {

    /**
     * Checks if the given matrix forms an X pattern.
     *
     * Algorithm:
     * 1. Iterate through all positions in the matrix
     * 2. For each position (i,j), determine if it's on main/anti-diagonal
     * 3. If on diagonal: element must be non-zero
     * 4. If not on diagonal: element must be zero
     * 5. Return false if any position violates the rule
     *
     * Time Complexity: O(n²) where n is matrix dimension
     * Space Complexity: O(1) - only using constant extra space
     *
     * @param matrix n x n integer matrix
     * @return true if matrix forms an X pattern
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
