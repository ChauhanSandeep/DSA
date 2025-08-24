package graph;

import java.util.*;

/**
 * Problem: Shortest Path to Get All Keys
 *
 * You are given an m x n grid grid where:
 * - '.' is an empty cell.
 * - '#' is a wall.
 * - '@' is the starting point.
 * - Lowercase letters represent keys.
 * - Uppercase letters represent locks.
 *
 * You start at the starting point and one move consists of walking one space in one of the four cardinal directions.
 * You cannot walk outside the grid, or walk into a wall.
 * If you walk over a key, you can pick it up and you cannot pick up a key more than once.
 * If you walk over a lock, you can only do so if you have the corresponding key.
 *
 * Return the lowest number of moves to acquire all keys. If it's impossible, return -1.
 *
 * Example:
 * Input: grid = ["@.a.#","###.#","b.A.B"]
 * Output: 8
 * Explanation: The path is @ -> . -> a -> . -> b -> . -> A -> B
 *
 * LeetCode: https://leetcode.com/problems/shortest-path-to-get-all-keys
 *
 * Time Complexity: O(m * n * 2^k) where m is number of rows, n is number of columns, and k is number of keys
 * Space Complexity: O(m * n * 2^k) for the visited set
 */
public class ShortestPathToGetAllKeys {
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
