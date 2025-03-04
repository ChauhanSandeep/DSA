package Array;

/**
 * Given an array representing garden taps, where each element denotes the range of the tap at that index,
 * this solution finds the minimum number of taps required to water the entire garden.
 *
 * LeetCode Link: https://leetcode.com/problems/minimum-number-of-taps-to-open-to-water-a-garden/
 *
 * Algorithm: Greedy Interval Covering with Precomputation
 * Time Complexity: O(n) - We process the garden length efficiently.
 * Space Complexity: O(n) - Uses an auxiliary array to store maximum reach from each position.
 */
public class MinTaps {

    public static void main(String[] args) {
        int target = 5;
        int[] ranges = {4, 3, 1, 2, 0, 0};
        int taps = new MinTaps().minTaps(target, ranges);
        System.out.println("Minimum taps required: " + taps);
    }

    /**
     * **Approach (Greedy Algorithm - O(n^2)):**
     * - Start from position `0` (leftmost point of the garden).
     * - In each step, find the **tap that extends coverage the farthest** from the current position.
     * - If no tap can extend coverage, return `-1` (garden cannot be fully watered).
     * - Continue until the entire garden (`target`) is covered.
     *
     * **Time Complexity:** O(n^2) (Can be optimized to O(n) using a sorted jump approach)
     * **Space Complexity:** O(1) (No extra space used)
     *
     * @param target Length of the garden (last index to be covered)
     * @param ranges Array where `ranges[i]` represents the tap range at position `i`
     * @return Minimum taps required to fully water the garden, or `-1` if impossible
     */
    public int minTaps(int target, int[] ranges) {
        int currentCoverageEnd = 0;  // Tracks the farthest point currently covered
        int nextCoverageEnd = 0;     // Tracks the farthest extension possible
        int tapsUsed = 0;            // Counts the number of taps turned on

        while (currentCoverageEnd < target) {
            // Find the tap that extends coverage the farthest within the current reachable range
            for (int i = 0; i < ranges.length; i++) {
                int leftLimit = i - ranges[i];   // Left boundary of this tap's range
                int rightLimit = i + ranges[i];  // Right boundary of this tap's range

                // If this tap covers the current uncovered area and extends beyond the current max
                if (leftLimit <= currentCoverageEnd && rightLimit > nextCoverageEnd) {
                    nextCoverageEnd = rightLimit;
                }
            }

            // If no new coverage is found, it's impossible to cover the full garden
            if (currentCoverageEnd == nextCoverageEnd) return -1;

            // Activate a tap and extend the coverage
            tapsUsed++;
            currentCoverageEnd = nextCoverageEnd;
        }

        return tapsUsed;
    }

    /**
     * Optimized approach to find the minimum taps needed to water the entire garden.
     * Uses a greedy jump approach (similar to Jump Game II) to achieve **O(n) complexity**.
     *
     * **Approach:**
     * 1. Precompute `maxReach[i]` → The farthest index that can be watered from `i`.
     * 2. Use a **greedy approach**:
     *    - Keep track of the current coverage (`currEnd`).
     *    - Expand coverage using the farthest tap found in the current range.
     *    - If at any step we can't expand further, return `-1`.
     *
     * **Time Complexity:** O(n) (Precomputing + Single pass)
     * **Space Complexity:** O(n) (For maxReach array)
     *
     * @param target Length of the garden (last index to be covered)
     * @param ranges Array where `ranges[i]` represents the tap range at position `i`
     * @return Minimum taps required to fully water the garden, or `-1` if impossible
     */
    public int minTapsOptimized(int target, int[] ranges) {
        int[] maxReach = new int[target + 1];

        // Step 1: Build maxReach array
        for (int i = 0; i < ranges.length; i++) {
            int left = Math.max(0, i - ranges[i]);
            int right = Math.min(target, i + ranges[i]);
            maxReach[left] = Math.max(maxReach[left], right);
        }

        // Step 2: Use Greedy Jump strategy
        int tapsUsed = 0;
        int currEnd = 0, nextEnd = 0;
        int i = 0;

        while (currEnd < target) {
            // Expand coverage to the farthest reachable index within current range
            while (i <= currEnd) {
                nextEnd = Math.max(nextEnd, maxReach[i]);
                i++;
            }

            // If we cannot extend further, return -1 (unreachable)
            if (currEnd == nextEnd) return -1;

            // Use a tap to extend the coverage
            tapsUsed++;
            currEnd = nextEnd;
        }

        return tapsUsed;
    }
}