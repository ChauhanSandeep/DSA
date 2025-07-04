package Array;

/**
 * 🔹 Problem: Search a 2D Matrix
 * 🔗 Leetcode: https://leetcode.com/problems/search-a-2d-matrix/
 *
 * You are given an `m x n` matrix where each row is sorted in ascending order
 * and the first integer of each row is greater than the last integer of the previous row.
 *
 * Given a target value, return true if it exists in the matrix, or false otherwise.
 *
 * 📌 Example:
 * Input: matrix = [
 *     [1, 3, 5, 7],
 *     [10, 11, 16, 20],
 *     [23, 30, 34, 60]
 * ], target = 3
 * Output: true
 *
 * ✅ Follow-up:
 * 1. Could we treat this like binary search over a 1D array?
 *    → Yes! The matrix is strictly increasing as a whole.
 * 2. Could we do binary search row-by-row?
 *    → Less efficient (O(m log n)) than this optimal approach (O(log(m*n)))
 */
public class Search2DMatrix {

    public static void main(String[] args) {
        int[][] matrix = {
            {1, 3, 5, 7},
            {10, 11, 16, 20},
            {23, 30, 34, 60}
        };

        System.out.println("Is 11 present? " + new Search2DMatrix().searchMatrix(matrix, 11)); // true
        System.out.println("Is 13 present? " + new Search2DMatrix().searchMatrix(matrix, 13)); // false
    }

    /**
     * Searches for a target value in a sorted 2D matrix.
     *
     * 🔹 Algorithm:
     * - Treat the 2D matrix as a flat sorted array of size (rows * cols)
     * - Perform binary search on the imaginary 1D array
     * - Convert 1D index to 2D using:
     *     → row = index / number of columns
     *     → col = index % number of columns
     *
     * 🔹 Time Complexity: O(log(M * N)) where M = number of rows, N = number of cols
     * 🔹 Space Complexity: O(1)
     *
     * @param matrix 2D sorted matrix
     * @param target value to search for
     * @return true if found, false otherwise
     */
    public boolean searchMatrix(int[][] matrix, int target) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0)
            return false;

        int rowCount = matrix.length;
        int colCount = matrix[0].length;
        int left = 0;
        int right = rowCount * colCount - 1;

        while (left <= right) {
            int midIndex = left + (right - left) / 2;
            int midValue = matrix[midIndex / colCount][midIndex % colCount]; // Map 1D index to 2D

            if (midValue == target) {
                return true;
            } else if (midValue < target) {
                left = midIndex + 1;
            } else {
                right = midIndex - 1;
            }
        }

        return false;
    }
}