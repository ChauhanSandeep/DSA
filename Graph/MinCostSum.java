package Graph;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * You are given `rows`, `cols` and matrix. Every cell in `matrix` has a character U,R,L or D indicating up,right,left and down.
 *
 * Your target is to go from top left corner to the bottom right corner of the matrix.
 *
 * But there are some restrictions while moving along the matrix:
 * 1. If you follow what is written in the cell then you can move freely.
 * 2. But if you don't follow what is written in the cell then you have to pay 1 unit of cost.
 *
 * So your task is to find the minimum cost to go from top-left corner to the bottom-right corner.
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
        System.out.println(new MinCostSum().solve(rows, cols, matrix));
    }

    //          R  D  L   U
    int[] dx = {0, 1, 0, -1};
    int[] dy = {1, 0, -1, 0};

    public int solve(int rows, int cols, String[] matrix) {

        String directions = "RDLU";
        int[][] dist = new int[rows][cols];
        for (int[] list : dist) {
            Arrays.fill(list, Integer.MAX_VALUE - 1);
        }
        PriorityQueue<Point> queue = new PriorityQueue<>(Comparator.comparingInt(a -> a.cost));
        queue.offer(new Point(0, 0, 0));
        dist[0][0] = 0;

        while (!queue.isEmpty()) {
            Point currPoint = queue.poll();
            if (currPoint.x == rows - 1 && currPoint.y == cols - 1) {
                break;
            }

            for (int i = 0; i < 4; i++) {
                //new position
                int newX = currPoint.x + dx[i];
                int newY = currPoint.y + dy[i];

                int cost = dist[currPoint.x][currPoint.y];
                // increase cost by 1 (if path not matches)
                if(directions.charAt(i) != matrix[currPoint.x].charAt(currPoint.y)) {
                    cost++;
                }

                //check boundary condition and cost
                if ((newX >= 0 && newY >= 0 && newX < rows && newY < cols) && cost < dist[newX][newY]) {
                    queue.offer(new Point(newX, newY, cost));
                    dist[newX][newY] = cost;
                }

            }
        }
        System.out.println(Arrays.deepToString(dist));
        return dist[rows-1][cols-1];
    }
}

class Point {
    int x;
    int y;
    int cost;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
        this.cost = 0;
    }

    public Point(int x, int y, int cost) {
        this.x = x;
        this.y = y;
        this.cost = cost;
    }
}
