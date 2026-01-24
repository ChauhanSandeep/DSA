package arrays;

import java.util.*;

/**
 * Check If Every Row And Column Contains All Numbers
 *
 * Problem: Given an n x n matrix, determine if it's valid (every row and column contains
 * all integers from 1 to n exactly once).
 *
 * Example: matrix = [
 *      [1,2,3],
 *      [2,3,1],
 *      [3,1,2]
 *      ]
 * -> Output: true
 * Each row and column contains {1,2,3} exactly once.
 *
 * LeetCode: https://leetcode.com/problems/check-if-every-row-and-column-contains-all-numbers
 *
 * Follow-up Questions:
 * - How to check if it's a Latin square variant? (This problem is essentially Latin square validation)
 * - What if we need to check diagonals too? (Add diagonal validation logic)
 * - Can we solve with less space complexity? (Use bit manipulation instead of sets)
 * LeetCode Contest Rating: 1264
 */
public class CheckIfEveryRowAndColumnContainsAllNumbers {

    /**
     * Optimized approach using boolean arrays instead of sets
     *
     * Steps:
     * 1. For each row, use a boolean array to track seen numbers
     * 2. For each column, use a boolean array to track seen numbers
     * 3. If any number is out of range or already seen, return false
     * 4. If all rows and columns are valid, return true
     *
     * Time Complexity: O(n²), where n is the size of the matrix
     * Space Complexity: O(n) where n is the size of the matrix (for boolean arrays)
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
