package Graph;

public class NumberOfIslands {

    public static void main(String[] args) {
        char[][] grid = {
                {'1', '1', '1', '1', '0'},
                {'1', '1', '0', '1', '0'},
                {'0', '0', '1', '0', '0'},
                {'0', '0', '0', '1', '1'}
        };
        System.out.println("Number of islands: " + numIslands(grid));
    }

    /**
     * Given a grid of 0s and 1s, find the number of islands formed by adjacent 1s.
     * Islands are formed by vertically and horizontally connected 1s.
     */
    public static int numIslands(char[][] grid) {
        if (grid == null || grid.length == 0) return 0;

        int count = 0;
        int rows = grid.length, cols = grid[0].length;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == '1') {
                    count++;
                    dfs(grid, i, j);
                }
            }
        }
        return count;
    }

    // DFS traversal to mark the entire island as visited
    private static void dfs(char[][] grid, int i, int j) {
        int rows = grid.length, cols = grid[0].length;
        if (i < 0 || j < 0 || i >= rows || j >= cols || grid[i][j] != '1') {
            return;
        }

        grid[i][j] = '0'; // Mark as visited

        // Explore 4 possible directions
        int[] dx = {0, 0, 1, -1};
        int[] dy = {1, -1, 0, 0};
        for (int d = 0; d < 4; d++) {
            dfs(grid, i + dx[d], j + dy[d]);
        }
    }
}
