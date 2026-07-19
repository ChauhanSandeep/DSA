package graphs.dijkstra;

import java.util.*;

    /**
     * Intuition: each cell-to-neighbor move has cost 0 when it follows the cell's
     * arrow and cost 1 otherwise. That makes the grid a weighted graph with
     * non-negative edges, so Dijkstra can safely finalize the cheapest cell first.
     *
     * Algorithm:
     *   1. Initialize dist with infinity and seed (0,0) with cost 0.
     *   2. Pop the lowest-cost point from the priority queue.
     *   3. For each of 4 directions, compute whether changing the current arrow adds 0 or 1.
     *   4. Relax the neighbor and push it when a lower cost is found.
     *
     * Time:  O(rows * cols * log(rows * cols)) - each cell relaxation uses the heap.
     * Space: O(rows * cols) - distance matrix and priority queue storage.
     *
     * @param rows number of rows in matrix
     * @param cols number of columns in matrix
     * @param matrix direction grid using R, D, L, and U
     * @return minimum arrow-change cost from top-left to bottom-right
     */
public class MinCostSum {
    public static void main(String[] args) {
        MinCostSum solver = new MinCostSum();
        String[] matrix1 = {"RRR", "DDD", "UUU"};
        String[] matrix2 = {"R"};

        System.out.printf("matrix=%s -> %d  expected=1%n",
            Arrays.toString(matrix1), solver.minCostPath(3, 3, matrix1));
        System.out.printf("matrix=%s -> %d  expected=0%n",
            Arrays.toString(matrix2), solver.minCostPath(1, 1, matrix2));
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
        for (int[] row : dist) {
            Arrays.fill(row, Integer.MAX_VALUE);
        } 

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
