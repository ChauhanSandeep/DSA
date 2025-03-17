package Graph;

import java.util.*;

/**
 * Min Cost to Reach Bottom-Right in a Grid
 * 
 * Approach:
 * - Use **Dijkstra's Algorithm** to find the minimum cost path from (0,0) to (rows-1,cols-1).
 * - **Priority Queue (Min-Heap)** ensures optimal selection of minimum cost paths.
 * - Each movement in the given direction costs **0**, while changing direction costs **1**.
 * 
 * Time Complexity: **O(R * C * log(R * C))** (Dijkstra’s Algorithm with Min-Heap)
 * Space Complexity: **O(R * C)** (For distance matrix & priority queue)
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
     * Finds the minimum cost to reach the bottom-right cell from (0,0).
     *
     * @param rows   Number of rows in the grid.
     * @param cols   Number of columns in the grid.
     * @param matrix Character matrix representing directions.
     * @return Minimum cost required to reach (rows-1, cols-1).
     */
    public int minCostPath(int rows, int cols, String[] matrix) {
        int[][] dist = new int[rows][cols];
        for (int[] row : dist) Arrays.fill(row, Integer.MAX_VALUE);

        PriorityQueue<Point> queue = new PriorityQueue<>(Comparator.comparingInt(a -> a.cost));
        queue.offer(new Point(0, 0, 0));
        dist[0][0] = 0;

        while (!queue.isEmpty()) {
            Point curr = queue.poll();
            int x = curr.x, y = curr.y, cost = curr.cost;

            // If we reached the bottom-right corner, return the cost
            if (x == rows - 1 && y == cols - 1) return cost;

            // Explore all 4 possible directions
            for (int i = 0; i < 4; i++) {
                int newX = x + dx[i];
                int newY = y + dy[i];

                // Check if within grid bounds
                if (newX < 0 || newY < 0 || newX >= rows || newY >= cols) continue;

                // If moving in the given direction, cost remains same; otherwise, +1
                char expectedDir = matrix[x].charAt(y);
                char moveDir = directions.charAt(i);
                int newCost = cost + (expectedDir == moveDir ? 0 : 1);

                // Relaxation step: Update distance only if we found a better path
                if (newCost < dist[newX][newY]) {
                    dist[newX][newY] = newCost;
                    queue.offer(new Point(newX, newY, newCost));
                }
            }
        }

        return -1;  // Should never reach here
    }

    /** Helper Class for Priority Queue */
    static class Point {
        int x, y, cost;
        public Point(int x, int y, int cost) {
            this.x = x;
            this.y = y;
            this.cost = cost;
        }
    }
}
