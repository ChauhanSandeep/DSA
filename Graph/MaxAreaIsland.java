import java.util.LinkedList;
import java.util.Queue;

private static int bfs(int[][] grid, int i, int j, int[][] directions) {
    Queue<int[]> queue = new LinkedList<>();
    queue.offer(new int[]{i, j});
    int area = 0;
    grid[i][j] = 0; // Mark visited

    while (!queue.isEmpty()) {
        int[] cell = queue.poll();
        area++;

        for (int[] dir : directions) {
            int newX = cell[0] + dir[0];
            int newY = cell[1] + dir[1];

            if (newX >= 0 && newX < grid.length && newY >= 0 && newY < grid[0].length && grid[newX][newY] == 1) {
                queue.offer(new int[]{newX, newY});
                grid[newX][newY] = 0; // Mark visited
            }
        }
    }
    return area;
}
