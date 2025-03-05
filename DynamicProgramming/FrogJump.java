package DynamicProgramming;

import java.util.*;

/**
 * LeetCode Problem: https://leetcode.com/problems/frog-jump/
 * 
 * Problem Statement:
 * A frog is trying to cross a river by stepping on stones at given positions (ascending order).
 * The frog can jump either (k-1), k, or (k+1) units from its last jump.
 * The first jump is always 1.
 * Determine whether the frog can reach the last stone.
 * 
 * Approach:
 * - We use an **iterative DFS (Depth-First Search) with a stack**.
 * - A **set `stoneSet` stores valid stone positions** for O(1) lookups.
 * - A **set `visited` tracks (position, jumpSize) pairs** to avoid redundant checks.
 * - If at any step, the frog reaches the last stone, we return `true`.
 * - If all possibilities are exhausted, return `false`.
 * 
 * Optimized Check:
 * - If a stone is **too far away (more than double of the previous stone)**, return `false` early.
 * 
 * Time Complexity: **O(N²)** in worst case (each stone has ~3 jump options).
 * Space Complexity: **O(N²)** due to `visited` set storing position-jump pairs.
 */
public class FrogJump {
    public static void main(String[] args) {
        int[] stones = {0, 1, 3, 5, 6, 8, 12, 17};
        System.out.println("Can the frog cross the river? " + new FrogJump().canCross(stones));
    }

    public boolean canCross(int[] stones) {
        int n = stones.length;

        // Early termination: If a stone is unreachable
        for (int i = 3; i < n; i++) {
            if (stones[i] > stones[i - 1] * 2) {
                return false;
            }
        }

        Set<Integer> stoneSet = new HashSet<>();
        for (int stone : stones) {
            stoneSet.add(stone);
        }

        Deque<int[]> stack = new ArrayDeque<>();
        stack.push(new int[]{0, 0}); // {currentPosition, lastJumpSize}

        Set<String> visited = new HashSet<>();
        visited.add("0-0");

        int lastStone = stones[n - 1];

        while (!stack.isEmpty()) {
            int[] state = stack.pop();
            int position = state[0];
            int lastJump = state[1];

            // Try all possible jump sizes (k-1, k, k+1)
            for (int jump = lastJump - 1; jump <= lastJump + 1; jump++) {
                if (jump <= 0) continue; // Cannot make non-positive jumps

                int nextPosition = position + jump;

                if (nextPosition == lastStone) return true; // Reached last stone

                String key = nextPosition + "-" + jump;
                if (stoneSet.contains(nextPosition) && visited.add(key)) {
                    stack.push(new int[]{nextPosition, jump});
                }
            }
        }
        return false;
    }
}
