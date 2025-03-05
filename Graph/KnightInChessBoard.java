package Graph;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Find the min moves required by a knight to move from source to destination in a chess board
 */
public class KnightInChessBoard {
    public static void main(String[] args) {
        int moves = new KnightInChessBoard().knight(8, 8, 1, 1, 8, 8);
        System.out.println("Moves required are " + moves);
    }

    private final int[] dx = {-2, -2, -1, -1, 2,  2, 1,  1};
    private final int[] dy = {-1,  1, -2,  2, 1, -1, -2,  2};

    /**
     * @param rows total number of rows in board
     * @param cols total number of cols in board
     * @param x1 starting row
     * @param y1 starting col
     * @param x2 destination row
     * @param y2 destination col
     * @return min number of moves required to move from source to destination
     */
    public int knight(int rows, int cols, int x1, int y1, int x2, int y2) {
        boolean[][] visited = new boolean[rows][cols]; // Use zero-based indexing
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{x1 - 1, y1 - 1, 0}); // Convert to zero-based index
        visited[x1 - 1][y1 - 1] = true;

        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            int currX = curr[0], currY = curr[1], steps = curr[2];

            if (currX == x2 - 1 && currY == y2 - 1) return steps; // Reached destination

            for (int dir = 0; dir < 8; dir++) {
                int nextX = currX + dx[dir];
                int nextY = currY + dy[dir];

                if (isValid(nextX, nextY, rows, cols, visited)) {
                    visited[nextX][nextY] = true;
                    queue.offer(new int[]{nextX, nextY, steps + 1});
                }
            }
        }
        return -1;
    }

    private boolean isValid(int x, int y, int rows, int cols, boolean[][] visited) {
        return x >= 0 && y >= 0 && x < rows && y < cols && !visited[x][y];
    }
}
