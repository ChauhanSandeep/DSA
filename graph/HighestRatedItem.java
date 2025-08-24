package graph;

import java.util.*;

/**
 * Problem: Find the top-k highest-ranked items in a grid, based on specific sorting criteria.
 *
 * Statement:
 * - You are given a grid of items represented by integers (0 = inaccessible cell, >0 = item price).
 * - From a given start position, you need to find the top `k` items within a given price range.
 * - Ranking Criteria (in order of priority):
 *   1. Shortest Manhattan distance from the start position.
 *   2. Lower price.
 *   3. Smaller row index.
 *   4. Smaller column index.
 *
 * Example:
 * Input:
 * grid = [[1,2,0,1],
 *         [1,3,3,1],
 *         [0,2,5,1]]
 * pricing = [2,3], start = [2,3], k = 2
 * Output: [[2,1],[1,1]]
 * Explaination:
 *
 *
 * LeetCode Link: https://leetcode.com/problems/k-highest-ranked-items-within-a-price-range/
 *
 * Follow-up Questions:
 * 1. Can this be solved without a priority queue?
 *    - Yes, we can use BFS level-by-level traversal and sort valid items within each level before adding.
 * 2. What if the grid is extremely large (e.g., millions of cells)?
 *    - Use pruning (stop BFS when no further valid items can be found within the required `k`).
 * 3. How to optimize memory if visited array is too large?
 *    - Use a BitSet or encode visited positions into a HashSet with "row*cols+col".
 */
public class HighestRatedItem {

    public static void main(String[] args) {
        int[][] grid = {
            {1, 2, 0, 1},
            {1, 3, 3, 1},
            {0, 2, 5, 1}
        };
        int[] pricing = {2, 3}; // Price range: [min, max]
        int[] start = {2, 3};   // Starting position
        int k = 2;              // Number of items to return

        HighestRatedItem solution = new HighestRatedItem();
        List<List<Integer>> result = solution.highestRankedKItems(grid, pricing, start, k);

        System.out.println("Top-K highest-ranked items: " + result);
    }

    /**
     * BFS + PriorityQueue approach.
     *
     * Steps:
     * 1. Start BFS from the given starting cell.
     * 2. Use a MinHeap to always prioritize the next best-ranked item.
     * 3. For each visited cell, check if it falls in the price range and collect it.
     * 4. Stop once k items are collected.
     *
     * Algorithm: BFS + MinHeap (PriorityQueue)
     * Time Complexity: O(R * C * log(R * C))
     * Space Complexity: O(R * C)
     *
     * @param grid    2D matrix of prices.
     * @param pricing Array [minPrice, maxPrice].
     * @param start   Start position [row, col].
     * @param k       Number of items to return.
     * @return List of positions of the top-k highest-ranked items.
     */
    public List<List<Integer>> highestRankedKItems(int[][] grid, int[] pricing, int[] start, int k) {
        int rows = grid.length, cols = grid[0].length;
        int minPrice = pricing[0], maxPrice = pricing[1];

        List<List<Integer>> result = new ArrayList<>();
        boolean[][] visited = new boolean[rows][cols];

        // MinHeap: order by (distance → price → row → col)
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> {
            if (a[2] != b[2]) return Integer.compare(a[2], b[2]);
            if (grid[a[0]][a[1]] != grid[b[0]][b[1]]) return Integer.compare(grid[a[0]][a[1]], grid[b[0]][b[1]]);
            if (a[0] != b[0]) return Integer.compare(a[0], b[0]);
            return Integer.compare(a[1], b[1]);
        });

        final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // Up, Down, Left, Right

        // Start BFS from start position
        minHeap.offer(new int[]{start[0], start[1], 0}); // row, col, distance
        visited[start[0]][start[1]] = true;

        while (!minHeap.isEmpty() && k > 0) {
            int[] current = minHeap.poll();
            int row = current[0], col = current[1], distance = current[2];

            // If current cell is valid and within price range, add to result
            if (grid[row][col] >= minPrice && grid[row][col] <= maxPrice) {
                result.add(Arrays.asList(row, col));
                k--;
            }

            // Explore neighbors
            for (int[] dir : DIRECTIONS) {
                int newRow = row + dir[0], newCol = col + dir[1];
                if (isValidCell(newRow, newCol, rows, cols, grid, visited)) {
                    visited[newRow][newCol] = true;
                    minHeap.offer(new int[]{newRow, newCol, distance + 1});
                }
            }
        }
        return result;
    }

    /**
     * Check if the cell is valid for BFS traversal.
     */
    private boolean isValidCell(int row, int col, int rows, int cols, int[][] grid, boolean[][] visited) {
        return row >= 0 && row < rows &&
            col >= 0 && col < cols &&
            grid[row][col] != 0 &&
            !visited[row][col];
    }

    /**
     * Optimized Alternative (Without PriorityQueue).
     * BFS Level-order traversal approach.
     *
     * - Traverse level by level using BFS.
     * - At each level, collect items within price range and sort them by (price, row, col).
     * - Continue until k items are collected.
     *
     * Time Complexity: O(R * C * log(R * C)) (due to sorting per level)
     * Space Complexity: O(R * C)
     */
    public List<List<Integer>> highestRankedKItemsBFSOnly(int[][] grid, int[] pricing, int[] start, int k) {
        int rows = grid.length, cols = grid[0].length;
        int minPrice = pricing[0], maxPrice = pricing[1];

        List<List<Integer>> result = new ArrayList<>();
        boolean[][] visited = new boolean[rows][cols];
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{start[0], start[1], 0});
        visited[start[0]][start[1]] = true;

        final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        while (!queue.isEmpty() && k > 0) {
            int size = queue.size();
            List<int[]> currentLevelItems = new ArrayList<>();

            for (int i = 0; i < size; i++) {
                int[] curr = queue.poll();
                int row = curr[0], col = curr[1], dist = curr[2];

                if (grid[row][col] >= minPrice && grid[row][col] <= maxPrice) {
                    currentLevelItems.add(curr);
                }

                for (int[] dir : DIRECTIONS) {
                    int newRow = row + dir[0], newCol = col + dir[1];
                    if (isValidCell(newRow, newCol, rows, cols, grid, visited)) {
                        visited[newRow][newCol] = true;
                        queue.offer(new int[]{newRow, newCol, dist + 1});
                    }
                }
            }

            // Sort items of this level by (price, row, col)
            currentLevelItems.sort((a, b) -> {
                if (grid[a[0]][a[1]] != grid[b[0]][b[1]])
                    return Integer.compare(grid[a[0]][a[1]], grid[b[0]][b[1]]);
                if (a[0] != b[0]) return Integer.compare(a[0], b[0]);
                return Integer.compare(a[1], b[1]);
            });

            for (int[] item : currentLevelItems) {
                if (k == 0) break;
                result.add(Arrays.asList(item[0], item[1]));
                k--;
            }
        }

        return result;
    }
}
