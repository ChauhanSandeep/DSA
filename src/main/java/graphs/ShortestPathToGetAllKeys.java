package graphs;

import java.util.*;

/**
 * Problem: Shortest Path to Get All Keys
 *
 * In a grid with walls, a start cell, lowercase keys, and uppercase locks, find
 * the fewest moves needed to collect every key. A lock can be crossed only after
 * collecting its matching lowercase key.
 *
 * Leetcode: https://leetcode.com/problems/shortest-path-to-get-all-keys/
 * Rating:   2259 (zerotrac Elo)
 * Pattern:  Graph | BFS with bitmask state | Keys and locks
 *
 * Example:
 *   Input:  grid = ["@.a.#","###.#","b.A.B"]
 *   Output: 8
 *   Why:    the route must collect a and b before passing locks A and B, and BFS
 *           finds the first state whose key mask contains both keys.
 *
 * Follow-ups:
 *   1. Return the path of moves?
 *      Store parent states keyed by row, column, and key mask.
 *   2. More than 6 keys?
 *      Bitmask BFS still works for moderate k, but state count grows as 2^k.
 *   3. Keys can be consumed when opening locks?
 *      The state must track key counts or availability, not just a collected mask.
 *
 * Related: Shortest Path in a Grid with Obstacles Elimination (1293), Sliding Puzzle (773).
 */
public class ShortestPathToGetAllKeys {

    public static void main(String[] args) {
        ShortestPathToGetAllKeys solver = new ShortestPathToGetAllKeys();
        String[][] inputs = {{"@.a..", "###.#", "b.A.B"}, {"@Aa"}};
        int[] expected = {8, -1};
        for (int i = 0; i < inputs.length; i++) {
            int output = solver.shortestPathAllKeys(inputs[i]);
            System.out.printf("grid=%s -> %d  expected=%d%n", Arrays.toString(inputs[i]), output, expected[i]);
        }
    }
    private static final int[][] DIRECTIONS = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

    public int shortestPathAllKeys(String[] grid) {
        int m = grid.length;
        int n = grid[0].length();

        // Find start position and count number of keys
        int startX = -1, startY = -1, totalKeys = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                char c = grid[i].charAt(j);
                if (c == '@') {
                    startX = i;
                    startY = j;
                } else if (Character.isLowerCase(c)) {
                    totalKeys = Math.max(totalKeys, c - 'a' + 1);
                }
            }
        }

        int targetKeys = (1 << totalKeys) - 1;
        Queue<int[]> queue = new LinkedList<>();
        boolean[][][] visited = new boolean[m][n][1 << totalKeys];

        // x, y, keys, steps
        queue.offer(new int[]{startX, startY, 0, 0});
        visited[startX][startY][0] = true;

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int x = current[0], y = current[1], keys = current[2], steps = current[3];

            if (keys == targetKeys) {
                return steps;
            }

            for (int[] dir : DIRECTIONS) {
                int nx = x + dir[0];
                int ny = y + dir[1];

                // Check boundaries and walls
                if (nx < 0 || nx >= m || ny < 0 || ny >= n || grid[nx].charAt(ny) == '#') {
                    continue;
                }

                char cell = grid[nx].charAt(ny);
                int newKeys = keys;

                // Check if it's a key
                if (Character.isLowerCase(cell)) {
                    newKeys |= (1 << (cell - 'a'));
                }

                // Check if it's a lock without key
                if (Character.isUpperCase(cell) && (keys & (1 << (cell - 'A'))) == 0) {
                    continue;
                }

                // If not visited with current key state
                if (!visited[nx][ny][newKeys]) {
                    visited[nx][ny][newKeys] = true;
                    queue.offer(new int[]{nx, ny, newKeys, steps + 1});
                }
            }
        }

        return -1;
    }
}
