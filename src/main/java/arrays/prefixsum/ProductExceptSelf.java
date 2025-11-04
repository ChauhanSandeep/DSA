package arrays.prefixsum;

import java.util.Arrays;

/**
 * Given an integer array nums, return an array such that answer[i] is the product of all elements except nums[i].
 * You must write an algorithm that runs in O(n) time and without using the division operation.
 *
 * Leetcode link: https://leetcode.com/problems/product-of-array-except-self/description/
 */
public class ProductExceptSelf {
    public static void main(String[] args) {
        int[] nums = {1,2,3,4};
        System.out.println("Optimized Output: " + Arrays.toString(productExceptSelf(nums)));
    }

    /**
     * Division approach handling zeros (WORKS but still not allowed by problem).
     *
     * Algorithm:
     * 1. Count zeros in array
     * 2. If multiple zeros: all answers are 0
     * 3. If one zero:
     *    - Product of all non-zero elements goes to zero position
     *    - All other positions are 0
     * 4. If no zeros: use division for all
     *
     * Time Complexity: O(N)
     * Space Complexity: O(1) auxiliary space
     *
     * @param nums array of integers
     * @return array where answer[i] = product of all nums except nums[i]
     */
    public int[] productExceptSelfDivisionWithZeros(int[] nums) {
        int n = nums.length;
        int[] answer = new int[n];

        // Count zeros
        int zeroCount = 0;
        int zeroIndex = -1;
        long productNonZero = 1;

        for (int i = 0; i < n; i++) {
            if (nums[i] == 0) {
                zeroCount++;
                zeroIndex = i;
            } else {
                productNonZero *= nums[i];
            }
        }

        // Case 1: Multiple zeros
        if (zeroCount > 1) {
            // All products are 0 because we'll always encounter a zero
            return answer;  // Already initialized with zeros
        }

        // Case 2: Exactly one zero
        if (zeroCount == 1) {
            answer[zeroIndex] = (int)productNonZero;
            return answer;  // All other positions already 0
        }

        // Case 3: No zeros - use division
        for (int i = 0; i < n; i++) {
            answer[i] = (int)(productNonZero / nums[i]);
        }

        return answer;
    }

    /**
     * Finds product of array except self using prefix and suffix approach.
     *
     * Algorithm:
     * 1. Create left array: left[i] = product of all elements before index i
     *    - left[0] = 1 (nothing before first element)
     *    - left[i] = left[i-1] * nums[i-1]
     *
     * 2. Create right array: right[i] = product of all elements after index i
     *    - right[n-1] = 1 (nothing after last element)
     *    - right[i] = right[i+1] * nums[i+1]
     *
     * 3. For each position i:
     *    - answer[i] = left[i] * right[i]
     *    - This represents product of all elements except nums[i]
     *
     * Key insight: The product of all elements except self can be decomposed as:
     * - Product of all elements to the left
     * - Multiplied by product of all elements to the right
     *
     * Time Complexity: O(N) - three passes (build left, build right, calculate answer)
     * Space Complexity: O(N) for left and right arrays (not counting output).
     *
     * @param nums array of integers
     * @return array where answer[i] = product of all nums except nums[i]
     */
    public static int[] productExceptSelf(int[] nums) {
        int n = nums.length;
        int[] left = new int[n];
        int[] right = new int[n];
        int[] answer = new int[n];

        // Build left array: left[i] = product of all elements before i
        left[0] = 1;
        for (int i = 1; i < n; i++) {
            left[i] = left[i - 1] * nums[i - 1];
        }

        // Build right array: right[i] = product of all elements after i
        right[n - 1] = 1;
        for (int i = n - 2; i >= 0; i--) {
            right[i] = right[i + 1] * nums[i + 1];
        }

        // Calculate answer: answer[i] = left[i] * right[i]
        for (int i = 0; i < n; i++) {
            answer[i] = left[i] * right[i];
        }

        return answer;
    }
}
