package arrays.xor;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Problem: Single Number II
 *
 * Given an integer array where every value appears exactly three times except one
 * value that appears once, return the single value. The intended solution should
 * run in linear time and use constant extra space.
 *
 * Leetcode: https://leetcode.com/problems/single-number-ii/
 * Rating:   acceptance 67.5% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Arrays | Bit manipulation | Bit counts modulo 3
 *
 * Example:
 *   Input:  [2,2,3,2]
 *   Output: 3
 *   Why:    the three copies of 2 contribute multiples of three to every bit
 *           count, so only 3 leaves non-zero remainders.
 *
 * Follow-ups:
 *   1. What if every other value appears k times?
 *      Count each bit modulo k and rebuild the value from non-zero remainders.
 *   2. What if two values appear once while others appear three times?
 *      First identify distinguishing bits, then combine state tracking per group.
 *   3. Can you do this with two bitmasks instead of 32 counters?
 *      Track bits seen once and twice, clearing them when they are seen a third time.
 *
 * Related: Single Number (136), Single Number III (260).
 */
public class SingleNumberII {

    public static void main(String[] args) {
        SingleNumberII solver = new SingleNumberII();
        int[][] inputs = {{2, 2, 3, 2}, {0, 1, 0, 1, 0, 1, 99}, {-2, -2, 1, -2}};
        int[] expected = {3, 99, 1};

        for (int i = 0; i < inputs.length; i++) {
            int got = solver.singleNumber(inputs[i]);
            System.out.printf("nums=%s -> %d  expected=%d%n",
                Arrays.toString(inputs[i]), got, expected[i]);
        }
    }


    /**
     * Find single number using maths.
     * 
     * Formula
     * -----------
     * 3 * (x + y + z + ...) + singleNumber = sumOfAllElements
     * 
     * x + y + z + ... + singleNumber = sumOfUniqueElements
     * 3 * (x + y + z + ...) + 3 * singleNumber = 3 * sumOfUniqueElements
     * 
     * Subtracting the two equations:
     * -----------
     * 2 * singleNumber = 3 * sumOfUniqueElements - sumOfAllElements
     * -----------
     * Rearranging gives:
     * singleNumber = (3 * sumOfUniqueElements - sumOfAllElements) / 2
     * 
     * Algorithm:
     * 1. Sum all unique elements and multiply by 3
     * 2. Subtract sum of all elements from this value
     * 3. The difference divided by 2 gives the single number
     * 
     * Time Complexity: O(n) where n is array length
     * Space Complexity: O(n) for storing unique elements
     * @param nums
     * @return
     */
    public int singleNumberMaths(int[] nums) {
        Set<Integer> uniqueElements = Arrays.stream(nums).boxed().collect(Collectors.toSet());
        int sumOfUniqueElements = uniqueElements.stream().mapToInt(Integer::intValue).sum();

        // Calculate sum of all elements
        int sumOfAllElements = Arrays.stream(nums).sum();

        // Using the formula: (3 * sumOfUniqueElements - sumOfAllElements) / 2
        return (3 * sumOfUniqueElements - sumOfAllElements) / 2;
    }

    /**
     * Intuition (interview default): look at one bit position at a time. Every
     * number that appears three times contributes either zero set bits or three set
     * bits at that position, so its contribution disappears modulo 3. If the final
     * count at a bit position has remainder 1, the unique number must have that bit
     * set. Repeating this for all 32 integer bits reconstructs the exact answer,
     * including negative numbers because the sign bit is processed too.
     *
     * Time:  O(n) - the 32 bit positions are constant, so all numbers are scanned a constant number of times.
     * Space: O(1) - only counters and the reconstructed result are stored.
     *
     * @param nums array where every value appears three times except one
     * @return value that appears exactly once
     */
    public int singleNumber(int[] nums) {
        int result = 0;

        // Check each bit position (32 bits for int)
        for (int bitPosition = 0; bitPosition < 32; bitPosition++) {
            int count = 0;

            // Count how many numbers have this bit set
            for (int num : nums) {
                if (((num >> bitPosition) & 1) == 1) { // check if 1 is set in numPosition
                    count++;
                }
            }

            // If count is not divisible by 3, single number has this bit
            if (count % 3 != 0) {
                result |= (1 << bitPosition); // set this bit in bitPosition of result
            }
        }

        return result;
    }

    /**
     * Generalized approach for any k repetitions.
     * Problem statement: Given an integer array nums where every element appears exactly k times
     * except for one element that appears exactly once. Find the single element and return it.
     * You must implement a solution with a linear runtime complexity and use only constant extra space.
     *
     * Algorithm:
     * 1. For each bit position, count how many numbers have that bit set
     * 2. If count % k != 0, this means that in the result single number, this bit is set
     * 3. Reconstruct the single number from these bits
     * 4. Works because numbers appearing k times contribute 0 or k to each bit count
     *
     * Time Complexity: O(n * log k), Space Complexity: O(1)
     */
    public int singleNumberGeneralized(int[] nums, int k) {
        int result = 0;

        for (int bit = 0; bit < 32; bit++) {
            int count = 0;

            for (int num : nums) {
                if (((num >> bit) & 1) == 1) {
                    count++;
                }
            }

            if (count % k != 0) {
                result |= (1 << bit);
            }
        }

        return result;
    }
}
