package arrays.greedy;

import java.util.Arrays;

/**
 * Problem: Max Increase to Keep City Skyline
 *
 * A city grid stores one building height per cell. Increase building heights as
 * much as possible while keeping every row skyline and every column skyline
 * exactly the same from all four directions.
 *
 * Leetcode: https://leetcode.com/problems/max-increase-to-keep-city-skyline/ (Medium)
 * Rating:   acceptance 86.5% (Medium) - contest rating 1376
 * Pattern:  Arrays | Matrix scanning | Row/column maximums
 *
 * Example:
 *   Input:  grid = [[3,0,8,4],[2,4,5,7],[9,2,6,3],[0,3,1,0]]
 *   Output: 35
 *   Why:    each cell can rise only to min(rowMax, colMax), so all added
 *           heights sum to 35 without changing any skyline.
 *
 * Follow-ups:
 *   1. What if the grid is rectangular instead of n x n?
 *      Store one maximum per row and one per column; the same formula applies.
 *   2. What if only some buildings may be increased?
 *      Compute the same caps, but add increases only for allowed cells.
 *   3. What if decreases are also allowed?
 *      Target min(rowMax, colMax) and sum absolute adjustment costs.
 *
 * Related: Trapping Rain Water II (407), Projection Area of 3D Shapes (883).
 */
public class MaxIncreaseToKeepCitySkyline {

    public static void main(String[] args) {
        MaxIncreaseToKeepCitySkyline solver = new MaxIncreaseToKeepCitySkyline();
        int[][][] inputs = {
            {{3, 0, 8, 4}, {2, 4, 5, 7}, {9, 2, 6, 3}, {0, 3, 1, 0}},
            {{0, 0}, {0, 0}}
        };
        int[] expected = {35, 0};

        for (int i = 0; i < inputs.length; i++) {
            int got = solver.maxIncreaseKeepingSkyline(inputs[i]);
            System.out.printf("grid=%s -> %d  expected=%d%n",
                Arrays.deepToString(inputs[i]), got, expected[i]);
        }
    }


    /**
     * Intuition: a building can grow only until it hits one of the skylines that
     * already constrains it. Its row maximum protects the east/west view and its
     * column maximum protects the north/south view, so the safe cap for cell
     * (i, j) is the smaller of rowMax[i] and colMax[j].
     *
     * Algorithm:
     *   1. Scan every row to compute rowMax.
     *   2. Scan every column to compute colMax.
     *   3. For every cell, add min(rowMax[i], colMax[j]) - grid[i][j].
     *
     * Time:  O(n^2) - the code scans the n x n grid a constant number of times.
     * Space: O(n) - rowMax and colMax store one value per row and column.
     *
     * @param grid n x n matrix of building heights
     * @return maximum total height increase that preserves all skylines
     */
    public int maxIncreaseKeepingSkyline(int[][] grid) {
        int length = grid.length;

        // Calculate max height in each row
        int[] rowMax = new int[length];
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                rowMax[i] = Math.max(rowMax[i], grid[i][j]);
            }
        }

        // Calculate max height in each column
        int[] colMax = new int[length];
        for (int j = 0; j < length; j++) {
            for (int i = 0; i < length; i++) {
                colMax[j] = Math.max(colMax[j], grid[i][j]);
            }
        }

        // Calculate total possible increase
        int totalIncrease = 0;
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                int maxPossibleHeight = Math.min(rowMax[i], colMax[j]);
                totalIncrease += maxPossibleHeight - grid[i][j];
            }
        }

        return totalIncrease;
    }

    /**
     * Alternative approach computing row and column maximums in a single pass.
     * This is a minor optimization that combines the first two passes into one.
     *
     * Algorithm: Single-pass maximum computation with simultaneous row/column scanning
     *
     * Step-by-step approach:
     * 1. In a single iteration through the grid (row by row):
     *    a. Update row maximum for current row
     *    b. Update column maximum for current column
     * 2. After first pass, all row and column maximums are computed
     * 3. Second pass computes total increase as in optimal solution
     *
     * This approach slightly improves cache locality by processing both row and column
     * maximums in the same iteration, though asymptotic complexity remains the same.
     *
     * Time Complexity: O(n^2) for two passes through the grid
     * Space Complexity: O(n) for row and column maximum arrays
     *
     * @param grid n x n matrix representing building heights
     * @return maximum total sum of height increases while preserving skyline
     */
    public int maxIncreaseKeepingSkylineAlternative(int[][] grid) {
        int length = grid.length;
        int[] rowMax = new int[length];
        int[] colMax = new int[length];

        // Single pass to compute both row and column maximums
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                rowMax[i] = Math.max(rowMax[i], grid[i][j]);
                colMax[j] = Math.max(colMax[j], grid[i][j]);
            }
        }

        // Calculate total increase
        int totalIncrease = 0;
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                totalIncrease += Math.min(rowMax[i], colMax[j]) - grid[i][j];
            }
        }

        return totalIncrease;
    }
}
