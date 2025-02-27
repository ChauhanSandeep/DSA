package Graph;

public class MaxAreaIsland {
    private static final int[][] DIRS = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}}; // Directions: down, up, right, left

    public static void main(String[] args) {
        int[][] grid = {
            {0, 0, 1, 1, 0},
            {1, 0, 1, 1, 0},
            {0, 1, 0, 0, 0},
            {0, 0, 0, 0, 1}
        };
        System.out.println("Max area of island is " + maxAreaOfIsland(grid));
    }

    public static int maxAreaOfIsland(int[][] grid) {
        int max = 0;
        boolean[][] visited = new boolean[grid.length][grid[0].length];

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == 1 && !visited[i][j]) {
                    max = Math.max(max, dfs(grid, visited, i, j));
                }
            }
        }
        return max;
    }

    private static int dfs(int[][] grid, boolean[][] visited, int i, int j) {
        if (i < 0 || i >= grid.length || j < 0 || j >= grid[i].length || grid[i][j] == 0 || visited[i][j]) {
            return 0;
        }

        visited[i][j] = true;
        int area = 1;
        for (int[] dir : DIRS) {
            area += dfs(grid, visited, i + dir[0], j + dir[1]);
        }
        return area;
    }
}