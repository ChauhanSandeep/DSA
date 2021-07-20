package Graph;

public class NumberOfIslands {

    public static void main(String[] args) {
        char[][] grid = {
                {'1', '1', '1', '1', '0'},
                {'1', '1', '0', '1', '0'},
                {'0', '0', '1', '0', '0'},
                {'0', '0', '0', '1', '1'}
        };
        System.out.println("Number of islands are " + numIslands(grid));
    }

    /**
     * Given a grid of 0 and 1. Find the number of Islands created by 1s
     * Islands is created of adjacent 1s (left, right, top, bottom)
     * @param grid
     * @return
     */
    public static int numIslands(char[][] grid) {
        int count = 0;
        if(grid == null || grid.length == 0) return count;

        for(int i=0; i<grid.length; i++) {
            for(int j=0; j<grid[i].length; j++) {
                if(grid[i][j] == '1') {
                    count++;
                    doDfs(grid, i, j);
                }
            }
        }
        return count;
    }

    public static void doDfs(char[][] grid, int i, int j) {
        if(i < 0 || i >= grid.length || j < 0 || j >= grid[i].length || grid[i][j] != '1'){
            return;
        }
        grid[i][j] = 0;
        doDfs(grid, i+1, j);
        doDfs(grid, i-1, j);
        doDfs(grid, i, j+1);
        doDfs(grid, i, j-1);
    }
}
