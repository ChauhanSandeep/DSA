package arrays.twopointers;

import java.util.Arrays;

/**
 * Problem: Two Sum II - Input Array Is Sorted
 *
 * Given a 1-indexed sorted array, return the 1-indexed positions of two numbers
 * that sum to target. The solution must use constant extra space.
 *
 * Leetcode: https://leetcode.com/problems/two-sum-ii-input-array-is-sorted/ (Medium)
 * Rating:   acceptance 65.5% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Array | Two pointers | Sorted complement search
 *
 * Example:
 *   Input:  numbers = [2,7,11,15], target = 9
 *   Output: [1,2]
 *   Why:    numbers[0] + numbers[1] = 9, and the problem asks for 1-indexed positions.
 *
 * Follow-ups:
 *   1. Return all unique pairs?
 *      Continue after a match and skip duplicate values on both sides.
 *   2. Find a pair closest to target?
 *      Track the smallest absolute difference while moving pointers by sum direction.
 *   3. What if the array is unsorted?
 *      Use a hash map, or sort value-index pairs if original indices must be returned.
 *
 * Related: Two Sum (1), 3Sum (15), 3Sum Closest (16).
 */
public class TwoSumIIInputArrayIsSorted {

public static void main(String[] args) {
    TwoSumIIInputArrayIsSorted solver = new TwoSumIIInputArrayIsSorted();
    int[][] inputs = { {2, 7, 11, 15}, {2, 3, 4} };
    int[] targets = { 9, 6 };
    int[][] expected = { {1, 2}, {1, 3} };

    for (int i = 0; i < inputs.length; i++) {
        int[] got = solver.twoSum(inputs[i], targets[i]);
        System.out.printf("numbers=%s target=%d -> %s  expected=%s%n",
            Arrays.toString(inputs[i]), targets[i], Arrays.toString(got), Arrays.toString(expected[i]));
    }
}

    /**
 * Intuition: in sorted order, the smallest remaining value is at left and the
 * largest is at right. If their sum is too small, only increasing left can
 * help; if the sum is too large, only decreasing right can help.
 *
 * Algorithm:
 *   1. Start left at 0 and right at numbers.length - 1.
 *   2. Compute numbers[left] + numbers[right].
 *   3. Return 1-indexed positions when the sum equals target.
 *   4. Move left rightward for a small sum, or right leftward for a large sum.
 *
 * Time:  O(n) - each pointer moves inward at most n times total.
 * Space: O(1) - only two pointers and the sum are stored.
 *
 * @param numbers sorted input values
 * @param target desired pair sum
 * @return 1-indexed positions of the pair, or {-1, -1} if none is found
 */
    public int[] twoSum(int[] numbers, int target) {
        int left = 0;
        int right = numbers.length - 1;

        while (left < right) {
            int sum = numbers[left] + numbers[right];

            if (sum == target) {
                return new int[]{left + 1, right + 1};
            } else if (sum < target) {
                left++;
            } else {
                right--;
            }
        }

        return new int[]{-1, -1};
    }

    /**
     * Alternative approach using binary search for each element.
     * For each element, binary search for its complement in remaining array.
     *
     * Algorithm:
     * 1. Iterate through each element at index i
     * 2. Calculate complement = target - numbers[i]
     * 3. Binary search for complement in subarray [i+1, n-1]
     * 4. If found, return 1-indexed positions of both elements
     *
     * Time Complexity: O(N log N) where N is array length. For each of N elements,
     * we perform binary search taking O(log N).
     *
     * Space Complexity: O(1) excluding recursion stack for binary search.
     *
     * Note: While this approach works, the two-pointer method is more efficient with
     * O(N) time complexity. Binary search approach is included to demonstrate alternatives.
     *
     * @param numbers sorted array of integers
     * @param target the target sum to find
     * @return array containing 1-indexed positions of the two numbers
     */
    public int[] twoSumBinarySearch(int[] numbers, int target) {
        for (int i = 0; i < numbers.length - 1; i++) {
            int complement = target - numbers[i];
            int complementIndex = binarySearch(numbers, i + 1, numbers.length - 1, complement);

            if (complementIndex != -1) {
                return new int[]{i + 1, complementIndex + 1};
            }
        }

        return new int[]{-1, -1};
    }

    // Helper method for binary search in a range
    private int binarySearch(int[] arr, int left, int right, int target) {
        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (arr[mid] == target) {
                return mid;
            } else if (arr[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return -1;
    }
}
