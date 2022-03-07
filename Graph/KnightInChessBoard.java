package Graph;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Find the min moves required by knight to move from source to destination in a chess board
 */
public class KnightInChessBoard {

    public static void main(String[] args) {
        int moves = new KnightInChessBoard().knight(8, 8, 1, 1, 8, 8);
        System.out.println("Moves required are " + moves);
    }

    int rows;
    int cols;
    int[] dx = {-2, -2, -1, -1, 2,  2, 1,  1};
    int[] dy = {-1,  1,  -2, 2, 1, -1, -2, 2};

    /**
     *
     * @param rows total number of rows in board
     * @param cols total number of cols in board
     * @param x1 starting row
     * @param y1 starting col
     * @param x2 destination row
     * @param y2 destination col
     * @return min number of moves required to move from source to destination
     */
    public int knight(int rows, int cols, int x1, int y1, int x2, int y2) {
        this.rows = rows;
        this.cols = cols;

        Queue<Coordinate> queue = new LinkedList<>();
        queue.offer(new Coordinate(x1, y1));
        boolean[][] visited = new boolean[rows+1][cols+1];
        int steps = 0;

        while(!queue.isEmpty()) {
            int size = queue.size();
            for(int i=0; i<size; i++) {
                Coordinate curr = queue.poll();
                if(visited[curr.x][curr.y]) continue;
                visited[curr.x][curr.y] = true;
                if(curr.x == x2 && curr.y == y2) return steps;

                for(int dir=0; dir<dx.length; dir++) {
                    int nextX = curr.x + dx[dir];
                    int nextY = curr.y + dy[dir];
                    if(isValid(nextX, nextY, visited)) {
                        queue.offer(new Coordinate(nextX, nextY));
                    }
                }
            }
            steps++;
        }
        return -1;

    }

    private boolean isValid(int x, int y, boolean[][] visited) {
        return x > 0 && y > 0 && x <= rows && y <= cols && !visited[x][y];
    }
}

class Coordinate {
    public int x;
    public int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
