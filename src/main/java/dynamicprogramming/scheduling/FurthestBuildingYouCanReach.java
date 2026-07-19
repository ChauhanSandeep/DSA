package dynamicprogramming.scheduling;

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * Problem: Furthest Building You Can Reach
 *
 * Move across building heights using bricks or ladders for upward jumps. Return the furthest index reachable with optimal resource assignment.
 *
 * Leetcode: https://leetcode.com/problems/furthest-building-you-can-reach/ (Medium)
 * Rating:   contest Elo 1962
 * Pattern:  Greedy | Min-heap | Resource scheduling
 *
 * Example:
 *   Input:  heights = [4,2,7,6,9,14,12], bricks = 5, ladders = 1
 *   Output: 4
 *   Why:    after reaching index 4, the next climb needs more resources than remain.
 *
 * Follow-ups:
 *   1. How would you return an actual solution, not only the value?
 *      Store predecessor or choice information while filling the same states.
 *   2. How can space be reduced?
 *      Keep only the previous row or active states when the recurrence allows it.
 *   3. How would constraints such as fees, limits, or weights change it?
 *      Add the constraint to the state or transition and keep the same invariant.
 *
 * Related: Course Schedule III (630), Minimum Number of Refueling Stops (871).
 */
public class FurthestBuildingYouCanReach {

    public static void main(String[] args) {
        FurthestBuildingYouCanReach solution = new FurthestBuildingYouCanReach();
        int[][] heightCases = { {4, 2, 7, 6, 9, 14, 12}, {4, 12, 2, 7, 3, 18, 20, 3, 19}, {1} };
        int[] bricksCases = {5, 10, 0};
        int[] ladderCases = {1, 2, 0};
        int[] expected = {4, 7, 0};
        for (int i = 0; i < heightCases.length; i++) {
            int got = solution.furthestBuilding(heightCases[i], bricksCases[i], ladderCases[i]);
            System.out.printf("heights=%s bricks=%d ladders=%d -> %d  expected=%d%n", Arrays.toString(heightCases[i]), bricksCases[i], ladderCases[i], got, expected[i]);
        }
    }


        /**
     * Intuition: ladders should cover the largest climbs seen so far, and bricks should cover smaller climbs. ladderJumpMinHeap stores climbs assigned to ladders; a larger new climb can replace the smallest ladder climb if bricks can pay for that smaller one.
     *
     * Algorithm:
     *   1. Scan adjacent height differences.
     *   2. Ignore non-positive climbs.
     *   3. Use ladder slots while available.
     *   4. Otherwise swap a larger climb into the ladder heap when bricks can pay the smaller climb.
     *   5. Return the current index when neither resource can pay.
     *
     * Time:  O(n log ladders) - positive climbs may touch the heap.
     * Space: O(ladders) - heap stores ladder climbs.
     *
     * @param heights building heights
     * @param bricks available bricks
     * @param ladders available ladders
     * @return furthest reachable index
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

        /** Checks whether a target building prefix is reachable. */
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

        /** Checks prefix reachability using counted jump sizes. */
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
