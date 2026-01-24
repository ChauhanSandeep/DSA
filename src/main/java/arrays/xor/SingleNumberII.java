package arrays.xor;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 137. Single Number II
 *
 * Problem Statement:
 * Given an integer array nums where every element appears exactly three times
 * except for one element that appears exactly once. Find the single element
 * and return it. You must implement a solution with a linear runtime complexity
 * and use only constant extra space.
 *
 * Example:
 * Input: nums = [2,2,3,2]
 * Output: 3
 * Explanation: Element 3 appears only once while 2 appears exactly three times.
 *
 * Input: nums = [0,1,0,1,0,1,99]
 * Output: 99
 * Explanation: Elements 0 and 1 appear exactly three times each, while 99 appears once.
 *
 * LeetCode Link: https://leetcode.com/problems/single-number-ii/
 *
 * Follow-up Questions:
 * 1. What if every element appears k times except one that appears once?
 *    Answer: Use bit counting approach with modulo k arithmetic for each bit position.
 * 2. What if there are two elements that appear once and others appear three times?
 *    Answer: Combine bit manipulation techniques from Single Number II and III approaches.
 * 3. How would you handle the case where elements appear different numbers of times?
 *    Answer: Use frequency counting with HashMap or extend bit manipulation for multiple patterns.
 * 4. What if we need to find all elements that don't follow the pattern?
 *    Answer: Use bit manipulation with multiple state tracking or frequency analysis.
 *
 * Related Problems:
 * - 136. Single Number: https://leetcode.com/problems/single-number/
 * - 260. Single Number III: https://leetcode.com/problems/single-number-iii/
 * - 645. Set Mismatch: https://leetcode.com/problems/set-mismatch/
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class SingleNumberII {

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
     * Finds single number using bit manipulation with modular arithmetic.
     *
     * Algorithm:
     * 1. For each bit position, count how many numbers have that bit set
     * 2. If count % 3 != 0, this means that in the result single number, this bit is set
     * 3. Reconstruct the single number from these bits
     * 4. Works because numbers appearing 3 times contribute 0 or 3 to each bit count
     *
     * Time Complexity: O(n) where n is array length
     * Space Complexity: O(1) - only using constant extra space
     *
     * @param nums array where every element appears 3 times except one
     * @return the element that appears exactly once
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
