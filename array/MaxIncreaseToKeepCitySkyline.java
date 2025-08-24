package array;

import java.util.Arrays;


/**
 * Max Increase To Keep City Skyline
 *
 * Problem: Increase building heights while preserving city skyline from 4 directions.
 * Return maximum total increase possible.
 *
 * Example: grid = [[3,0,8,4],[2,4,5,7],[9,2,6,3],[0,3,1,0]] -> Output: 35
 * Each building can be increased to min(max_in_row, max_in_column) - current_height.
 *
 * LeetCode: https://leetcode.com/problems/max-increase-to-keep-city-skyline
 *
 * Follow-up Questions:
 * - How to handle rectangular grids? (Same algorithm works for m×n grids)
 * - What if we want to preserve diagonal skylines too? (Find max along diagonals)
 * - Can we solve with less space? (Yes, calculate max on-the-fly but less readable)
 */
public class MaxIncreaseToKeepCitySkyline {

    /**
     * Calculates maximum increase while preserving skyline.
     *
     * Algorithm:
     * 1. Calculate maximum height in each row (south/north skyline)
     * 2. Calculate maximum height in each column (east/west skyline)
     * 3. For each position, max possible height = min(row_max, col_max)
     * 4. Sum all possible increases: (max_possible - current) for all positions
     *
     * Time Complexity: O(n²) where n is grid dimension
     * Space Complexity: O(n) for storing row and column maximums
     *
     * @param grid n×n matrix of building heights
     * @return maximum total increase possible
     */
    public int maxIncreaseKeepingSkyline(int[][] grid) {
        int n = grid.length;

        // Calculate max height in each row
        int[] rowMax = new int[n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                rowMax[i] = Math.max(rowMax[i], grid[i][j]);
            }
        }

        // Calculate max height in each column
        int[] colMax = new int[n];
        for (int j = 0; j < n; j++) {
            for (int i = 0; i < n; i++) {
                colMax[j] = Math.max(colMax[j], grid[i][j]);
            }
        }

        // Calculate total possible increase
        int totalIncrease = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int maxPossibleHeight = Math.min(rowMax[i], colMax[j]);
                totalIncrease += maxPossibleHeight - grid[i][j];
            }
        }

        return totalIncrease;
    }

    /**
     * Single-pass approach calculating maximums on-the-fly
     * Time Complexity: O(n²), Space Complexity: O(1)
     */
    public int maxIncreaseKeepingSkylineOptimized(int[][] grid) {
        int n = grid.length;
        int totalIncrease = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // Calculate row maximum
                int rowMax = 0;
                for (int col = 0; col < n; col++) {
                    rowMax = Math.max(rowMax, grid[i][col]);
                }

                // Calculate column maximum
                int colMax = 0;
                for (int row = 0; row < n; row++) {
                    colMax = Math.max(colMax, grid[row][j]);
                }

                // Add possible increase for this position
                int maxPossibleHeight = Math.min(rowMax, colMax);
                totalIncrease += maxPossibleHeight - grid[i][j];
            }
        }

        return totalIncrease;
    }

    /**
     * Stream-based approach using Java 8 features
     * Time Complexity: O(n²), Space Complexity: O(n)
     */
    public int maxIncreaseKeepingSkylineStream(int[][] grid) {
        int n = grid.length;

        // Calculate row maximums using streams
        int[] rowMax = new int[n];
        for (int i = 0; i < n; i++) {
            rowMax[i] = Arrays.stream(grid[i]).max().orElse(0);
        }

        // Calculate column maximums
        int[] colMax = new int[n];
        for (int j = 0; j < n; j++) {
            final int col = j;
            colMax[j] = Arrays.stream(grid)
                             .mapToInt(row -> row[col])
                             .max()
                             .orElse(0);
        }

        // Calculate total increase
        int totalIncrease = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                totalIncrease += Math.min(rowMax[i], colMax[j]) - grid[i][j];
            }
        }

        return totalIncrease;
    }
}
