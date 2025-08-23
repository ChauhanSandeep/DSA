package Array;

import java.util.*;

/**
 * Check If Every Row And Column Contains All Numbers
 * 
 * Problem: Given an n x n matrix, determine if it's valid (every row and column contains 
 * all integers from 1 to n exactly once).
 * 
 * Example: matrix = [[1,2,3],[2,3,1],[3,1,2]] -> Output: true
 * Each row and column contains {1,2,3} exactly once.
 * 
 * LeetCode: https://leetcode.com/problems/check-if-every-row-and-column-contains-all-numbers
 * 
 * Follow-up Questions:
 * - How to check if it's a Latin square variant? (This problem is essentially Latin square validation)
 * - What if we need to check diagonals too? (Add diagonal validation logic)
 * - Can we solve with less space complexity? (Use bit manipulation instead of sets)
 */
public class CheckIfEveryRowAndColumnContainsAllNumbers {

    /**
     * Validates if matrix is a valid Latin square (each row/column has 1 to n).
     * 
     * Algorithm:
     * 1. For each row, use set to track seen numbers
     * 2. Verify each row contains exactly numbers 1 to n
     * 3. For each column, use set to track seen numbers  
     * 4. Verify each column contains exactly numbers 1 to n
     * 5. Return true only if all rows and columns are valid
     * 
     * Time Complexity: O(n²) where n is matrix dimension
     * Space Complexity: O(n) for the temporary sets
     * 
     * @param matrix n x n integer matrix
     * @return true if every row and column contains all numbers from 1 to n
     */
    public boolean checkValid(int[][] matrix) {
        int n = matrix.length;

        // Check all rows
        for (int i = 0; i < n; i++) {
            Set<Integer> seen = new HashSet<>();
            for (int j = 0; j < n; j++) {
                int value = matrix[i][j];
                // Check if value is in valid range and not duplicate
                if (value < 1 || value > n || !seen.add(value)) {
                    return false;
                }
            }
        }

        // Check all columns
        for (int j = 0; j < n; j++) {
            Set<Integer> seen = new HashSet<>();
            for (int i = 0; i < n; i++) {
                int value = matrix[i][j];
                // Check if value is in valid range and not duplicate
                if (value < 1 || value > n || !seen.add(value)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Optimized approach using boolean arrays instead of sets
     * Time Complexity: O(n²), Space Complexity: O(n)
     */
    public boolean checkValidOptimized(int[][] matrix) {
        int n = matrix.length;

        // Check rows
        for (int i = 0; i < n; i++) {
            boolean[] seen = new boolean[n + 1]; // Index 0 unused
            for (int j = 0; j < n; j++) {
                int value = matrix[i][j];
                if (value < 1 || value > n || seen[value]) {
                    return false;
                }
                seen[value] = true;
            }
        }

        // Check columns
        for (int j = 0; j < n; j++) {
            boolean[] seen = new boolean[n + 1]; // Index 0 unused
            for (int i = 0; i < n; i++) {
                int value = matrix[i][j];
                if (value < 1 || value > n || seen[value]) {
                    return false;
                }
                seen[value] = true;
            }
        }

        return true;
    }
}
