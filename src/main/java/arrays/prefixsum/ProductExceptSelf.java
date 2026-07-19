package arrays.prefixsum;

import java.util.Arrays;
/**
 * Problem: Product of Array Except Self
 *
 * Given an integer array, return an array where each position contains the
 * product of all other values. The Leetcode version requires O(n) time and does
 * not allow division.
 *
 * Leetcode: https://leetcode.com/problems/product-of-array-except-self/ (Medium)
 * Rating:   no contest rating (pre-contest problem)
 * Pattern:  Prefix products | Suffix products | Array preprocessing
 *
 * Example:
 *   Input:  nums = [1,2,3,4]
 *   Output: [24,12,8,6]
 *   Why:    each output multiplies the values before and after that index only.
 *
 * Follow-ups:
 *   1. Use O(1) extra space excluding output?
 *      Store prefix products in answer, then multiply by a running suffix product.
 *   2. Allow division?
 *      Count zeros first; division only works directly when there are no zeros.
 *   3. Support streaming updates?
 *      Maintain a product tree or segment tree with zero counts.
 *
 * Related: Trapping Rain Water (42), Maximum Product Subarray (152).
 */
public class ProductExceptSelf {

    public static void main(String[] args) {
        int[][] inputs = { {1, 2, 3, 4}, {1, 2, 0, 4}, {0, 0} };
        int[][] expected = { {24, 12, 8, 6}, {0, 0, 8, 0}, {0, 0} };

        for (int i = 0; i < inputs.length; i++) {
            int[] got = productExceptSelf(inputs[i]);
            System.out.printf("nums=%s -> output=%s  expected=%s%n",
                Arrays.toString(inputs[i]), Arrays.toString(got), Arrays.toString(expected[i]));
        }
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
 * Intuition: the product except self splits into two independent parts: everything
 * before the index and everything after it. Precompute those two products for
 * every position, then multiply the matching pair.
 *
 * Algorithm:
 *   1. Build left where left[i] is the product before i.
 *   2. Build right where right[i] is the product after i.
 *   3. Set answer[i] to left[i] * right[i] for every index.
 *
 * Time:  O(n) - three linear passes over the array.
 * Space: O(n) - left, right, and answer arrays are allocated.
 *
 * @param nums input array
 * @return products of all values except each index
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
