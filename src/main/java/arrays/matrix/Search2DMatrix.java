package arrays.matrix;

import java.util.Arrays;
/**
 * Problem: Search a 2D Matrix
 *
 * Given an m x n matrix where each row is sorted and each row starts after the
 * previous row ends, determine whether target exists. Those rules make the whole
 * matrix behave like one sorted array.
 *
 * Leetcode: https://leetcode.com/problems/search-a-2d-matrix/ (Medium)
 * Rating:   no contest rating (pre-contest problem)
 * Pattern:  Binary search | Matrix as virtual array
 *
 * Example:
 *   Input:  matrix = [[1,3,5,7],[10,11,16,20],[23,30,34,60]], target = 3
 *   Output: true
 *   Why:    index 1 in the virtual sorted array maps to matrix[0][1] == 3.
 *
 * Follow-ups:
 *   1. Rows and columns sorted independently only?
 *      Use the staircase search from the top-right or bottom-left corner.
 *   2. Return insertion position?
 *      Run the same virtual binary search and map the final left pointer back to coordinates.
 *   3. Matrix stored on disk?
 *      Binary search still needs only O(log(mn)) random element reads.
 *
 * Related: Search a 2D Matrix II (240), Kth Smallest Element in a Sorted Matrix (378).
 */
public class Search2DMatrix {

    public static void main(String[] args) {
        Search2DMatrix solver = new Search2DMatrix();
        int[][] matrix = { {1, 3, 5, 7}, {10, 11, 16, 20}, {23, 30, 34, 60} };
        int[] targets = { 3, 13, 60 };
        boolean[] expected = { true, false, true };

        for (int i = 0; i < targets.length; i++) {
            boolean got = solver.searchMatrix(matrix, targets[i]);
            System.out.printf("matrix=%s target=%d -> output=%s  expected=%s%n",
                Arrays.deepToString(matrix), targets[i], got, expected[i]);
        }
    }

/**
 * Intuition: because every row starts after the previous row ends, row breaks do
 * not matter for ordering. Treat positions 0 through rows * cols - 1 as a single
 * sorted array and convert a midpoint back to row and column when comparing.
 *
 * Algorithm:
 *   1. Reject null or empty matrices.
 *   2. Binary search the virtual index range [0, rows * cols - 1].
 *   3. Convert mid to row = mid / cols and col = mid % cols.
 *   4. Move left or right according to matrix[row][col].
 *
 * Time:  O(log(m * n)) - standard binary search over all cells.
 * Space: O(1) - only search bounds and one mapped coordinate are stored.
 *
 * @param matrix row-major sorted matrix
 * @param target value to find
 * @return true if target exists in the matrix
 */
    public boolean searchMatrix(int[][] matrix, int target) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return false;
        }

        int rows = matrix.length;
        int cols = matrix[0].length;
        int left = 0;
        int right = rows * cols - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            // Convert 1D index to 2D coordinates
            int row = mid / cols;
            int col = mid % cols;
            int midValue = matrix[row][col];

            if (midValue == target) {
                return true;
            } else if (midValue < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return false;
    }

    /**
     * Alternative two-step binary search approach for clearer logic.
     *
     * Algorithm:
     * 1. First binary search: find the correct row where target could exist
     * 2. Second binary search: search within that row for target
     *
     * Time Complexity: O(log M + log N) = O(log(M*N)) where M is rows and N is columns.
     * Two binary searches but still logarithmic overall.
     *
     * Space Complexity: O(1) using only constant extra space.
     *
     * @param matrix 2D sorted matrix
     * @param target value to search for
     * @return true if target exists in matrix
     */
    public boolean searchMatrixTwoStep(int[][] matrix, int target) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return false;
        }

        int rows = matrix.length;
        int cols = matrix[0].length;

        // Step 1: Binary search to find the row
        int targetRow = findRow(matrix, target);

        if (targetRow == -1) {
            return false;
        }

        // Step 2: Binary search within the row
        return binarySearch(matrix[targetRow], target);
    }

    // Find the row where target could exist
    private int findRow(int[][] matrix, int target) {
        int top = 0;
        int bottom = matrix.length - 1;
        int cols = matrix[0].length;

        while (top <= bottom) {
            int mid = top + (bottom - top) / 2;

            // Check if target falls in this row's range
            if (matrix[mid][0] <= target && target <= matrix[mid][cols - 1]) {
                return mid;
            } else if (target < matrix[mid][0]) {
                bottom = mid - 1;
            } else {
                top = mid + 1;
            }
        }

        return -1;
    }

    // Standard binary search in 1D array
    private boolean binarySearch(int[] arr, int target) {
        int left = 0;
        int right = arr.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (arr[mid] == target) {
                return true;
            } else if (arr[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return false;
    }
}
