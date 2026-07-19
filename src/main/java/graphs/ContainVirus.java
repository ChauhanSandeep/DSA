package graphs;

import java.util.*;

/**
 * Problem: Contain Virus
 *
 * A grid has infected cells, clean cells, and later quarantined cells. Each day,
 * choose the infected region that threatens the largest number of clean cells,
 * build walls around it, and let every other infected region spread one step.
 * Return the total number of walls built.
 *
 * Leetcode: https://leetcode.com/problems/contain-virus/ (Hard)
 * Rating:   2277 (zerotrac Elo)
 * Pattern:  Graph | Simulation | DFS connected components
 *
 * Example:
 *   Input:  isInfected = [[0,1,0,0,0,0,0,1],[0,1,0,0,0,0,0,1],[0,0,0,0,0,0,0,1],[0,0,0,0,0,0,0,0]]
 *   Output: 10
 *   Why:    the left region threatens more new cells first, so it is walled off;
 *           after the remaining region spreads, the next quarantine uses the rest.
 *
 * Follow-ups:
 *   1. Minimize walls with a different policy?
 *      Model choices over time as an optimization problem instead of the greedy simulation.
 *   2. Return which cells were quarantined each day?
 *      Store the selected region set before marking it as blocked.
 *   3. Scale region discovery on a large grid?
 *      Maintain components with union-find between days, rebuilding only changed borders.
 *
 * Related: Max Area of Island (695), Making A Large Island (827).
 */
public class ContainVirus {


    public static void main(String[] args) {
        ContainVirus solver = new ContainVirus();
        int[][][] inputs = {
            {{0, 1, 0, 0, 0, 0, 0, 1}, {0, 1, 0, 0, 0, 0, 0, 1}, {0, 0, 0, 0, 0, 0, 0, 1}, {0, 0, 0, 0, 0, 0, 0, 0}},
            {{1, 1, 1}, {1, 1, 1}}
        };
        int[] expected = {10, 0};

        for (int i = 0; i < inputs.length; i++) {
            int[][] grid = Arrays.stream(inputs[i]).map(int[]::clone).toArray(int[][]::new);
            int output = solver.containVirus(grid);
            System.out.printf("grid=%s  ->  %d  expected=%d%n",
                Arrays.deepToString(inputs[i]), output, expected[i]);
        }
    }
    private static final int[][] DIRS = {{-1,0},{1,0},{0,-1},{0,1}};
    /**
     * Intuition: each day we quarantine the active region that threatens the most
     * new cells, because only one region can be contained before the virus spreads.
     * DFS discovers every region, counts its frontier, and records the walls needed.
     * The chosen region is sealed; every other region expands into its frontier.
     *
     * Algorithm:
     *   1. Discover all uncontained infected regions and their threatened cells.
     *   2. Choose the region with the largest threatened frontier.
     *   3. Add that region's wall count and mark it as quarantined.
     *   4. Spread every other region into its threatened cells and repeat.
     *
     * Time:  O((m*n)^2) - each day can rescan the grid, and there can be many days.
     * Space: O(m*n) - visited sets and region/frontier lists can cover the grid.
     *
     * @param grid infection grid where 1 is active virus, 0 is clean, and quarantined cells are marked in place
     * @return total number of walls built
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
