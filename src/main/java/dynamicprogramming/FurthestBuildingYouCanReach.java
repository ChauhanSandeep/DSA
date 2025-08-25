package dynamicprogramming;

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * Problem: Furthest Building You Can Reach (LeetCode #1642)
 *
 * Problem Statement:
 * You are given an integer array heights representing the heights of buildings, some bricks, and some ladders.
 * You start your journey from building 0 and move to the next building by possibly using bricks or ladders.
 *
 * While moving from building i to building i+1 (0-indexed):
 * - If the current building's height >= next building's height, you do not need a ladder or bricks.
 * - If the current building's height < next building's height, you can either use one ladder or (h[i+1] - h[i]) bricks.
 *
 * Return the furthest building index (0-indexed) you can reach if you use the given ladders and bricks optimally.
 *
 * Example 1:
 * Input: heights = [4,2,7,6,9,14,12], bricks = 5, ladders = 1
 * Output: 4
 * Explanation: Starting at building 0, you can follow these steps:
 * - Go to building 1 without using ladders nor bricks since 4 >= 2
 * - Go to building 2 using 5 bricks (2 < 7, difference is 5)
 * - Go to building 3 without using ladders nor bricks since 7 >= 6
 * - Go to building 4 using 3 bricks (6 < 9, difference is 3)
 * It's impossible to go beyond building 4 because you have to climb 5 more bricks to reach building 5,
 * but you only have 0 bricks and 1 ladder.
 *
 * Approach:
 * We can solve this problem using a greedy approach with a min-heap. The key insight is to use ladders for the
 * largest jumps and bricks for the smaller ones. We'll keep track of the largest jumps using a min-heap, and
 * when we run out of ladders, we'll use bricks for the smallest jump in the heap.
 *
 * Steps to solve:
 * 1. Use a min-heap to keep track of the k largest jumps, where k is the number of ladders.
 * 2. For each building, calculate the height difference with the next building.
 * 3. If the height difference is positive (we need to climb):
 *    a. If we have ladders remaining, use a ladder and add the jump to the heap.
 *    b. If no ladders left, check if we can replace the smallest ladder jump with bricks.
 *    c. If we can't use bricks for the smallest ladder jump, we can't proceed further.
 * 4. If we can't make the jump with the available resources, return the current building index.
 * 5. If we can make all jumps, return the last building index.
 *
 * Time Complexity: O(n log k) where n is the number of buildings and k is the number of ladders
 * Space Complexity: O(k) for the min-heap
 *
 * Follow-up Questions:
 * 1. What if we have multiple types of ladders with different lengths?
 *    Answer: We would need to modify the approach to consider the ladder types and their lengths when deciding
 *    which ladder to use for each jump, potentially using a priority queue to manage the ladders.
 *
 * 2. What if we can use both bricks and ladders for a single jump?
 *    Answer: The problem would become more complex as we would need to decide the optimal combination of bricks
 *    and ladders for each jump, possibly requiring a different dynamic programming approach.
 *
 * 3. Can you solve it with O(1) space?
 *    Answer: No, since we need to keep track of the largest jumps to make optimal use of ladders, which requires
 *    some form of sorting or priority queue, leading to at least O(k) space.
 *
 * LeetCode: https://leetcode.com/problems/furthest-building-you-can-reach/
 */
public class FurthestBuildingYouCanReach {

    /**
     * Calculates the furthest building you can reach with the given bricks and ladders.
     *
     * Steps to solve:
     * 1. Initialize a min-heap to keep track of the largest jumps where ladders are used.
     * 2. Iterate through each building from index 0 to n-2:
     *    a. Calculate the height difference between the current and next building.
     *    b. If the height difference is positive (need to climb):
     *       - If we have ladders remaining, use a ladder and add the jump to the heap.
     *       - Else, if the heap is not empty and the current jump is larger than the smallest jump in the heap:
     *           * Replace the smallest ladder jump with bricks (remove it from heap and add to bricks).
     *           * Use a ladder for the current jump.
     *       - If we can't use bricks for the jump (not enough bricks), return the current index.
     * 3. If we can process all buildings, return the last index.
     *
     * @param heights Array of building heights
     * @param bricks Number of bricks available
     * @param ladders Number of ladders available
     * @return The furthest building index (0-based) that can be reached
     */
    public int furthestBuilding(int[] heights, int bricks, int ladders) {
        // Min-heap to store the largest jumps where ladders are used
        PriorityQueue<Integer> ladderJumpMinHeap = new PriorityQueue<>();

        for (int i = 0; i < heights.length - 1; i++) {
            int diff = heights[i + 1] - heights[i];

            // No need for bricks or ladders if the next building is lower or equal
            if (diff <= 0) {
                continue;
            }

            // If we have ladders remaining, use a ladder for this jump
            if (ladderJumpMinHeap.size() < ladders) {
                ladderJumpMinHeap.offer(diff);
            } else {
                // No ladders left, check if we can replace the smallest ladder jump with bricks
                if (!ladderJumpMinHeap.isEmpty() && diff > ladderJumpMinHeap.peek()) {
                    // Replace the smallest ladder jump with bricks
                    int smallestLadderJump = ladderJumpMinHeap.poll();
                    if (bricks >= smallestLadderJump) {
                        bricks -= smallestLadderJump;
                        ladderJumpMinHeap.offer(diff); // Use ladder for the current (larger) jump
                    } else {
                        // Not enough bricks to replace the smallest ladder jump
                        return i;
                    }
                } else if (bricks >= diff) {
                    // Use bricks for the current jump
                    bricks -= diff;
                } else {
                    // Can't make the jump with available resources
                    return i;
                }
            }
        }

        // If we can reach the last building
        return heights.length - 1;
    }

    /**
     * Alternative solution using binary search
     *
     * Steps to solve:
     * 1. Use binary search to find the furthest reachable building.
     * 2. For each candidate building, check if it's possible to reach it with the given bricks and ladders.
     * 3. To check if a building is reachable, collect all positive jumps up to that building,
     *    sort them in descending order, and use ladders for the largest jumps and bricks for the rest.
     *
     * Time Complexity: O(n log n) due to sorting in each binary search step
     * Space Complexity: O(n) for storing the jumps
     */
    public int furthestBuildingBinarySearch(int[] heights, int bricks, int ladders) {
        int left = 0;
        int right = heights.length - 1;

        while (left < right) {
            int mid = left + (right - left + 1) / 2;

            if (canReach(heights, mid, bricks, ladders)) {
                left = mid;
            } else {
                right = mid - 1;
            }
        }

        return left;
    }

    /**
     * Helper method to check if we can reach the target building
     */
    private boolean canReach(int[] heights, int target, int bricks, int ladders) {
        // Collect all positive jumps up to the target building
        int[] jumps = new int[target];
        int jumpCount = 0;

        for (int i = 0; i < target; i++) {
            int diff = heights[i + 1] - heights[i];
            if (diff > 0) {
                jumps[jumpCount++] = diff;
            }
        }

        // If no jumps needed or enough ladders for all jumps
        if (jumpCount <= ladders) {
            return true;
        }

        // Sort the jumps in ascending order
        Arrays.sort(jumps, 0, jumpCount);

        // Use bricks for the smallest (jumpCount - ladders) jumps
        int bricksNeeded = 0;
        for (int i = 0; i < jumpCount - ladders; i++) {
            bricksNeeded += jumps[i];
            if (bricksNeeded > bricks) {
                return false;
            }
        }

        return true;
    }

    /**
     * Space-optimized solution using counting sort
     *
     * This approach is efficient when the maximum jump height is not too large.
     * It uses counting sort to sort the jumps in O(n) time.
     */
    public int furthestBuildingCountingSort(int[] heights, int bricks, int ladders) {
        int left = 0;
        int right = heights.length - 1;

        while (left < right) {
            int mid = left + (right - left + 1) / 2;

            if (canReachCountingSort(heights, mid, bricks, ladders)) {
                left = mid;
            } else {
                right = mid - 1;
            }
        }

        return left;
    }

    /**
     * Helper method using counting sort for the jumps
     */
    private boolean canReachCountingSort(int[] heights, int target, int bricks, int ladders) {
        int maxJump = 0;
        int jumpCount = 0;

        // First pass: count jumps and find maximum jump
        for (int i = 0; i < target; i++) {
            int diff = heights[i + 1] - heights[i];
            if (diff > 0) {
                maxJump = Math.max(maxJump, diff);
                jumpCount++;
            }
        }

        // If no jumps needed or enough ladders for all jumps
        if (jumpCount <= ladders) {
            return true;
        }

        // Use counting sort to sort the jumps
        int[] count = new int[maxJump + 1];
        for (int i = 0; i < target; i++) {
            int diff = heights[i + 1] - heights[i];
            if (diff > 0) {
                count[diff]++;
            }
        }

        // Calculate bricks needed for the smallest (jumpCount - ladders) jumps
        int bricksNeeded = 0;
        int jumpsToCover = jumpCount - ladders;

        for (int jump = 1; jump <= maxJump && jumpsToCover > 0; jump++) {
            int jumps = Math.min(count[jump], jumpsToCover);
            bricksNeeded += jumps * jump;
            jumpsToCover -= jumps;

            if (bricksNeeded > bricks) {
                return false;
            }
        }

        return true;
    }
}
