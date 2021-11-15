package Graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * You are given an n x n binary matrix grid. You are allowed to change at most one 0 to be 1.
 * Return the size of the largest island in grid after applying this operation.
 * An island is a 4-directionally connected group of 1s.
 */
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
        int id = 2;
        int result = 0;
        Map<Integer, Integer> map = new HashMap<>(); // <id, size>
        // 1. mark island with id
        for(int i=0; i<grid.length; i++) {
            for(int j= 0; j<grid[i].length; j++){
                if(grid[i][j] == 1) {
                    int currSize = findSize(grid, i, j, id);
                    result = Math.max(currSize, result);
                    map.put(id, currSize);
                    id++;
                }
            }
        }
        // 2. connect island
        for(int i=0; i<grid.length; i++) {
            for(int j=0; j<grid[i].length; j++) {
                if(grid[i][j] == 0) {
                    int tentative = 1 + findConnected(grid, i, j, map);
                    result = Math.max(result, tentative);
                }
            }
        }
        return result;
    }

    private int findSize(int[][] grid, int i, int j, int id) {
        if(i<0 || i>=grid.length || j<0 || j>=grid[i].length || grid[i][j] != 1) {
            return 0;
        }
        grid[i][j] = id;
        return 1 + findSize(grid, i + 1, j, id) + findSize(grid, i, j + 1, id)
                + findSize(grid, i - 1, j, id) + findSize(grid, i, j - 1, id);
    }

    private int findConnected(int[][] grid, int i, int j, Map<Integer, Integer> map) {
        int connectedSize = 0;
        Set<Integer> connectedIds = new HashSet<>();

        if(i>0) connectedIds.add(grid[i - 1][j]);
        if(i<grid.length - 1) connectedIds.add(grid[i + 1][j]);
        if(j>0) connectedIds.add(grid[i][j - 1]);
        if(j<grid[i].length - 1) connectedIds.add(grid[i][j + 1]);

        for(Integer id: connectedIds) {
            connectedSize += map.getOrDefault(id, 0);
        }
        return connectedSize;
    }
}
