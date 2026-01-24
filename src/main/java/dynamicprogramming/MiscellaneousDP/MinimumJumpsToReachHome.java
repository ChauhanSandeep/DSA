package dynamicprogramming.MiscellaneousDP;

import java.util.*;

/**
 * A certain bug's home is on the x-axis at position x. Help them get there from position 0.
 *
 * The bug jumps according to the following rules:
 * 1. It can jump exactly a positions forward (to the right).
 * 2. It can jump exactly b positions backward (to the left).
 * 3. It cannot jump backward twice in a row.
 * 4. It cannot jump to any forbidden positions.
 * 5. It can jump beyond its home, but it cannot jump to positions with negative integers.
 *
 * Given an array of integers forbidden, where forbidden[i] means the bug cannot jump to the position forbidden[i], and integers a, b, and x, return the minimum number of jumps needed for the bug to reach its home. If there is no possible sequence of jumps that lands the bug on position x, return -1.
 *
 * Example 1:
 * Input: forbidden = [14,4,18,1,15], a = 3, b = 15, x = 9
 * Output: 3
 * Explanation: 3 jumps forward (0 -> 3 -> 6 -> 9) will get the bug home.
 *
 * Example 2:
 * Input: forbidden = [8,3,16,6,12,20], a = 15, b = 13, x = 11
 * Output: -1
 *
 * LeetCode: https://leetcode.com/problems/minimum-jumps-to-reach-home/
 *
 * Follow-up Questions:
 * 1. What if the bug can jump backward multiple times in a row?
 *    - The problem would become more complex as we'd need to track the number of consecutive backward jumps.
 * 2. How would you handle very large values of x, a, and b (e.g., up to 2000)?
 *    - The BFS approach with proper bounds should handle this efficiently.
 * 3. What if there are multiple forbidden positions?
 *    - The solution already handles multiple forbidden positions using a set.
 *
 * Related Problems:
 * - Jump Game II (https://leetcode.com/problems/jump-game-ii/)
 * - Jump Game III (https://leetcode.com/problems/jump-game-iii/)
 * LeetCode Contest Rating: 2124
 **/
public class MinimumJumpsToReachHome {
    /**
     * Calculates the minimum number of jumps needed to reach home.
     *
     * @param forbidden Array of forbidden positions
     * @param a Number of positions to jump forward
     * @param b Number of positions to jump backward
     * @param x Target position (home)
     * @return Minimum number of jumps or -1 if not possible
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
}
