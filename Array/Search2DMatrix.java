package Array;

/**
 * Write an efficient algorithm that searches for a value in an m x n matrix. This matrix has the following properties:
 * - Integers in each row are sorted from left to right.
 * - The first integer of each row is greater than the last integer of the previous row.
 * 
 * Example 1:
 * Input: matrix = [[1,3,5,7],[10,11,16,20],[23,30,34,60]], target = 3
 * Output: true
 * 
 * Example 2:
 * Input: matrix = [[1,3,5,7],[10,11,16,20],[23,30,34,60]], target = 13
 * Output: false
 * 
 * LeetCode: https://leetcode.com/problems/search-a-2d-matrix/
 * 
 * Follow-up Questions:
 * 1. How would you handle very large matrices that don't fit in memory?
 *    - We could use binary search on the virtual 1D array without materializing it.
 * 2. What if the matrix is sorted row-wise and column-wise but doesn't have the second property?
 *    - We'd need a different approach, like starting from the top-right corner.
 * 3. How would you find all occurrences of the target value?
 *    - We could find the range of rows and columns where the target appears.
 * 
 * Related Problems:
 * - Search a 2D Matrix II (https://leetcode.com/problems/search-a-2d-matrix-ii/)
 * - Kth Smallest Element in a Sorted Matrix (https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/)
 */
public class Search2DMatrix {
    /**
     * Searches for a target value in the matrix using binary search.
     * 
     * @param matrix The m x n matrix with sorted rows and columns
     * @param target The value to search for
     * @return true if the target is found, false otherwise
     */
    public boolean searchMatrix(int[][] matrix, int target) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return false;
        }
        
        int m = matrix.length;
        int n = matrix[0].length;
        
        // Binary search on the virtual 1D array
        int left = 0;
        int right = m * n - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            int midValue = matrix[mid / n][mid % n];
            
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
     * Alternative approach using two binary searches:
     * 1. First find the correct row using binary search
     * 2. Then search within that row using binary search
     */
    public boolean searchMatrixTwoBinarySearches(int[][] matrix, int target) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return false;
        }
        
        int m = matrix.length;
        int n = matrix[0].length;
        
        // Binary search to find the correct row
        int top = 0;
        int bottom = m - 1;
        int targetRow = -1;
        
        while (top <= bottom) {
            int mid = top + (bottom - top) / 2;
            if (matrix[mid][0] <= target && target <= matrix[mid][n - 1]) {
                targetRow = mid;
                break;
            } else if (matrix[mid][0] > target) {
                bottom = mid - 1;
            } else {
                top = mid + 1;
            }
        }
        
        if (targetRow == -1) {
            return false; // Target is not in any row
        }
        
        // Binary search within the target row
        int left = 0;
        int right = n - 1;
        int[] row = matrix[targetRow];
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (row[mid] == target) {
                return true;
            } else if (row[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return false;
    }
    
    /**
     * Solution for when the matrix is sorted row-wise and column-wise but doesn't have
     * the second property (first element of row > last element of previous row).
     * This is LeetCode #240: Search a 2D Matrix II
     */
    public boolean searchMatrixII(int[][] matrix, int target) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return false;
        }
        
        int m = matrix.length;
        int n = matrix[0].length;
        
        // Start from the top-right corner
        int row = 0;
        int col = n - 1;
        
        while (row < m && col >= 0) {
            if (matrix[row][col] == target) {
                return true;
            } else if (matrix[row][col] < target) {
                // Move down if current element is smaller than target
                row++;
            } else {
                // Move left if current element is larger than target
                col--;
            }
        }
        
        return false;
    }
    
    /**
     * Finds the first occurrence of the target in the matrix.
     * Returns the coordinates as an array [row, col], or [-1, -1] if not found.
     */
    public int[] searchMatrixFindFirst(int[][] matrix, int target) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return new int[]{-1, -1};
        }
        
        int m = matrix.length;
        int n = matrix[0].length;
        
        int left = 0;
        int right = m * n - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            int midValue = matrix[mid / n][mid % n];
            
            if (midValue == target) {
                // Found a match, now find the first occurrence
                int row = mid / n;
                int col = mid % n;
                
                // Find the first occurrence in the row
                int firstInRow = col;
                while (firstInRow > 0 && matrix[row][firstInRow - 1] == target) {
                    firstInRow--;
                }
                
                // Find the first row with this target
                int firstRow = row;
                while (firstRow > 0 && matrix[firstRow - 1][firstInRow] == target) {
                    firstRow--;
                }
                
                return new int[]{firstRow, firstInRow};
                
            } else if (midValue < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return new int[]{-1, -1};
    }
}
