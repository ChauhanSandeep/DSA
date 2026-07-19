package arrays;

import java.util.*;

/**
 * Problem: Minimum Moves to Equal Array Elements II
 *
 * In one move, you may increment or decrement one array element by 1. Return the
 * minimum total moves needed to make every element equal. The target value may be
 * any integer, not necessarily one already chosen ahead of time.
 *
 * Leetcode: https://leetcode.com/problems/minimum-moves-to-equal-array-elements-ii/ (Medium)
 * Rating:   acceptance 62.2% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Array | Sorting | Median minimizes absolute distance
 *
 * Example:
 *   Input:  nums = [1,10,2,9]
 *   Output: 16
 *   Why:    any value between the two middle sorted values 2 and 9 is optimal;
 *           choosing 2 costs 1 + 8 + 0 + 7 = 16 moves.
 *
 * Follow-ups:
 *   1. Find the target without sorting the whole array?
 *      Use quickselect to find a median in O(n) average time.
 *   2. What if increment and decrement have different costs?
 *      The optimal target shifts to a weighted quantile instead of the ordinary median.
 *   3. Make all elements equal using only increment operations?
 *      Then the target must be the maximum value, matching Minimum Moves I's complement trick.
 *
 * Related: Minimum Moves to Equal Array Elements (453), Best Meeting Point (296).
 */
public class MinimumMovesToEqualArrayElementsII {

    public static void main(String[] args) {
        MinimumMovesToEqualArrayElementsII solver = new MinimumMovesToEqualArrayElementsII();

        int[][] inputs = { {1, 2, 3}, {1, 10, 2, 9}, {7} };
        int[] expected = { 2, 16, 0 };

        for (int i = 0; i < inputs.length; i++) {
            int got = solver.minMoves2(inputs[i].clone());
            System.out.printf("nums=%s  ->  %d  expected=%d%n",
                Arrays.toString(inputs[i]), got, expected[i]);
        }
    }

    /**
     * Intuition: the sum of absolute distances is minimized at a median. Moving the
     * target left of the median leaves more numbers on the right pulling it back, and
     * moving it right leaves more numbers on the left pulling it back. Sorting exposes
     * the median, then every element contributes its distance from that target.
     *
     * Algorithm:
     *   1. Return 0 for null or single-element input.
     *   2. Sort nums.
     *   3. Pick nums[nums.length / 2] as the median target.
     *   4. Sum absolute differences from the median and return the total.
     *
     * Time:  O(n log n) - sorting dominates the final linear sum.
     * Space: O(1) - aside from the in-place sort, only the median and total are stored.
     *
     * @param nums values to make equal, mutated by sorting
     * @return minimum number of +/-1 moves needed
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
