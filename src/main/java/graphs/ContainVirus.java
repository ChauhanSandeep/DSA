package graphs;

import java.util.*;

/**
 * 749. Contain Virus
 *
 * Problem: Given a grid with infected cells (1) and uninfected cells (0),
 * build walls to quarantine the infected region that would infect the most cells.
 * Return total walls built.
 *
 * Example:
 * Input: grid = [[0,1,0,0,0,0,0,1],...]
 * Output: 10
 *
 * LeetCode: https://leetcode.com/problems/contain-virus
 *
 * Follow-up questions:
 * Q: How to optimize for large grids?
 * A: Use union-find to group regions, then process.
 *
 * Q: Can we parallelize region analysis?
 * A: Yes, each region can be analyzed concurrently.
 *
 * Q: What if we only need to minimize total walls without step-by-step?
 * A: Solve as minimum cut problem on graph.
 * LeetCode Contest Rating: 2277
 **/
public class ContainVirus {

    private static final int[][] DIRS = {{-1,0},{1,0},{0,-1},{0,1}};

    /**
     * Simulates containment step-by-step, building walls around worst region.
     *
     * Algorithm:
     * - Loop until no infected cells can spread
     * - Identify all regions with their frontiers and walls needed
     * - Quarantine region with max frontier (build walls)
     * - Spread infection for other regions
     * - Accumulate walls built
     *
     * Time Complexity: O(m*n*steps)
     * Space Complexity: O(m*n)
     */
    public int containVirus(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        int totalWalls = 0;

        while (true) {
            List<Set<Integer>> regions = new ArrayList<>();
            List<Set<Integer>> frontiers = new ArrayList<>();
            List<Integer> walls = new ArrayList<>();
            boolean[][] visited = new boolean[m][n];

            // Identify regions
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if (grid[i][j] == 1 && !visited[i][j]) {
                        Set<Integer> region = new HashSet<>();
                        Set<Integer> frontier = new HashSet<>();
                        int[] count = new int[1];
                        dfs(grid, i, j, visited, region, frontier, count);
                        regions.add(region);
                        frontiers.add(frontier);
                        walls.add(count[0]);
                    }
                }
            }

            if (regions.isEmpty()) break;

            // Find region with max frontier
            int idx = 0, maxFront = 0;
            for (int i = 0; i < frontiers.size(); i++) {
                if (frontiers.get(i).size() > maxFront) {
                    maxFront = frontiers.get(i).size();
                    idx = i;
                }
            }

            // Build walls for region idx
            totalWalls += walls.get(idx);
            for (int code : regions.get(idx)) {
                int x = code / n, y = code % n;
                grid[x][y] = -1; // quarantined
            }

            // Spread infection for others
            boolean anySpread = false;
            for (int i = 0; i < regions.size(); i++) {
                if (i == idx) continue;
                for (int code : frontiers.get(i)) {
                    int x = code / n, y = code % n;
                    if (grid[x][y] == 0) {
                        grid[x][y] = 1;
                        anySpread = true;
                    }
                }
            }

            if (!anySpread) break;
        }

        return totalWalls;
    }

    // DFS to explore region, record frontier and required walls
    private void dfs(int[][] grid, int x, int y, boolean[][] visited,
                     Set<Integer> region, Set<Integer> frontier, int[] walls) {
        int m = grid.length, n = grid[0].length;
        visited[x][y] = true;
        region.add(x * n + y);

        for (int[] d : DIRS) {
            int nx = x + d[0], ny = y + d[1];
            if (nx < 0 || nx >= m || ny < 0 || ny >= n || grid[nx][ny] == -1) continue;
            if (grid[nx][ny] == 0) {
                frontier.add(nx * n + ny);
                walls[0]++;
            } else if (!visited[nx][ny]) {
                dfs(grid, nx, ny, visited, region, frontier, walls);
            }
        }
    }
}
