package dynamicprogramming.MiscellaneousDP;

import java.util.*;

/**
 * Problem: Minimum Jumps to Reach Home
 *
 * A bug starts at 0 and wants to reach x. It may jump a positions forward or b
 * positions backward, cannot land on forbidden positions, and cannot jump backward
 * twice in a row. Return the minimum jumps, or -1 if unreachable.
 *
 * Leetcode: https://leetcode.com/problems/minimum-jumps-to-reach-home/
 * Rating:   2124 (zerotrac Elo)
 * Pattern:  BFS | State graph | Direction-aware visited state
 *
 * Example:
 *   Input:  forbidden = [14,4,18,1,15], a = 3, b = 15, x = 9
 *   Output: 3
 *   Why:    three forward jumps go 0 -> 3 -> 6 -> 9 without hitting a forbidden position.
 *
 * Follow-ups:
 *   1. Allow up to k backward jumps in a row?
 *      Add the current backward-run length to the BFS state.
 *   2. Return the actual path?
 *      Store a parent pointer for each visited state and reconstruct when x is reached.
 *   3. What if forbidden positions are streamed?
 *      The graph changes over time; use dynamic shortest-path updates or rerun BFS when needed.
 *
 * Related: Frog Jump (403), Jump Game III (1306).
 */
public class MinimumJumpsToReachHome {
    /**
     * Intuition: this is a shortest-path problem on positions, but position alone
     * is not enough state. Landing at the same position after a backward jump is
     * different from landing there after a forward jump, because only one of those
     * states may jump backward next. BFS explores by jump count, so the first time
     * it reaches x is minimal. A finite upper bound keeps the search from drifting
     * forever to the right.
     *
     * Algorithm:
     *   1. Build forbiddenSet, visited[position][direction], and a BFS queue from position 0.
     *   2. Process BFS one level at a time so jumps is the current distance.
     *   3. Always try a forward jump within maxLimit.
     *   4. Try a backward jump only when the previous jump was not backward.
     *
     * Time:  O(U) - at most two direction states are visited for each position up to the bound U.
     * Space: O(U) - the visited states and queue are bounded by the same positions.
     *
     * @param forbidden positions the bug cannot land on
     * @param a forward jump length
     * @param b backward jump length
     * @param x target home position
     * @return minimum jumps to reach x, or -1 if impossible
     */
    public int minimumJumps(int[] forbidden, int a, int b, int x) {
        // Maximum possible position we need to consider
        int maxLimit = 2000 + 2 * Math.max(a, b);

        // Convert forbidden array to a set for O(1) lookups
        Set<Integer> forbiddenSet = new HashSet<>();
        for (int pos : forbidden) {
            forbiddenSet.add(pos);
        }

        // Each state is represented as [currentPosition, isPreviousJumpBackward]
        // We use a boolean to track if the previous jump was backward
        boolean[][] visited = new boolean[maxLimit + 1][2];
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{0, 0}); // [position, jumps]
        visited[0][0] = true;

        int jumps = 0;

        while (!queue.isEmpty()) {
            int size = queue.size();

            for (int i = 0; i < size; i++) {
                int[] current = queue.poll();
                int position = current[0];
                int isPrevBackward = current[1];

                // If we've reached home
                if (position == x) {
                    return jumps;
                }

                // Try jumping forward
                int nextPos = position + a;
                if (nextPos <= maxLimit && !forbiddenSet.contains(nextPos) && !visited[nextPos][0]) {
                    visited[nextPos][0] = true;
                    queue.offer(new int[]{nextPos, 0});
                }

                // Try jumping backward if the previous jump wasn't backward
                nextPos = position - b;
                if (isPrevBackward == 0 && nextPos >= 0 && !forbiddenSet.contains(nextPos) && !visited[nextPos][1]) {
                    visited[nextPos][1] = true;
                    queue.offer(new int[]{nextPos, 1});
                }
            }

            jumps++;
        }

        return -1;
    }

    /**
     * Optimized solution with early termination and better bounds
     */
    public int minimumJumpsOptimized(int[] forbidden, int a, int b, int x) {
        // The upper bound is max(max(forbidden) + a + b, x) + b
        // This ensures we can reach x even if we need to jump back
        int upperBound = 0;
        for (int pos : forbidden) {
            upperBound = Math.max(upperBound, pos);
        }
        upperBound = Math.max(upperBound + a + b, x) + b;

        Set<Integer> forbiddenSet = new HashSet<>();
        for (int pos : forbidden) {
            forbiddenSet.add(pos);
        }

        // [position, isPreviousBackward]
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{0, 0, 0}); // [position, isPreviousBackward, jumps]

        // We can use a single visited set by encoding the direction in the position
        // For positions < 0: position * 2 - 1 (backward)
        // For positions >= 0: position * 2 (forward)
        Set<Integer> visited = new HashSet<>();
        visited.add(0); // Start at position 0, forward jump (0 * 2 = 0)

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int position = current[0];
            int isPrevBackward = current[1];
            int jumps = current[2];

            if (position == x) {
                return jumps;
            }

            // Try jumping forward
            int nextPos = position + a;
            int key = nextPos * 2;
            if (nextPos <= upperBound && !forbiddenSet.contains(nextPos) && !visited.contains(key)) {
                visited.add(key);
                queue.offer(new int[]{nextPos, 0, jumps + 1});
            }

            // Try jumping backward if previous wasn't backward
            if (isPrevBackward == 0) {
                nextPos = position - b;
                key = nextPos * 2 - 1;
                if (nextPos >= 0 && !forbiddenSet.contains(nextPos) && !visited.contains(key)) {
                    visited.add(key);
                    queue.offer(new int[]{nextPos, 1, jumps + 1});
                }
            }
        }

        return -1;
    }

    public static void main(String[] args) {
        MinimumJumpsToReachHome solver = new MinimumJumpsToReachHome();
        int[][] forbidden = {{}, {14, 4, 18, 1, 15}, {8, 3, 16, 6, 12, 20}};
        int[] forward = {2, 3, 15};
        int[] backward = {1, 15, 13};
        int[] target = {0, 9, 11};
        int[] expected = {0, 3, -1};

        for (int i = 0; i < forbidden.length; i++) {
            int got = solver.minimumJumps(forbidden[i], forward[i], backward[i], target[i]);
            System.out.printf("forbidden=%s a=%d b=%d x=%d -> %d  expected=%d%n",
                Arrays.toString(forbidden[i]), forward[i], backward[i], target[i], got, expected[i]);
        }
    }
}
