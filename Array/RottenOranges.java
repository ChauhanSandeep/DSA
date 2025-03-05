package Array;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Given a grid containing oranges, each rotten orange rots its neighbor in one minute.
 * Find in how many minutes all the oranges will be rotten.
 */
public class RottenOranges {

    public static void main(String[] args) {
        int[][] grid = {{2,1,1},{1,1,0},{0,1,1}};
        int timeElapsed = new RottenOranges().orangesRotting(grid);
        System.out.println("Time elapsed to rot all oranges: " + timeElapsed);
    }

    private static final int FRESH = 1;
    private static final int ROTTEN = 2;

    public int orangesRotting(int[][] grid) {
        Queue<int[]> queue = new LinkedList<>();
        int freshOranges = 0;
        int rows = grid.length, cols = grid[0].length;

        // Step 1: Add all rotten oranges to queue & count fresh oranges
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (grid[row][col] == ROTTEN) {
                    queue.offer(new int[]{row, col});
                } else if (grid[row][col] == FRESH) {
                    freshOranges++;
                }
            }
        }

        // If there are no fresh oranges, return 0 immediately
        if (freshOranges == 0) return 0;

        int minutesElapsed = 0;
        int[][] directions = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

        // Step 2: BFS Processing
        while (!queue.isEmpty()) {
            int size = queue.size();
            boolean rotted = false;

            for (int i = 0; i < size; i++) {
                int[] orange = queue.poll();
                int row = orange[0], col = orange[1];

                for (int[] dir : directions) {
                    int newRow = row + dir[0];
                    int newCol = col + dir[1];

                    if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols && grid[newRow][newCol] == FRESH) {
                        grid[newRow][newCol] = ROTTEN;
                        queue.offer(new int[]{newRow, newCol});
                        freshOranges--;
                        rotted = true;
                    }
                }
            }

            // Only increment time if at least one orange was rotted
            if (rotted) minutesElapsed++;
        }

        return freshOranges == 0 ? minutesElapsed : -1;
    }
}
