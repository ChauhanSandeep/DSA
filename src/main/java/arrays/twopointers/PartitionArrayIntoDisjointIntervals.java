package arrays.twopointers;

/**
 * 915. Partition Array into Disjoint Intervals
 *
 * Problem Statement:
 * Given an integer array nums, partition it into two (contiguous) subarrays left and right so that:
 * 1. Every element in left is less than or equal to every element in right
 * 2. left and right are non-empty
 * 3. left has the smallest possible size
 *
 * Return the length of left after such a partitioning.
 * It is guaranteed that such a partitioning exists.
 *
 * Example:
 * Input: nums = [5,0,3,8,6]
 * Output: 3
 * Explanation: left = [5,0,3], right = [8,6]
 * The maximum in left is 5, minimum in right is 6. Since 5 <= 6, this partition is valid.
 *
 * Input: nums = [1,1,1,0,6,12]
 * Output: 4
 * Explanation: left = [1,1,1,0], right = [6,12]
 *
 * LeetCode Link: https://leetcode.com/problems/partition-array-into-disjoint-intervals/
 *
 * Follow-up Questions:
 * 1. What if we want to partition into k disjoint intervals instead of 2?
 *    Answer: Use dynamic programming with k states, tracking min/max at each partition boundary.
 * 2. How to handle the case where we want strictly less than instead of less than equal?
 *    Answer: Change condition from max(left) <= min(right) to max(left) < min(right).
 * 3. What if we want to maximize the size of left instead of minimizing?
 *    Answer: Iterate from right to left and find the last valid partition point.
 * 4. How would you handle duplicate elements more efficiently?
 *    Answer: Use coordinate compression or segment trees for range min/max queries.
 *
 * Related Problems:
 * - 768. Max Chunks To Make Sorted II: https://leetcode.com/problems/max-chunks-to-make-sorted-ii/
 * - 1043. Partition Array for Maximum Sum: https://leetcode.com/problems/partition-array-for-maximum-sum/
 * - 132. Palindrome Partitioning II: https://leetcode.com/problems/palindrome-partitioning-ii/
 * LeetCode Contest Rating: 1501
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
