package Graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Find min distance from top left to bottom right in binary matrix.
 * Given that path can be created by 0s only. Traversal to all 8 adjacent block is allowed
 * If there is no path possible return -1
 *
 * https://leetcode.com/problems/shortest-path-in-binary-matrix/
 */
public class ShortestPath {

  public int[][] directions = {{-1, -1},{-1, 0},{-1, 1},{0, -1},{0, 1},{1, -1},{1, 0},{1, 1}};
  public static void main(String[] args) {
    int[][] grid = {
        {0,0,0},
        {1,0,0},
        {1,1,0}
    };
    int shortestPath = new ShortestPath().shortestPathBinaryMatrix(grid);
    System.out.println(shortestPath);
  }

  public int shortestPathBinaryMatrix(int[][] grid) {
    if(grid == null || grid.length == 0 || grid[0].length == 0 || grid[0][0] == 1) return -1;

    int rows = grid.length;
    int cols = grid[0].length;

    // initial setup
    Queue<int[]> queue = new LinkedList<>();
    boolean[][] visited = new boolean[grid.length][grid[0].length];
    queue.offer(new int[] {0, 0});

    // BFS traversal
    int ans = 1;
    while(!queue.isEmpty()) {
      int size = queue.size();
      for(int i=0; i<size; i++) {
        int[] curr = queue.poll();
        if(curr[0] == rows-1 && curr[1] == cols - 1) return ans;
        if(visited[curr[0]][curr[1]]) continue;
        visited[curr[0]][curr[1]] = true;

        for(int[] direction: directions) {
          int newX = curr[0] + direction[0];
          int newY = curr[1] + direction[1];
          if(newX >= 0 && newY >= 0 && newX < rows && newY < cols && grid[newX][newY] == 0 && !visited[newX][newY]) {
            queue.offer(new int[] {newX, newY});
          }
        }
      }
      ans++;
    }
    return -1;
  }


}
