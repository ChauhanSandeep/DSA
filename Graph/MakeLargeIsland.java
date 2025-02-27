package Graph;

import java.util.*;

public class MakeLargeIsland {
    public static void main(String[] args) {
        int[][] grid = {
                {1, 1, 1},
                {0, 1, 0},
                {0, 0, 1}};
        int largestIsland = new MakeLargeIsland().largestIsland(grid);
        System.out.println("Largest island after flipping one 0 is " + largestIsland);
    }

    public int largestIsland(int[][] grid) {
        int n = grid.length;
        int islandId = 2;  // Unique ID for each island
        int maxIslandSize = 0;
        Map<Integer, Integer> islandSizeMap = new HashMap<>();

        // First Pass: Label islands & store sizes
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    int size = dfs(grid, i, j, islandId);
                    islandSizeMap.put(islandId, size);
                    maxIslandSize = Math.max(maxIslandSize, size);
                    islandId++;
                }
            }
        }

        // Second Pass: Try flipping each `0`
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 0) {
                    maxIslandSize = Math.max(maxIslandSize, 1 + computeNewIslandSize(grid, i, j, islandSizeMap));
                }
            }
        }
        return maxIslandSize;
    }

    private int dfs(int[][] grid, int i, int j, int islandId) {
        int n = grid.length;
        Stack<int[]> stack = new Stack<>();
        stack.push(new int[]{i, j});
        grid[i][j] = islandId;
        int size = 0;

        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        while (!stack.isEmpty()) {
            int[] cell = stack.pop();
            int x = cell[0], y = cell[1];
            size++;

            for (int[] dir : directions) {
                int newX = x + dir[0], newY = y + dir[1];
                if (newX >= 0 && newX < n && newY >= 0 && newY < n && grid[newX][newY] == 1) {
                    grid[newX][newY] = islandId;
                    stack.push(new int[]{newX, newY});
                }
            }
        }
        return size;
    }

    private int computeNewIslandSize(int[][] grid, int i, int j, Map<Integer, Integer> islandSizeMap) {
        int n = grid.length;
        Set<Integer> uniqueIslands = new HashSet<>();
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        for (int[] dir : directions) {
            int newX = i + dir[0], newY = j + dir[1];
            if (newX >= 0 && newX < n && newY >= 0 && newY < n && grid[newX][newY] > 1) {
                uniqueIslands.add(grid[newX][newY]);
            }
        }

        int newSize = 0;
        for (int islandId : uniqueIslands) {
            newSize += islandSizeMap.get(islandId);
        }
        return newSize;
    }
}
