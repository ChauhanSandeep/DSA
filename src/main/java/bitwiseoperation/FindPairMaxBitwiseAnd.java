package bitwiseoperation;

import java.util.Arrays;

/**
 * Problem: Find Pair With Maximum Bitwise AND
 *
 * Given an array of integers, return the largest value obtainable as
 * nums[i] & nums[j] for two different indices. The pair itself does not need to
 * be returned.
 *
 * Leetcode: Not available (practice variant)
 * Rating:   N/A (custom practice problem)
 * Pattern:  Bit manipulation | Greedy high-bit feasibility | Pair AND
 *
 * Example:
 *   Input:  nums = [5, 8, 7, 2]
 *   Output: 5
 *   Why:    5 & 7 keeps bits 0101, while the 8-bit cannot be shared by any pair.
 *
 * Follow-ups:
 *   1. How would you return the actual pair of indices?
 *      Track two indices while counting numbers that contain the candidate pattern.
 *   2. How would you find a maximum AND triplet instead of a pair?
 *      Run the same bit-feasibility test but require at least three matching numbers.
 *   3. What changes when negative numbers are allowed?
 *      Decide signed versus unsigned ordering first, then handle the sign bit explicitly.
 *   4. How would you answer many queries over changing arrays?
 *      Maintain counts in a binary trie or segment tree keyed by high-bit prefixes.
 *
 * Related: Maximum XOR of Two Numbers in an Array (421).
 */
public class FindPairMaxBitwiseAnd {

    public static void main(String[] args) {
        FindPairMaxBitwiseAnd solver = new FindPairMaxBitwiseAnd();

        int[][] inputs = {
            {5, 8, 7, 2},
            {1},
            {12, 4, 8, 6}
        };
        int[] expected = {5, 0, 8};

        for (int i = 0; i < inputs.length; i++) {
            int output = solver.maxBitwiseAnd(inputs[i]);
            System.out.printf("nums=%s -> %d  expected=%d%n",
                Arrays.toString(inputs[i]), output, expected[i]);
        }
    }

    /**
     * Intuition: higher bits are worth more than all lower bits combined, so the
     * original code greedily asks whether each bit can be kept in the answer. A
     * candidate pattern is feasible only when at least two numbers contain every
     * bit already chosen plus the current bit. If two numbers match that pattern,
     * their AND can preserve it; otherwise this bit cannot be part of the maximum.
     *
     * Algorithm:
     *   1. Return 0 when fewer than two numbers are available.
     *   2. Scan bit positions from 31 down to 0 and add the current bit to result as pattern.
     *   3. Count numbers whose set bits include every bit in pattern.
     *   4. Keep pattern as the result only when at least two numbers match it.
     *
     * Time:  O(32 * n) - the code scans all numbers once for each of the 32 bit positions.
     * Space: O(1) - only masks and counters are stored.
     *
     * @param nums array of integers
     * @return maximum bitwise AND value achievable by any pair
     */
    public int maxBitwiseAnd(int[] nums) {
        if (nums == null || nums.length < 2) {
            return 0;
        }

        int result = 0;

        // Process each bit from MSB (31) to LSB (0)
        for (int bit = 31; bit >= 0; bit--) {
            int pattern = result | (1 << bit); // Candidate pattern with current bit set
            // result | (1 << bit);  will set the current bit in the result pattern

            // Count how many elements match the current pattern
            int matchCount = 0;
            for (int num : nums) {
                if ((num & pattern) == pattern) { // Check if num has all bits of pattern set
                matchCount++;
                }
            }

            // If at least 2 elements have this bit set, include it in result
            if (matchCount >= 2) {
                result = pattern;
            }
        }

        return result;
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