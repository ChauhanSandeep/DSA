package arrays.greedy;

/**
 * LeetCode Problem 807: Max Increase to Keep City Skyline
 *
 * Problem Statement:
 * You have a city represented as an n x n grid where each cell grid[r][c] contains the
 * height of a building at row r and column c. The skyline is what you see when looking
 * at the city from far away in each direction (north, east, south, west).
 *
 * When viewing from:
 * - North or South: You see the maximum height in each column
 * - East or West: You see the maximum height in each row
 *
 * You are allowed to increase the heights of buildings by any amount, but the skyline
 * from all four directions must remain unchanged. Return the maximum total sum by which
 * the height of all buildings can be increased without changing any skyline view.
 *
 * Example 1:
 * Input: grid = [[3,0,8,4],[2,4,5,7],[9,2,6,3],[0,3,1,0]]
 * Output: 35
 * Explanation:
 * Original grid:
 * [[3, 0, 8, 4],
 *  [2, 4, 5, 7],
 *  [9, 2, 6, 3],
 *  [0, 3, 1, 0]]
 *
 * Row maximums (East/West skyline): [
 *      8,
 *      7,
 *      9,
 *      3
 * ]
 * Column maximums (North/South skyline): [9, 4, 8, 7]
 *
 * For each position (i,j), the maximum height without changing skyline is
 * min(rowMax[i], colMax[j]). The grid after maximum increases:
 * [[8, 4, 8, 7],
 *  [7, 4, 7, 7],
 *  [9, 4, 8, 7],
 *  [3, 3, 3, 3]]
 *
 * Total increase = (8-3) + (4-0) + (0) + (3) + (5) + (0) + (2) + (0) + (0) + (2) + (2) +
 * (4) + (3) + (0) + (2) + (3) = 35
 *
 * Example 2:
 * Input: grid = [[0,0,0],[0,0,0],[0,0,0]]
 * Output: 0
 * Explanation: Increasing any building height will change the skyline from some direction.
 *
 * Constraints:
 * - n == grid.length
 * - n == grid[r].length
 * - 2 <= n <= 50
 * - 0 <= grid[r][c] <= 100
 *
 * LeetCode Link: https://leetcode.com/problems/max-increase-to-keep-city-skyline/
 *
 * Follow-up Questions:
 * 1. Q: How would you solve this if we can decrease building heights as well?
 *    A: The problem becomes finding the total adjustment (absolute difference) to reach
 *    the optimal configuration. Each building should be adjusted to min(rowMax[i], colMax[j]).
 *    Calculate sum of |grid[i][j] - min(rowMax[i], colMax[j])| for all positions.
 *
 * 2. Q: What if there are restrictions on which buildings can be modified?
 *    A: Add a boolean mask array indicating modifiable buildings. When computing the total
 *    increase, only include positions where the mask is true. The skyline computation remains
 *    the same since we need to preserve views from all directions.
 *
 * 3. Q: How would you extend this to a 3D grid (multiple floors)?
 *    A: For 3D with dimensions n x m x h, maintain skyline views from 6 directions (all faces
 *    of the cuboid). Each cell (i,j,k) can increase to min of the maximum values along each
 *    of the three axes through that point. The principle generalizes but requires tracking
 *    3D maximums.
 *
 * 4. Q: What if building increases have different costs per unit height?
 *    A: Model as a maximum profit problem. For each building at (i,j), the maximum increase
 *    is still min(rowMax[i], colMax[j]) - grid[i][j]. Multiply by the unit cost at that
 *    position to get the profit. Sum all profits. Use greedy approach if we have a budget
 *    constraint (sort by cost-effectiveness ratio).
 *
 * 5. Q: How would you solve this if skyline must be maintained after each incremental update?
 *    A: Use dynamic programming or greedy approach. Process buildings in order, maintaining
 *    current row and column maximums. Before increasing a building, check if it would exceed
 *    current skyline constraints. Update maximums incrementally. This is useful for online
 *    scenarios where updates come sequentially.
 * LeetCode Contest Rating: 1376
 **/
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
