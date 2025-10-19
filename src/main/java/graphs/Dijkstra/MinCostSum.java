package graphs.Dijkstra;

import java.util.*;

/**
 * 1368. Minimum Cost to Make at Least One Valid Path in a Grid
 *
 * Problem:
 * You're given a grid of size `rows x cols`, where each cell contains a direction: 'R', 'L', 'U', or 'D'.
 * You can move from a cell to an adjacent cell **only** if it's in the direction specified — otherwise, you must
 * pay a cost of `1` to change the direction. Find the **minimum total cost** to go from the top-left (0,0) to
 * the bottom-right (rows-1, cols-1).
 *
 * Example:
 * Input:
 * rows = 3, cols = 3
 * matrix = ["RRR", "DDD", "UUU"]
 * Output: 2
 *
 * Explanation:
 * - Start at (0,0) → Go right (free) → right (free) → right (cost 1, wrong direction) → down (free) → down (free)
 *   → down (cost 1, wrong direction) → reached (2,2)
 * - Total cost = 2
 *
 * Approach:
 * - Apply Dijkstra's Algorithm using a Min-Heap (PriorityQueue).
 * - Track cost to reach each cell, update if a cheaper path is found.
 * - If moving in the expected direction, no cost is added; else, +1 cost.
 *
 * Time Complexity: O(R * C * log(R * C)) → Grid traversal with PQ operations.
 * Space Complexity: O(R * C) → For distance tracking and visited management.
 *
 * Follow-up Questions:
 * - Can you solve it with 0-1 BFS instead of Dijkstra? (Yes) → [0-1 BFS](https://leetcode.com/problems/minimum-cost-to-make-at-least-one-valid-path-in-a-grid/editorial/)
 * - What if diagonal directions are added? (Extend direction arrays accordingly)
 */
public class MinCostSum {
    public static void main(String[] args) {
        int rows = 3;
        int cols = 3;
        String[] matrix = {
                "RRR",
                "DDD",
                "UUU"
        };
        System.out.println("Minimum cost: " + new MinCostSum().minCostPath(rows, cols, matrix));
    }

    // Directions: Right, Down, Left, Up
    private static final int[] dx = {0, 1, 0, -1};
    private static final int[] dy = {1, 0, -1, 0};
    private static final String directions = "RDLU";

    /**
     * Uses Dijkstra's algorithm to compute the minimum cost from (0,0) to (rows-1, cols-1).
     *
     * Steps:
     * 1. Initialize distance matrix with Integer.MAX_VALUE
     * 2. Use a min-heap to always process the next lowest-cost cell
     * 3. For each neighbor of current cell, calculate cost:
     *    - 0 if direction matches expected
     *    - 1 if direction needs to change
     * 4. Relax distance and push into heap if new cost is better
     *
     * Time Complexity: O(R * C * log(R * C))
     * Space Complexity: O(R * C)
     */
    public int minCostPath(int rows, int cols, String[] matrix) {
        int[][] dist = new int[rows][cols];
        for (int[] row : dist) Arrays.fill(row, Integer.MAX_VALUE);

        // Min-heap based on accumulated cost
        PriorityQueue<Point> queue = new PriorityQueue<>(Comparator.comparingInt(p -> p.costSoFar));
        queue.offer(new Point(0, 0, 0));
        dist[0][0] = 0;

        while (!queue.isEmpty()) {
            // SELECT : Get the point with the lowest cost
            Point curr = queue.poll();
            int x = curr.x, y = curr.y, cost = curr.costSoFar;

            // MARK(*): Skip if we already found a better path
            if (cost > dist[x][y]) continue;

            // WORK: Goal check
            if (x == rows - 1 && y == cols - 1) return cost;

            // ADD(*): Add neighbors with lower costs
            for (int i = 0; i < 4; i++) {
                int newX = x + dx[i];
                int newY = y + dy[i];

                // Bounds check
                if (newX < 0 || newY < 0 || newX >= rows || newY >= cols) continue;

                char flowDir = matrix[x].charAt(y);
                char moveDir = directions.charAt(i);
                int newCost = cost + (flowDir == moveDir ? 0 : 1);

                // Only push if it's a better path
                if (newCost < dist[newX][newY]) {
                    dist[newX][newY] = newCost;
                    queue.offer(new Point(newX, newY, newCost));
                }
            }
        }

        return -1; // Should never hit this
    }

    /** Helper Class for Priority Queue */
    static class Point {
        int x, y, costSoFar;
        public Point(int x, int y, int costSoFar) {
            this.x = x;
            this.y = y;
            this.costSoFar = costSoFar;
        }
    }
}
