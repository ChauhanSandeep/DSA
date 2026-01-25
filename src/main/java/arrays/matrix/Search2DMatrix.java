package arrays.matrix;

/**
 * Problem: Search a 2D Matrix
 *
 * You are given an m x n integer matrix with the following two properties:
 * - Each row is sorted in non-decreasing order
 * - The first integer of each row is greater than the last integer of the previous row
 *
 * Given an integer target, return true if target is in matrix or false otherwise.
 * You must write a solution in O(log(m * n)) time complexity.
 *
 * Example:
 * Input: matrix = [[1,3,5,7],[10,11,16,20],[23,30,34,60]], target = 3
 * Output: true
 * Explanation: 3 exists in the first row of the matrix.
 *
 * Constraints:
 * - m == matrix.length
 * - n == matrix[i].length
 * - 1 <= m, n <= 100
 * - -10^4 <= matrix[i][j], target <= 10^4
 *
 * LeetCode Problem: https://leetcode.com/problems/search-a-2d-matrix
 *
 * Follow-up Questions:
 *
 * 1. What if rows are sorted but first element of a row is NOT guaranteed greater than last of previous?
 *    Answer: Use different approach - start from top-right or bottom-left corner. Move left if
 *    target is smaller, down if larger. This gives O(m+n) time.
 *    Related problem: https://leetcode.com/problems/search-a-2d-matrix-ii/
 *
 * 2. How would you modify this to find all occurrences if duplicates exist?
 *    Answer: After finding target with binary search, expand left and right to find all
 *    occurrences in that row, then check adjacent rows. Or collect all matching positions
 *    during a modified binary search.
 *
 * 3. What if matrix is extremely large and stored on disk, not in memory?
 *    Answer: Use external memory algorithms. Binary search still works as it only needs
 *    to access O(log n) positions. Fetch required rows/elements from disk on demand.
 *
 * 4. Can you find the kth smallest element in this matrix efficiently?
 *    Answer: Use binary search on value range, not on indices. For each mid value, count
 *    elements ≤ mid using the sorted property. Adjust range until finding kth smallest.
 *    Related problem: https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/
 *
 * 5. How would you handle if matrix dimensions are not known in advance?
 *    Answer: Exponential search to find bounds first. Start with small range, double until
 *    finding element out of bounds, then apply binary search on discovered range.
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class Search2DMatrix {

    /**
     * Searches for target using single binary search by treating 2D matrix as 1D sorted array.
     *
     * Algorithm:
     * 1. Treat m×n matrix as sorted array of length m*n
     * 2. Map 1D index to 2D coordinates: row = index/n, col = index%n
     * 3. Apply standard binary search on this virtual 1D array
     * 4. Return true if target found, false otherwise
     *
     * Key insight: Since rows are sorted AND first element of each row > last element
     * of previous row, entire matrix can be viewed as one sorted sequence. Binary search
     * works directly with index arithmetic to map between 1D and 2D.
     *
     * Time Complexity: O(log(M*N)) where M is rows and N is columns. Standard binary
     * search over M*N elements.
     *
     * Space Complexity: O(1) using only constant extra variables.
     *
     * @param matrix 2D sorted matrix
     * @param target value to search for
     * @return true if target exists in matrix
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
