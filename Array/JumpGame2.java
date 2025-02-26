package Array;

import java.util.Arrays;

/**
 * LeetCode: https://leetcode.com/problems/jump-game-ii/
 *
 * Problem:
 * Given an array `nums` where each element represents the max jump length from that position,
 * return the minimum number of jumps required to reach the last index.
 *
 * Approach:
 * - We use a **Greedy Algorithm** to track the farthest reachable position.
 * - At each step, we greedily find the next best jump that takes us farthest.
 *
 * Time Complexity: O(n)  
 * Space Complexity: O(1)
 */
public class JumpGame2 {
    public static void main(String[] args) {
        int[] nums = {2, 3, 1, 1, 4};
        int jumps = minJumps(nums);
        System.out.println("Min jumps required to reach end: " + jumps);
    }

    /**
     * Greedy approach to find the minimum number of jumps.
     * @param nums The input array where nums[i] represents max jump length at index i.
     * @return Minimum jumps required to reach the last index.
     */
    public static int minJumps(int[] nums) {
        if (nums.length <= 1) return 0; // If only one element, no jumps needed

        int jumps = 0;       // Total jumps taken
        int farthest = 0;    // Farthest index reachable at the current jump
        int currentEnd = 0;  // Current boundary of our reachable range

        for (int i = 0; i < nums.length - 1; i++) {
            farthest = Math.max(farthest, i + nums[i]); // Track the farthest position we can reach

            // When we reach the current boundary, we must jump
            if (i == currentEnd) {
                jumps++;
                currentEnd = farthest; // Update boundary

                if (currentEnd >= nums.length - 1) break; // Early exit if last index is reached
            }
        }
        return jumps;
    }
}
