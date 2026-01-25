package arrays;

import java.util.*;

/**
 * Problem: Minimum Moves to Equal Array Elements II
 *
 * Given an integer array nums of size n, return the minimum number of moves required to make
 * all array elements equal. In one move, you can increment or decrement an element of the array by 1.
 *
 * Example:
 * Input: nums = [1,2,3]
 * Output: 2
 * Explanation:
 * Only two moves are needed (remember each move increments or decrements one element):
 * [1,2,3] => [2,2,3] => [2,2,2]
 *
 * Input: nums = [1,10,2,9]
 * Output: 16
 * Explanation: The median is (2+10)/2 = 6 or we can choose 2 or 10. Using median 2:
 * |1-2| + |10-2| + |2-2| + |9-2| = 1 + 8 + 0 + 7 = 16 moves
 *
 * LeetCode: https://leetcode.com/problems/minimum-moves-to-equal-array-elements-ii
 *
 * Follow-up Questions:
 * 1. Q: Why is the median the optimal target value?
 *    A: The median minimizes the sum of absolute deviations. This is a fundamental property
 *       in statistics - any other value would result in more total moves.
 *
 * 2. Q: What if the array has even length - which median to choose?
 *    A: Any value between the two middle elements gives the same result, so we can choose either.
 *
 * 3. Q: How would you handle very large arrays with memory constraints?
 *    A: Use quickselect to find median in O(n) average time without full sorting.
 *
 * 4. Q: What if moves have different costs (increment vs decrement)?
 *    A: Would need to modify the approach to consider asymmetric costs in the optimization.
 *
 * Related Problems:
 * - Minimum Moves to Equal Array Elements: https://leetcode.com/problems/minimum-moves-to-equal-array-elements/
 * - Best Meeting Point: https://leetcode.com/problems/best-meeting-point/
 * - Minimum Operations to Make Array Equal: https://leetcode.com/problems/minimum-operations-to-make-array-equal/
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class MinimumMovesToEqualArrayElementsII {

    /**
     * Finds minimum moves using median as optimal target value.
     *
     * Algorithm:
     * 1. Sort the array to easily find the median
     * 2. Find median element (middle for odd length, either middle for even length)
     * 3. Calculate sum of absolute differences between each element and median
     * 4. Each absolute difference represents moves needed for that element
     *
     * Time Complexity: O(n log n) due to sorting
     * Space Complexity: O(1) if sorting in-place, O(n) if creating new sorted array
     *
     * @param nums array of integers to equalize
     * @return minimum number of moves required
     */
    public int minMoves2(int[] nums) {
        if (nums == null || nums.length <= 1) {
            return 0;
        }

        // Sort array to find median efficiently
        Arrays.sort(nums);

        // Get median value (for even length, either middle element works)
        int median = nums[nums.length / 2];

        // Calculate total moves as sum of absolute differences from median
        int totalMoves = 0;
        for (int num : nums) {
            totalMoves += Math.abs(num - median);
        }

        return totalMoves;
    }

    /**
     * Alternative approach using quickselect to find median without full sorting.
     * More efficient for large arrays where we only need the median.
     *
     * Algorithm:
     * 1. Use quickselect to find median in O(n) average time
     * 2. Calculate sum of absolute differences from median
     * 3. Return total moves needed
     *
     * Time Complexity: O(n) average case, O(n²) worst case
     * Space Complexity: O(log n) for recursion stack in average case
     *
     * @param nums array of integers to equalize
     * @return minimum number of moves required
     */
    public int minMoves2Optimized(int[] nums) {
        if (nums == null || nums.length <= 1) {
            return 0;
        }

        // Find median using quickselect without full sorting
        int medianIndex = nums.length / 2;
        int median = quickSelect(nums, 0, nums.length - 1, medianIndex);

        // Calculate total moves as sum of absolute differences
        int totalMoves = 0;
        for (int num : nums) {
            totalMoves += Math.abs(num - median);
        }

        return totalMoves;
    }

    // Quickselect algorithm to find kth smallest element
    private int quickSelect(int[] nums, int left, int right, int k) {
        if (left == right) {
            return nums[left];
        }

        // Choose random pivot to avoid worst case
        int pivotIndex = left + new Random().nextInt(right - left + 1);
        pivotIndex = partition(nums, left, right, pivotIndex);

        if (k == pivotIndex) {
            return nums[k];
        } else if (k < pivotIndex) {
            return quickSelect(nums, left, pivotIndex - 1, k);
        } else {
            return quickSelect(nums, pivotIndex + 1, right, k);
        }
    }

    // Partition helper for quickselect
    private int partition(int[] nums, int left, int right, int pivotIndex) {
        int pivotValue = nums[pivotIndex];

        // Move pivot to end
        swap(nums, pivotIndex, right);

        // Partition around pivot
        int storeIndex = left;
        for (int i = left; i < right; i++) {
            if (nums[i] < pivotValue) {
                swap(nums, i, storeIndex);
                storeIndex++;
            }
        }

        // Move pivot to final position
        swap(nums, storeIndex, right);
        return storeIndex;
    }

    // Helper method to swap array elements
    private void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
}
