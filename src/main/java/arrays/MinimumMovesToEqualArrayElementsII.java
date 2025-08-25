package arrays;

import java.util.*;

/**
 * Minimum Moves To Equal Array Elements II
 *
 * Problem: Find minimum moves to make all array elements equal, where one move
 * is incrementing or decrementing an element by 1.
 *
 * Example: nums = [1,2,3] -> Output: 2
 * Move 1 to 2 and 3 to 2. Total moves = |1-2| + |3-2| = 1 + 1 = 2.
 *
 * LeetCode: https://leetcode.com/problems/minimum-moves-to-equal-array-elements-ii
 *
 * Follow-up Questions:
 * - What if move cost varies by element? (Use weighted median)
 * - How to handle floating point targets? (Consider continuous optimization)
 * - What if we want to minimize maximum moves instead of total? (Different optimization problem)
 */
public class MinimumMovesToEqualArrayElementsII {

    /**
     * Finds minimum moves to make all elements equal.
     *
     * Algorithm:
     * 1. The optimal target is the median of the array
     * 2. Sort the array to find median easily
     * 3. Calculate sum of absolute differences from median
     * 4. For even length arrays, any value between two middle elements works
     *
     * Time Complexity: O(n log n) due to sorting
     * Space Complexity: O(1) if sorting in-place
     *
     * @param nums input array of integers
     * @return minimum moves to make all elements equal
     */
    public int minMoves2(int[] nums) {
        Arrays.sort(nums);
        int n = nums.length;
        int median = nums[n / 2]; // For even length, this chooses the upper median

        int totalMoves = 0;
        for (int num : nums) {
            totalMoves += Math.abs(num - median);
        }

        return totalMoves;
    }

    /**
     * Optimized approach without finding explicit median
     * Uses two pointers from ends moving toward center
     * Time Complexity: O(n log n), Space Complexity: O(1)
     */
    public int minMoves2TwoPointers(int[] nums) {
        Arrays.sort(nums);

        int totalMoves = 0;
        int left = 0;
        int right = nums.length - 1;

        // Pair elements from both ends
        while (left < right) {
            totalMoves += nums[right] - nums[left];
            left++;
            right--;
        }

        return totalMoves;
    }

    /**
     * Quick select approach to find median without full sorting
     * Time Complexity: O(n) average case, O(n²) worst case
     * Space Complexity: O(1)
     */
    public int minMoves2QuickSelect(int[] nums) {
        int n = nums.length;
        int median = quickSelect(nums, 0, n - 1, n / 2);

        int totalMoves = 0;
        for (int num : nums) {
            totalMoves += Math.abs(num - median);
        }

        return totalMoves;
    }

    // Quick select implementation to find kth smallest element
    private int quickSelect(int[] nums, int left, int right, int k) {
        if (left == right) return nums[left];

        int pivotIndex = partition(nums, left, right);

        if (k == pivotIndex) {
            return nums[k];
        } else if (k < pivotIndex) {
            return quickSelect(nums, left, pivotIndex - 1, k);
        } else {
            return quickSelect(nums, pivotIndex + 1, right, k);
        }
    }

    // Partition method for quick select
    private int partition(int[] nums, int left, int right) {
        int pivot = nums[right];
        int i = left;

        for (int j = left; j < right; j++) {
            if (nums[j] <= pivot) {
                swap(nums, i, j);
                i++;
            }
        }

        swap(nums, i, right);
        return i;
    }

    // Helper method to swap array elements
    private void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }

    /**
     * Mathematical proof approach - demonstrating why median is optimal
     * Time Complexity: O(n log n), Space Complexity: O(1)
     */
    public int minMoves2WithExplanation(int[] nums) {
        Arrays.sort(nums);
        int n = nums.length;

        // The median minimizes sum of absolute deviations
        int median = nums[n / 2];

        int totalMoves = 0;
        for (int num : nums) {
            totalMoves += Math.abs(num - median);
        }

        return totalMoves;
    }

    /**
     * Alternative median finding for even-length arrays
     * Shows that any value between two middle elements gives same result
     */
    public int minMoves2EvenHandling(int[] nums) {
        Arrays.sort(nums);
        int n = nums.length;

        int median;
        if (n % 2 == 1) {
            median = nums[n / 2];
        } else {
            // For even length, both middle elements give same total cost
            // We can choose either nums[n/2 - 1] or nums[n/2]
            median = nums[n / 2 - 1]; // Lower median
        }

        int totalMoves = 0;
        for (int num : nums) {
            totalMoves += Math.abs(num - median);
        }

        return totalMoves;
    }
}
