package Array;


import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Queue;

public class RottenOranges {

    public static void main(String[] args) {
        int[][] grids = {{2,1,1},{1,1,0},{0,1,1}};
        int timeElapse = new RottenOranges().orangesRotting(grids);
        System.out.println("Time elapsed to rot all oranges is " + timeElapse);
    }

    private static final int FRESH = 1;
    private static final int ROTTEN = 2;

    public int orangesRotting(int[][] grid) {
        Queue<Coordinate> queue = new LinkedList<>(); // contains Rotten oranges
        int freshOranges = 0;
        int rows = grid.length, cols = grid[0].length;

        for (int row = 0; row < rows; ++row) {
            for (int col = 0; col < cols; ++col) {
                if (grid[row][col] == ROTTEN) queue.offer(new Coordinate(row, col));
                else if (grid[row][col] == FRESH) freshOranges++;
            }
        }

        // marks start of new level in Graph
        queue.offer(new Coordinate(-1, -1));

        // Step 2). start the rotting process via BFS
        int minutesElapsed = -1;
        int[][] directions = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

        while (!queue.isEmpty()) {
            Coordinate coordinate = queue.poll();
            int row = coordinate.getRow();
            int col = coordinate.getCol();
            if (row == -1) {
                // One level of graph finished
                minutesElapsed++;
                if (!queue.isEmpty()) {
                    queue.offer(new Coordinate(-1, -1));
                }
            } else {
                // this is a rotten orange
                // then it would contaminate its neighbors
                for (int[] dir : directions) {
                    int neighborRow = row + dir[0];
                    int neighborCol = col + dir[1];
                    if (neighborRow >= 0 && neighborRow < rows && neighborCol >= 0 && neighborCol < cols) {
                        if (grid[neighborRow][neighborCol] == FRESH) {
                            grid[neighborRow][neighborCol] = ROTTEN;
                            freshOranges--;
                            // this orange would then contaminate other oranges
                            queue.offer(new Coordinate(neighborRow, neighborCol));
                        }
                    }
                }
            }
        }

        // return elapsed minutes if no fresh orange left
        return freshOranges == 0 ? minutesElapsed : -1;
    }
}

class Coordinate {
    int row;
    int col;

    public Coordinate(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
