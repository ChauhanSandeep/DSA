package bitwiseoperation;

import java.util.*;

/**
 * 🔍 Problem Statement:
 * Given an array of integers, find the maximum AND value of any two elements.
 *
 * ✅ Example:
 * Input: [5, 8, 7, 2]
 * Output: 5
 * Explanation: The maximum AND is obtained from (5 & 7) = 5.
 *
 * 🔗 Similar Problem:
 * - Not a direct LeetCode problem, but a variation can be found here:
 *   https://www.geeksforgeeks.org/find-pair-max-bitwise-and/
 *
 * 🧠 Follow-up Questions:
 * 1. Can you return the actual pair that gives the max AND?
 *    ➤ Yes, store the pair when the pattern is matched during the loop.
 * 2. What if you need the top-K such AND values?
 *    ➤ Use a min-heap of size K while scanning all pairs — O(n² log k).
 */
public class MaximumAnd {

    public static void main(String[] args) {
        int[] input = {5, 8, 7, 2};
        int maxAndValue = new MaximumAnd().findMaximumAndValue(input);
        System.out.println("Maximum AND Value: " + maxAndValue);
    }

    /**
     * Finds the maximum AND value obtainable by any two numbers in the array.
     *
     * @param nums Array of non-negative integers
     * @return Maximum AND value between any two elements
     *
     * Approach:
     * - Iterate from the highest bit to the lowest (31 to 0 for int)
     * - At each bit position, try setting that bit in a temporary candidate
     * - Check how many numbers in the array have this bit set
     * - If ≥ 2 numbers have the candidate pattern, keep that bit in result
     *
     * Time Complexity: O(32 * n) = O(n)
     * Space Complexity: O(1)
     */
    public int findMaximumAndValue(int[] nums) {
        int result = 0;

        for (int bit = 31; bit >= 0; bit--) {
            // Try setting this bit in the result
            int candidate = result | (1 << bit);
            int count = countWithBitPattern(nums, candidate);

            if (count >= 2) {
                result = candidate; // Retain the bit if at least 2 numbers match
            }
        }

        return result;
    }

    /**
     * Counts how many numbers in the array have all bits set that are set in the given pattern.
     *
     * @param nums     The input array
     * @param pattern  The candidate bit pattern
     * @return Count of numbers matching the pattern
     */
    private int countWithBitPattern(int[] nums, int pattern) {
        int count = 0;
        for (int num : nums) {
            if ((num & pattern) == pattern) {
                count++;
            }
        }
        return count;
    }

    /**
     * Alternate Brute-force method (for interview comparison):
     * Compares all pairs to find max AND.
     *
     * @param nums Input array
     * @return Max AND value from all pairs
     *
     * Time Complexity: O(n²)
     * Space Complexity: O(1)
     */
    public int bruteForceMaxAnd(int[] nums) {
        int maxAnd = 0;
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                maxAnd = Math.max(maxAnd, nums[i] & nums[j]);
            }
        }
        return maxAnd;
    }
}