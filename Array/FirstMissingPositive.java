package Array;

import java.util.Arrays;

/**
 * Problem: First Missing Positive
 * Leetcode: https://leetcode.com/problems/first-missing-positive/
 *
 * Given an unsorted integer array, find the smallest missing positive integer.
 * The array can contain duplicates and negative numbers.
 * You must implement an algorithm that runs in O(n) time and uses constant extra space.
 *
 * 📌 Example:
 * Input: [3, 4, -1, 1]
 * Output: 2
 *
 * Input: [1, 2, 0]
 * Output: 3
 *
 * Follow-up Questions:
 * 1. Can you use a HashSet?
 *    → Yes, but violates constant space constraint.
 * 2. Can you use a modified cyclic sort instead of marking?
 *    → Yes, possible alternative.
 */
public class FirstMissingPositive {

    public static void main(String[] args) {
        int[] arr = {1, 2, 0};
        int result = new FirstMissingPositive().firstMissingPositive(arr);
        System.out.println("First missing positive number in array is " + result);
    }

    /**
     * Finds the first missing positive number from an unsorted array using index marking.
     *
     * 🔹 Algorithm Overview:
     * - Ignore non-positive numbers and numbers greater than the array size (they can't affect the result).
     * - Use index marking by negating the value at the position corresponding to each number.
     * - The first index with a positive number indicates the missing value.
     *
     * 🔹 Steps:
     * 1. Check if 1 is missing → return 1 early.
     * 2. Normalize invalid numbers (<=0 or > N) to 1.
     * 3. Use array index as a hash to mark presence via negative values.
     * 4. Scan array to find first index with a positive value.
     *
     * 🔹 Time Complexity: O(N)
     * 🔹 Space Complexity: O(1) (in-place modification)
     *
     * @param nums the input array of integers
     * @return the smallest missing positive integer
     */
    public int firstMissingPositive(int[] nums) {
        int length = nums.length;

        // Step 1: Check if 1 is present
        boolean hasOne = false;
        for (int num : nums) {
            if (num == 1) {
                hasOne = true;
                break;
            }
        }
        if (!hasOne) return 1;

        // Step 2: Replace invalid elements (≤0 or > N) with 1
        for (int i = 0; i < length; i++) {
            if (nums[i] <= 0 || nums[i] > length) {
                nums[i] = 1;
            }
        }

        // Step 3: Use index marking (negate value at index = num) to indicate presence
        for (int i = 0; i < length; i++) {
            int currentValue = Math.abs(nums[i]);

            // Mark nums[currentValue] as negative to indicate presence of currentValue
            if (currentValue == length) {
                // Special case: use index 0 to mark presence of length
                nums[0] = -Math.abs(nums[0]);
            } else {
                nums[currentValue] = -Math.abs(nums[currentValue]);
            }
        }

        // Step 4: First index with positive value => missing number
        for (int i = 1; i < length; i++) {
            if (nums[i] > 0) {
                return i;
            }
        }

        // Check for presence of length
        if (nums[0] > 0) return length;

        // All 1 to N present
        return length + 1;
    }
}