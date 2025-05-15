package Graph;

import java.util.*;

public class HighestRatedItem {
    /**
     * Problem: Find the top-k highest-ranked items in a grid, based on specific sorting criteria.
     * 
     * Intuition:
     * - We perform a **Breadth-First Search (BFS)** from the given start position.
     * - We use a **Min Heap (PriorityQueue)** to prioritize:
     *   1. Shortest Manhattan distance
     *   2. Lower price
     *   3. Row-wise ordering
     *   4. Column-wise ordering
     * - The first `k` valid items found (within the price range) are returned.
     *
     * Algorithm:
     * 1. Use BFS for shortest path traversal.
     * 2. Use a Min Heap (PriorityQueue) to maintain sorting criteria.
     * 3. Track visited cells to prevent revisits.
     * 4. Extract the top-k highest-ranked items.
     *
     * Time Complexity: O(R * C * log(R * C)) - BFS visits each cell once, and heap operations take logN time.
     * Space Complexity: O(R * C) - For visited matrix and priority queue.
     * 
     * LeetCode Link: [Add relevant problem link if available]
     */
    public static void main(String[] args) {
        int[][] grid = {
                {1, 2, 0, 1},
                {1, 3, 3, 1},
                {0, 2, 5, 1}
        };
        int[] pricing = {2, 3}; // Price range: [lower bound, upper bound]
        int[] start = {2, 3};   // Starting position
        int k = 2;              // Number of top-ranked items to return

        HighestRatedItem solution = new HighestRatedItem();
        List<List<Integer>> result = solution.highestRankedKItems(grid, pricing, start, k);

        System.out.println("Top-K highest-ranked items: " + result);
    }

    /**
     * Finds the highest-ranked K items within a price range using BFS.
     * 
     * @param grid    2D matrix representing the items (0 means inaccessible cell).
     * @param pricing Array containing the price range [minPrice, maxPrice].
     * @param start   The starting position [row, col].
     * @param k       Number of items to return.
     * @return List of positions of the top-k highest-ranked items.
     */
    public List<List<Integer>> highestRankedKItems(int[][] grid, int[] pricing, int[] start, int k) {
        int rows = grid.length, cols = grid[0].length;
        int minPrice = pricing[0], maxPrice = pricing[1];

        List<List<Integer>> result = new ArrayList<>();
        boolean[][] visited = new boolean[rows][cols];

        // Min Heap: Prioritize items based on distance, price, row, and column
        PriorityQueue<int[]> priorityQueue = new PriorityQueue<>((a, b) -> {
            if (a[2] != b[2]) return Integer.compare(a[2], b[2]); // Sort by distance
            if (grid[a[0]][a[1]] != grid[b[0]][b[1]]) return Integer.compare(grid[a[0]][a[1]], grid[b[0]][b[1]]); // Sort by price
            if (a[0] != b[0]) return Integer.compare(a[0], b[0]); // Sort by row index
            return Integer.compare(a[1], b[1]); // Sort by column index
        });

        // Directions for BFS traversal: {Up, Down, Left, Right}
        final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        // Start BFS from the given start position
        priorityQueue.offer(new int[]{start[0], start[1], 0}); // {row, col, distance}
        visited[start[0]][start[1]] = true;

        while (!priorityQueue.isEmpty() && k > 0) {
            int[] current = priorityQueue.poll();
            int row = current[0], col = current[1], distance = current[2];

            // Check if the current cell is a valid item within price range
            if (grid[row][col] >= minPrice && grid[row][col] <= maxPrice) {
                result.add(Arrays.asList(row, col));
                k--;
            }

            // Explore all 4 possible directions
            for (int[] dir : DIRECTIONS) {
                int newRow = row + dir[0], newCol = col + dir[1];

                if (isValidCell(newRow, newCol, rows, cols, grid, visited)) {
                    visited[newRow][newCol] = true;
                    priorityQueue.offer(new int[]{newRow, newCol, distance + 1});
                }
            }
        }
        return result;
    }

    /**
     * Checks if the given cell is valid for traversal.
     * 
     * @param row      Row index of the cell.
     * @param col      Column index of the cell.
     * @param rows     Total number of rows in the grid.
     * @param cols     Total number of columns in the grid.
     * @param grid     The 2D grid representation.
     * @param visited  The visited boolean array.
     * @return True if the cell can be visited; otherwise, false.
     */
    private boolean isValidCell(int row, int col, int rows, int cols, int[][] grid, boolean[][] visited) {
        return row >= 0 && row < rows && col >= 0 && col < cols && grid[row][col] != 0 && !visited[row][col];
    }
}
