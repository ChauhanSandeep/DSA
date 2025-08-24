package array;

/**
 * Write an efficient algorithm that searches for a target value in an m x n integer matrix.
 * This matrix has the following properties:
 * - Integers in each row are sorted in ascending from left to right.
 * - Integers in each column are sorted in ascending from top to bottom.
 *
 * Example 1:
 * Input: matrix = [[1,4,7,11,15],[2,5,8,12,19],[3,6,9,16,22],[10,13,14,17,24],[18,21,23,26,30]], target = 5
 * Output: true
 *
 * Example 2:
 * Input: matrix = [[1,4,7,11,15],[2,5,8,12,19],[3,6,9,16,22],[10,13,14,17,24],[18,21,23,26,30]], target = 20
 * Output: false
 *
 * LeetCode: https://leetcode.com/problems/search-a-2d-matrix-ii/
 *
 * Follow-up Questions:
 * 1. How would you find all occurrences of the target value?
 *    - We could modify the search to continue after finding a match.
 * 2. What if the matrix is very large and doesn't fit in memory?
 *    - We could implement an external search that loads only necessary rows/columns.
 * 3. How would you find the kth smallest element in this matrix?
 *    - We could use a min-heap or binary search approach.
 *
 * Related Problems:
 * - Search a 2D Matrix (https://leetcode.com/problems/search-a-2d-matrix/)
 * - Kth Smallest Element in a Sorted Matrix (https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/)
 */
public class Search2DMatrixII {
    /**
     * Searches for a target value in a 2D matrix using the search space reduction approach.
     * Starts from the top-right corner and eliminates either a row or column in each step.
     *
     * @param matrix The 2D matrix to search in
     * @param target The value to search for
     * @return true if target is found, false otherwise
     */
    public boolean searchMatrix(int[][] matrix, int target) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return false;
        }

        int rows = matrix.length;
        int cols = matrix[0].length;

        // Start from the top-right corner
        int row = 0;
        int col = cols - 1;

        while (row < rows && col >= 0) {
            if (matrix[row][col] == target) {
                return true;
            } else if (matrix[row][col] < target) {
                // Target is larger, move down to a larger value
                row++;
            } else {
                // Target is smaller, move left to a smaller value
                col--;
            }
        }

        return false;
    }

    /**
     * Alternative approach starting from the bottom-left corner.
     * This approach is similar but might be more intuitive for some.
     */
    public boolean searchMatrixFromBottomLeft(int[][] matrix, int target) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return false;
        }

        int rows = matrix.length;
        int cols = matrix[0].length;

        // Start from the bottom-left corner
        int row = rows - 1;
        int col = 0;

        while (row >= 0 && col < cols) {
            if (matrix[row][col] == target) {
                return true;
            } else if (matrix[row][col] < target) {
                // Target is larger, move right to a larger value
                col++;
            } else {
                // Target is smaller, move up to a smaller value
                row--;
            }
        }

        return false;
    }

    /**
     * Binary search approach for each row.
     * This approach is less efficient but demonstrates an alternative method.
     * Time complexity: O(m log n) where m is the number of rows and n is the number of columns.
     */
    public boolean searchMatrixBinarySearch(int[][] matrix, int target) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return false;
        }

        for (int[] row : matrix) {
            if (binarySearch(row, target)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Helper method for binary search in a sorted array.
     */
    private boolean binarySearch(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (nums[mid] == target) {
                return true;
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return false;
    }

    /**
     * Finds all occurrences of the target in the matrix.
     * Returns a list of coordinates where the target is found.
     */
    public java.util.List<int[]> findAllOccurrences(int[][] matrix, int target) {
        java.util.List<int[]> result = new java.util.ArrayList<>();

        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return result;
        }

        int rows = matrix.length;
        int cols = matrix[0].length;

        // Start from the top-right corner
        int row = 0;
        int col = cols - 1;

        while (row < rows && col >= 0) {
            if (matrix[row][col] == target) {
                // Found a match, add to result
                result.add(new int[]{row, col});

                // Move to the next position (diagonally down-left)
                row++;
                col--;
            } else if (matrix[row][col] < target) {
                // Target is larger, move down
                row++;
            } else {
                // Target is smaller, move left
                col--;
            }
        }

        return result;
    }

    /**
     * Finds the kth smallest element in the matrix.
     * This is the solution to "Kth Smallest Element in a Sorted Matrix" (LeetCode #378).
     */
    public int kthSmallest(int[][] matrix, int k) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return -1;
        }

        int n = matrix.length;
        int left = matrix[0][0];
        int right = matrix[n-1][n-1];

        while (left < right) {
            int mid = left + (right - left) / 2;
            int count = countLessOrEqual(matrix, mid);

            if (count < k) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }

        return left;
    }

    /**
     * Counts the number of elements less than or equal to the given value in the matrix.
     */
    private int countLessOrEqual(int[][] matrix, int target) {
        int count = 0;
        int n = matrix.length;
        int row = n - 1;
        int col = 0;

        while (row >= 0 && col < n) {
            if (matrix[row][col] <= target) {
                count += row + 1;
                col++;
            } else {
                row--;
            }
        }

        return count;
    }
}
