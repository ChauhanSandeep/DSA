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
     * Finds the minimum number of taps needed to water the entire garden up to the given target.
     *
     * @param target The last index of the garden that needs watering.
     * @param ranges An array where each index represents a tap and its value denotes its watering range.
     * @return The minimum number of taps required to water the entire garden, or -1 if it's not possible.
     */
    public int minTaps(int target, int[] ranges) {
        // Array to store the farthest right position reachable from each starting index.
        int[] maxReach = new int[target + 1];

        // Precompute the maximum reach for each tap.
        for (int i = 0; i < ranges.length; i++) {
            int left = Math.max(0, i - ranges[i]);
            int right = Math.min(target, i + ranges[i]);
            maxReach[left] = Math.max(maxReach[left], right);
        }

        int taps = 0, currEnd = 0, nextEnd = 0;

        // Greedily extend the watering range using the precomputed maximum reaches.
        for (int i = 0; i <= target; i++) {
            // If current index exceeds the furthest reachable position, the garden cannot be fully watered.
            if (i > nextEnd) {
                return -1;
            }
            // If the current index exceeds the current tap's range, open a new tap.
            if (i > currEnd) {
                taps++;
                currEnd = nextEnd;
            }
            nextEnd = Math.max(nextEnd, maxReach[i]);
        }

        return taps;
    }
}