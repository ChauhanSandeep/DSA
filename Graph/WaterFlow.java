package Graph;

import java.util.Arrays;

/**
 * Find number of nodes from where water can flow to top-left and bottom-right boundary
 * water can only flow from higher number to lower number.
 *
 * https://www.interviewbit.com/problems/water-flow/
 */
public class WaterFlow {

    public static void main(String[] args) {
        int[][] grid = {
                {1, 2, 2, 3, 5},
                {3, 2, 3, 4, 4},
                {2, 4, 5, 3, 1},
                {6, 7, 1, 4, 5},
                {5, 1, 1, 2, 4},
        };
        System.out.println(new WaterFlow().solve(grid));
    }

    /**
     * [[false, false, false, false, true],
     * [false, false, false, true, true],
     * [false, false, false, true, true],
     * [true, true, false, true, true],
     * [true, true, true, true, true]]
     *
     * [[true, true, true, true, true],
     * [true, false, false, false, false],
     * [true, false, false, false, false],
     * [true, false, false, false, false],
     * [true, false, false, false, false]]
     */

    int rows;
    int cols;
    int[][] dirs = {
            {-1, 0},
            {0, -1},
            {1, 0},
            {0, 1}
    };

    public int solve(int[][] grid) {
        if(grid == null || grid.length == 0 || grid[0].length == 0) return 0;
        rows = grid.length;
        cols = grid[0].length;

        boolean[][] visited1 = new boolean[rows][cols]; // top-left
        boolean[][] visited2 = new boolean[rows][cols]; // bottom-right

        for(int i=0; i<rows; i++) {
            dfs(grid, visited1, i, 0, Integer.MIN_VALUE);       // left
            dfs(grid, visited2, i, cols-1, Integer.MIN_VALUE);  // right
        }

        for(int j=0; j<cols; j++) {
            dfs(grid, visited1, 0, j, Integer.MIN_VALUE);       // top
            dfs(grid, visited2, rows-1, j, Integer.MIN_VALUE);  // bottom
        }

        int count = 0;
        for(int i=0; i<rows; i++) {
            for(int j=0; j<cols; j++) {
                if(visited1[i][j] && visited2[i][j]) count++;
            }
        }

        return count;
    }

    private void dfs(int[][] grid, boolean[][] visited, int i, int j, int prev) {
        if(i<0 || j<0 || i>= grid.length || j>=grid[0].length || visited[i][j] || grid[i][j] < prev) return;

        visited[i][j] = true;
        int curr = grid[i][j];
        for(int[] dir: dirs) {
            int ni = i + dir[0];
            int nj = j + dir[1];
            dfs(grid, visited, ni, nj, curr);
        }
    }
}
