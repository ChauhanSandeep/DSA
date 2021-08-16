package Graph;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

/**
 * Find the shortest bridge that can be created between 2 islands consisting of 1's
 */
public class ShortestBridge {

    int[][] directions = {
            {-1, 0},
            {1,  0},
            {0, -1},
            {0,  1}
    };

    public static void main(String[] args) {
        int[][] grid = {
                {0,  1},
                {1,  0}};
        int result = new ShortestBridge().shortestBridge(grid);
        System.out.println(result);
    }

    public int shortestBridge(int[][] grid) {
        Queue<Pair> queue = new LinkedList<>();
        Set<Pair> visited = new HashSet<>();

        outer:
        for(int i=0; i<grid.length; i++) {
            for(int j=0; j<grid[i].length; j++) {
                if(grid[i][j] == 1) {
                    dfs(grid, i, j, visited, queue);
                    break outer;
                }
            }
        }
        int level = 0;
        while(!queue.isEmpty()) {
            int size = queue.size();
            for(int i=0; i<size; i++) {
                Pair current = queue.poll();
                for(int[] direction: directions) {
                    int newX = current.row + direction[0];
                    int newY = current.col + direction[1];
                    Pair next = new Pair(newX, newY);
                    if (newX >= 0 && newX < grid.length && newY >= 0 && newY < grid[newX].length && !visited.contains(next)) {
                        if (grid[newX][newY] == 0) {
                            visited.add(next);
                            queue.offer(next);
                        } else {
                            return level;
                        }
                    }
                }
            }
            level++;
        }
        return -1;
    }

    public void dfs(int[][] grid, int i, int j, Set<Pair> visited, Queue<Pair> queue) {
        if(i<0 || j < 0 || i >= grid.length || j >= grid[i].length || grid[i][j] == 0 || visited.contains(new Pair(i, j))) return;
        Pair current = new Pair(i, j);
        queue.offer(current);
        visited.add(current);

        dfs(grid, i+1, j, visited, queue);
        dfs(grid, i, j+1, visited, queue);
        dfs(grid, i-1, j, visited, queue);
        dfs(grid, i, j-1, visited, queue);
    }

}


class Pair {
    int row;
    int col;

    public Pair(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public boolean equals(Object o) {
        return o instanceof Pair &&((Pair) o).row == this.row &&  ((Pair) o).col == this.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}