package array;

/**
 * Partition Array Into Disjoint Intervals
 *
 * Problem: Find smallest index to partition array such that max(left_part) ≤ min(right_part).
 *
 * Example: nums = [5,0,3,8,6] -> Output: 3
 * Partition at index 3: [5,0,3] and [8,6]. max([5,0,3]) = 5 ≤ min([8,6]) = 6.
 *
 * LeetCode: https://leetcode.com/problems/partition-array-into-disjoint-intervals
 *
 * Follow-up Questions:
 * - What if multiple partitions are valid? (Problem asks for smallest index)
 * - How to handle empty partitions? (Problem guarantees non-empty partitions exist)
 * - What if we want largest valid partition index? (Scan from right to left)
 */
public class PartitionArrayIntoDisjointIntervals {

    /**
     * Finds smallest partition index using prefix max and suffix min arrays.
     *
     * Algorithm:
     * 1. Build prefix maximum array: maxLeft[i] = max of elements [0...i]
     * 2. Build suffix minimum array: minRight[i] = min of elements [i...n-1]
     * 3. Find smallest index i where maxLeft[i] ≤ minRight[i+1]
     * 4. Return i+1 (partition after index i)
     *
     * Time Complexity: O(n) where n is array length
     * Space Complexity: O(n) for prefix and suffix arrays
     *
     * @param nums input array to partition
     * @return smallest valid partition index
     */
    public int partitionDisjoint(int[] nums) {
        int n = nums.length;

        // Build prefix maximum array
        int[] maxLeft = new int[n];
        maxLeft[0] = nums[0];
        for (int i = 1; i < n; i++) {
            maxLeft[i] = Math.max(maxLeft[i - 1], nums[i]);
        }

        // Build suffix minimum array
        int[] minRight = new int[n];
        minRight[n - 1] = nums[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            minRight[i] = Math.min(minRight[i + 1], nums[i]);
        }

        // Find partition point
        for (int i = 0; i < n - 1; i++) {
            if (maxLeft[i] <= minRight[i + 1]) {
                return i + 1;
            }
        }

        // Should never reach here given problem constraints
        return n - 1;
    }

    /**
     * Optimized single-pass approach
     * Time Complexity: O(n), Space Complexity: O(1)
     */
    public int partitionDisjointOptimized(int[] nums) {
        int n = nums.length;
        int maxSoFar = nums[0];
        int maxEndingHere = nums[0];
        int partitionIndex = 0;

        for (int i = 1; i < n; i++) {
            maxEndingHere = Math.max(maxEndingHere, nums[i]);

            // If current element is smaller than max so far,
            // we need to extend left partition
            if (nums[i] < maxSoFar) {
                partitionIndex = i;
                maxSoFar = maxEndingHere;
            }
        }

        return partitionIndex + 1;
    }

    /**
     * Two-pass approach with clearer logic
     * Time Complexity: O(n), Space Complexity: O(n)
     */
    public int partitionDisjointTwoPass(int[] nums) {
        int n = nums.length;

        // Calculate suffix minimums
        int[] suffixMin = new int[n];
        suffixMin[n - 1] = nums[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            suffixMin[i] = Math.min(suffixMin[i + 1], nums[i]);
        }

        // Find partition while tracking prefix maximum
        int prefixMax = nums[0];
        for (int i = 0; i < n - 1; i++) {
            if (prefixMax <= suffixMin[i + 1]) {
                return i + 1;
            }
            if (i + 1 < n) {
                prefixMax = Math.max(prefixMax, nums[i + 1]);
            }
        }

        return n - 1;
    }

    /**
     * Brute force approach for verification (less efficient)
     * Time Complexity: O(n²), Space Complexity: O(1)
     */
    public int partitionDisjointBruteForce(int[] nums) {
        int n = nums.length;

        for (int partitionIndex = 1; partitionIndex < n; partitionIndex++) {
            // Find max in left part [0...partitionIndex-1]
            int maxLeft = Integer.MIN_VALUE;
            for (int i = 0; i < partitionIndex; i++) {
                maxLeft = Math.max(maxLeft, nums[i]);
            }

            // Find min in right part [partitionIndex...n-1]
            int minRight = Integer.MAX_VALUE;
            for (int i = partitionIndex; i < n; i++) {
                minRight = Math.min(minRight, nums[i]);
            }

            // Check if valid partition
            if (maxLeft <= minRight) {
                return partitionIndex;
            }
        }

        return n - 1;
    }

    /**
     * Helper method to validate a partition (for testing)
     */
    public boolean isValidPartition(int[] nums, int partitionIndex) {
        if (partitionIndex <= 0 || partitionIndex >= nums.length) {
            return false;
        }

        int maxLeft = Integer.MIN_VALUE;
        for (int i = 0; i < partitionIndex; i++) {
            maxLeft = Math.max(maxLeft, nums[i]);
        }

        int minRight = Integer.MAX_VALUE;
        for (int i = partitionIndex; i < nums.length; i++) {
            minRight = Math.min(minRight, nums[i]);
        }

        return maxLeft <= minRight;
    }
}
