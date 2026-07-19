package arrays.twopointers;

import java.util.Arrays;

/**
 * Problem: Partition Array into Disjoint Intervals
 *
 * Split nums into non-empty left and right parts so every value in left is less
 * than or equal to every value in right. Return the smallest possible length of
 * left; the problem guarantees that a valid split exists.
 *
 * Leetcode: https://leetcode.com/problems/partition-array-into-disjoint-intervals/ (Medium)
 * Rating:   zerotrac 1501 (Q2, weekly-contest-104)
 * Pattern:  Array | Prefix/suffix extrema | Greedy boundary extension
 *
 * Example:
 *   Input:  nums = [5,0,3,8,6]
 *   Output: 3
 *   Why:    max([5,0,3]) = 5 and min([8,6]) = 6, so every left value is <= every right value.
 *
 * Follow-ups:
 *   1. Partition into k valid intervals?
 *      Use dynamic programming or repeated boundary checks with prefix/suffix extrema.
 *   2. Require strict inequality instead of <=?
 *      Change the boundary test to max(left) < min(right), which may make some inputs invalid.
 *   3. Return the largest valid left partition?
 *      Scan candidate boundaries from right to left or keep all valid boundaries.
 *
 * Related: Max Chunks To Make Sorted II (768), Partition Labels (763).
 */
public class PartitionArrayIntoDisjointIntervals {

public static void main(String[] args) {
    PartitionArrayIntoDisjointIntervals solver = new PartitionArrayIntoDisjointIntervals();
    int[][] inputs = { {5, 0, 3, 8, 6}, {1, 1, 1, 0, 6, 12} };
    int[] expected = { 3, 4 };

    for (int i = 0; i < inputs.length; i++) {
        int got = solver.partitionDisjoint(inputs[i]);
        System.out.printf("nums=%s -> %d  expected=%d%n",
            Arrays.toString(inputs[i]), got, expected[i]);
    }
}

    /**
 * Intuition: a split after i is valid exactly when the largest value on the
 * left side is not greater than the smallest value on the right side. Prefix
 * maxima and suffix minima make that test O(1) for every boundary.
 *
 * Algorithm:
 *   1. Build maxLeft where maxLeft[i] is the maximum over nums[0..i].
 *   2. Build minRight where minRight[i] is the minimum over nums[i..end].
 *   3. Scan boundaries from left to right.
 *   4. Return i + 1 at the first boundary with maxLeft[i] <= minRight[i + 1].
 *
 * Time:  O(n) - three linear passes over nums.
 * Space: O(n) - prefix and suffix arrays store one value per index.
 *
 * @param nums input array to partition
 * @return smallest valid left-part length
 */
    public int partitionDisjoint(int[] nums) {
        int size = nums.length;

        // Build prefix maximum array
        int[] maxLeft = new int[size];
        maxLeft[0] = nums[0];
        for (int i = 1; i < size; i++) {
            maxLeft[i] = Math.max(maxLeft[i - 1], nums[i]);
        }

        // Build suffix minimum array
        int[] minRight = new int[size];
        minRight[size - 1] = nums[size - 1];
        for (int i = size - 2; i >= 0; i--) {
            minRight[i] = Math.min(minRight[i + 1], nums[i]);
        }

        // Find partition point
        for (int i = 0; i < size - 1; i++) {
            if (maxLeft[i] <= minRight[i + 1]) {
                return i + 1;
            }
        }

        // Should never reach here given problem constraints
        return size - 1;
    }

    /**
     * Optimized single-pass approach for partitioning the array.
     *
     * Intuition:
     * - We track the maximum value in the left partition (`leftMax`) and the maximum value
     *   seen so far (`currentMax`).
     * - If we find an element smaller than `leftMax`, it means the left partition must
     *   include this element, so we extend the left partition and update `leftMax`.
     *
     * Steps:
     * 1. Initialize `leftMax` and `currentMax` to the first element.
     * 2. Iterate through the array:
     *    - Update `currentMax` to the max seen so far.
     *    - If the current element is less than `leftMax`, extend the left partition to this index,
     *      and update `leftMax` to `currentMax`.
     * 3. Return the size of the left partition.
     *
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     */
    public int partitionDisjointOptimized(int[] nums) {
        int length = nums.length;
        int leftMax = nums[0];
        int currentMax = nums[0];
        int leftPartitionEnd = 0;

        for (int i = 1; i < length; i++) {
            currentMax = Math.max(currentMax, nums[i]);
            if (nums[i] < leftMax) {
                leftPartitionEnd = i;
                leftMax = currentMax;
            }
        }

        return leftPartitionEnd + 1;
    }

    /**
     * Brute force approach for verification (less efficient)
     * Algorithm:
     * 1. For each possible partition index, compute max in left and min in right
     * 2. If maxLeft ≤ minRight, return partition index
     *
     * Time Complexity: O(n²), Space Complexity: O(1)
     */
    public int partitionDisjointBruteForce(int[] nums) {
        int size = nums.length;

        for (int partitionIndex = 1; partitionIndex < size; partitionIndex++) {
            // Find max in left part [0...partitionIndex-1]
            int maxLeft = Integer.MIN_VALUE;
            for (int i = 0; i < partitionIndex; i++) {
                maxLeft = Math.max(maxLeft, nums[i]);
            }

            // Find min in right part [partitionIndex...size-1]
            int minRight = Integer.MAX_VALUE;
            for (int i = partitionIndex; i < size; i++) {
                minRight = Math.min(minRight, nums[i]);
            }

            // Check if valid partition
            if (maxLeft <= minRight) {
                return partitionIndex;
            }
        }

        return size - 1;
    }


}
