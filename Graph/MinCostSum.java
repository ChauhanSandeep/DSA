package Graph;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

public class MinCostSum {
    public static void main(String[] args) {
        int rows = 3;
        int cols = 3;
        String[] matrix = {
                "RRR",
                "DDD",
                "UUU"
        };
        System.out.println(new MinCostSum().solve(rows, cols, matrix));
    }

    // Directions: Right, Down, Left, Up
    private static final int[] dx = {0, 1, 0, -1};
    private static final int[] dy = {1, 0, -1, 0};
    private static final String directions = "RDLU";

    public int solve(int rows, int cols, String[] matrix) {
        int[][] dist = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            Arrays.fill(dist[i], Integer.MAX_VALUE);
        }

        PriorityQueue<Point> queue = new PriorityQueue<>(Comparator.comparingInt(a -> a.cost));
        queue.offer(new Point(0, 0, 0));
        dist[0][0] = 0;

        while (!queue.isEmpty()) {
            Point curr = queue.poll();
            int currX = curr.x, currY = curr.y, currCost = curr.cost;

            // If we reached the bottom-right corner, return the cost
            if (currX == rows - 1 && currY == cols - 1) return currCost;

            for (int i = 0; i < 4; i++) {
                int newX = currX + dx[i];
                int newY = currY + dy[i];

                // Check boundaries
                if (newX < 0 || newY < 0 || newX >= rows || newY >= cols) continue;

                // Corrected Cost Calculation
                int newCost = currCost + (matrix[currX].charAt(currY) != directions.charAt(i) ? 1 : 0);

                // If we found a shorter path, update and push to queue
                if (newCost < dist[newX][newY]) {
                    dist[newX][newY] = newCost;
                    queue.offer(new Point(newX, newY, newCost));
                }
            }
        }

        return dist[rows - 1][cols - 1];
    }

    static class Point {
        int x, y, cost;
        public Point(int x, int y, int cost) {
            this.x = x;
            this.y = y;
            this.cost = cost;
        }
    }
}
