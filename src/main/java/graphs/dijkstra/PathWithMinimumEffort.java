package graphs.dijkstra;

import java.util.*;

/**
 * Problem: Path With Minimum Effort
 *
 * You are given a 2D array heights representing the heights of cells in a grid.
 * A route's effort is the maximum absolute difference in heights between two
 * consecutive cells of the route.
 *
 * Return the minimum effort required to travel from the top-left cell (0,0) to
 * the bottom-right cell (rows-1, cols-1).
 *
 * You can move up, down, left, or right (4 directions).
 *
 * Example:
 * Input: heights = [[1,2,2],[3,8,2],[5,3,5]]
 * Output: 2
 * Explanation: The route [1,2,2,5] has effort 2, which is minimum.
 * Path: (0,0)→(0,1)→(0,2)→(2,2)
 * Efforts: |1-2|=1, |2-2|=0, |2-5|=3 → max effort = 3 (not optimal)
 * Better path: (0,0)→(1,0)→(2,0)→(2,1)→(2,2) with max effort 2
 *
 * Constraints:
 * - rows == heights.length
 * - cols == heights[i].length
 * - 1 <= rows, cols <= 100
 * - 1 <= heights[i][j] <= 10^6
 *
 * LeetCode Problem: https://leetcode.com/problems/path-with-minimum-effort
 *
 * Follow-up Questions:
 *
 * 1. What if you need to return the actual path, not just the effort?
 *    Answer: Track parent pointers during Dijkstra's. After reaching destination,
 *    backtrack from end to start using parent map to reconstruct path.
 *
 * 2. How would you handle if diagonal movement is allowed?
 *    Answer: Add 4 more directions (diagonals). Calculate effort same way.
 *    Dijkstra's handles all directions uniformly.
 *
 * 3. What if there are obstacles you cannot pass through?
 *    Answer: Mark obstacle cells, skip them during BFS/Dijkstra's. Check if cell
 *    is obstacle before adding to queue/heap.
 *
 * 4. Can you find k paths with minimum efforts?
 *    Answer: Modify Dijkstra's to track k best efforts to each cell. Use
 *    priority queue with (effort, row, col, pathId). More complex state tracking.
 *
 * 5. How would you handle if some cells have negative heights?
 *    Answer: Use absolute difference same way. Dijkstra's still works because
 *    edge weights (differences) remain non-negative.
 */
public class PathWithMinimumEffort {

    private static final int[][] DIRECTIONS = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

    /**
     * Inner class to represent a cell in the grid with its effort.
     * Used in Dijkstra's algorithm to improve readability over using int arrays.
     */
    private static class Cell implements Comparable<Cell> {
        int effort;
        int row;
        int col;

        Cell(int effort, int row, int col) {
            this.effort = effort;
            this.row = row;
            this.col = col;
        }

        @Override
        public int compareTo(Cell other) {
            return Integer.compare(this.effort, other.effort);
        }
    }

    /**
     * Finds minimum effort path using Dijkstra's algorithm with min-heap.
     *
     * Algorithm:
     * 1. Use priority queue (min-heap) to process cells by minimum effort
     * 2. Track minimum effort to reach each cell in effort array
     * 3. For each cell, explore all 4 neighbors:
     *    - Calculate new effort = max(current effort, height difference to neighbor)
     *    - If new effort < neighbor's recorded effort, update and add to queue
     * 4. Return effort to reach bottom-right cell
     *
     * Key insight: This is a shortest path problem where edge weight is the height
     * difference. But we track MAXIMUM difference along path (not sum). Dijkstra's
     * still works because we greedily process minimum effort paths first.
     *
     * Time Complexity: O(M * N * log(M * N)) where M=rows, N=cols.
     * Each cell added to heap at most once, heap operations are O(log size).
     *
     * Space Complexity: O(M * N) for effort array and heap.
     *
     * @param heights 2D array of cell heights
     * @return minimum effort required to reach bottom-right from top-left
     */
    public int minimumEffortPath(int[][] heights) {
        int rows = heights.length;
        int cols = heights[0].length;

        // Track minimum effort to reach each cell
        int[][] effort = new int[rows][cols];
        for (int[] row : effort) {
            Arrays.fill(row, Integer.MAX_VALUE);
        }
        effort[0][0] = 0;

        // Min-heap: prioritized by effort
        PriorityQueue<Cell> minHeap = new PriorityQueue<>();
        minHeap.offer(new Cell(0, 0, 0));

        while (!minHeap.isEmpty()) {
            Cell current = minHeap.poll();

            // Already found better path to this cell
            if (current.effort > effort[current.row][current.col]) {
                continue;
            }

            // Reached destination
            if (current.row == rows - 1 && current.col == cols - 1) {
                return current.effort;
            }

            // Explore all 4 neighbors
            for (int[] dir : DIRECTIONS) {
                int newRow = current.row + dir[0];
                int newCol = current.col + dir[1];

                // Check boundaries
                if (newRow < 0 || newRow >= rows || newCol < 0 || newCol >= cols) {
                    continue;
                }

                // Calculate effort to reach neighbor
                int heightDiff = Math.abs(heights[current.row][current.col] - heights[newRow][newCol]);
                int newEffort = Math.max(current.effort, heightDiff);

                // Update if found better path
                if (newEffort < effort[newRow][newCol]) {
                    effort[newRow][newCol] = newEffort;
                    minHeap.offer(new Cell(newEffort, newRow, newCol));
                }
            }
        }

        return effort[rows - 1][cols - 1];
    }
}
