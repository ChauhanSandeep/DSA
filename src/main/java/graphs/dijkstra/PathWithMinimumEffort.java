package graphs.dijkstra;

import java.util.*;

/**
 * Problem: Path With Minimum Effort
 *
 * Given a grid of heights, a path's effort is the maximum absolute height
 * difference across any one step on that path. Return the minimum possible effort
 * from the top-left cell to the bottom-right cell.
 *
 * Leetcode: https://leetcode.com/problems/path-with-minimum-effort/ (Medium)
 * Rating:   1948 (zerotrac Elo)
 * Pattern:  Graph | Dijkstra on grid | Minimize maximum edge weight
 *
 * Example:
 *   Input:  heights = [[1,2,2],[3,8,2],[5,3,5]]
 *   Output: 2
 *   Why:    the route through 1,3,5,3,5 never uses an edge with difference greater than 2.
 *
 * Follow-ups:
 *   1. Solve without Dijkstra?
 *      Binary search the answer and BFS/DFS through edges whose difference is within the limit.
 *   2. Return the actual path?
 *      Store parent cells when an effort value improves, then reconstruct at the destination.
 *   3. Allow diagonal movement?
 *      Add four diagonal directions; the effort relaxation stays the same.
 *
 * Related: Minimum Cost to Make a Valid Path (1368), Swim in Rising Water (778).
 */
public class PathWithMinimumEffort {

    public static void main(String[] args) {
        PathWithMinimumEffort solver = new PathWithMinimumEffort();
        int[][] heights1 = {{1, 2, 2}, {3, 8, 2}, {5, 3, 5}};
        int[][] heights2 = {{7}};

        System.out.printf("heights=%s -> %d  expected=2%n",
            Arrays.deepToString(heights1), solver.minimumEffortPath(heights1));
        System.out.printf("heights=%s -> %d  expected=0%n",
            Arrays.deepToString(heights2), solver.minimumEffortPath(heights2));
    }

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
     * Intuition: treat each step's height difference as an edge cost, but a path's
     * score is the maximum edge on it rather than a sum. Dijkstra still works when
     * relaxation uses max(current effort, next edge effort), because efforts only
     * stay the same or increase along a path.
     *
     * Algorithm:
     *   1. Fill the effort matrix with infinity and seed the start with 0.
     *   2. Pop the cell with the smallest current effort from the min-heap.
     *   3. For each neighbor, compute the max effort needed to extend the path there.
     *   4. If that improves the neighbor, record it and push the neighbor into the heap.
     *
     * Time:  O(rows * cols * log(rows * cols)) - heap operations over grid cells.
     * Space: O(rows * cols) - effort matrix and heap entries.
     *
     * @param heights grid of cell heights
     * @return minimum possible effort from top-left to bottom-right
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
