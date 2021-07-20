package Graph;

public class MaxAreaIsland {
    public static void main(String[] args) {
        int[][] grid = {
                {0, 0, 1, 1, 0},
                {1, 0, 1, 1, 0},
                {0, 1, 0, 0, 0},
                {0, 0, 0, 0, 1}};
        System.out.println("Max area of island is " + maxAreaOfIsland(grid));
    }

    public static int maxAreaOfIsland(int[][] grid) {
        int max = 0;
        for(int i=0; i<grid.length; i++) {
            for(int j=0; j<grid[i].length; j++) {
                if(grid[i][j] == 1) {
                    max = Math.max(max, doDfs(grid, i, j));
                }
            }
        }
        return max;
    }

    public static int doDfs(int[][] grid, int i, int j) {
        if(i < 0 || i >= grid.length || j < 0 || j >= grid[i].length || grid[i][j] == 0) return 0;

        grid[i][j] = 0;
        return 1
                + doDfs(grid, i + 1, j)
                + doDfs(grid, i - 1, j)
                + doDfs(grid, i, j + 1)
                + doDfs(grid, i, j - 1);
    }
}
