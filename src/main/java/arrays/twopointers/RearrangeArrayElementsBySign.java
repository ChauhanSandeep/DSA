package arrays.twopointers;

import java.util.*;

/**
 * Problem: Rearrange Array Elements by Sign
 *
 * You are given a 0-indexed integer array nums of even length consisting of an equal number
 * of positive and negative integers. You should rearrange the elements of nums such that the
 * modified array follows the given conditions:
 *
 * 1. Every consecutive pair of integers have opposite signs
 * 2. For all integers with the same sign, the order in which they were present in nums is preserved
 * 3. The rearranged array begins with a positive integer
 *
 * Return the array that satisfies the above conditions.
 *
 * Example:
 * Input: nums = [3,1,-2,-5,2,-4]
 * Output: [3,-2,1,-5,2,-4]
 * Explanation:
 * - Positive numbers in order: [3,1,2]
 * - Negative numbers in order: [-2,-5,-4]
 * - Alternating pattern: [3,-2,1,-5,2,-4]
 *
 * Input: nums = [-1,1]
 * Output: [1,-1]
 * Explanation: Positive first, then negative: [1,-1]
 *
 * LeetCode: https://leetcode.com/problems/rearrange-array-elements-by-sign
 *
 * Follow-up Questions:
 * 1. Q: What if positive and negative counts are not equal?
 *    A: Would need to handle remaining elements separately at the end of array.
 *
 * 2. Q: What if we wanted to start with negative numbers instead?
 *    A: Simply swap the initial positions: negatives at even indices, positives at odd.
 *
 * 3. Q: How would you solve this in-place without extra space?
 *    A: More complex - would require cyclic replacements or multiple passes with careful index management.
 *
 * 4. Q: What if there were three types of elements (positive, negative, zero)?
 *    A: Would need a three-pointer approach to manage placement of each type.
 *
 * Related Problems:
 * - Sort Array by Parity: https://leetcode.com/problems/sort-array-by-parity/
 * - Sort Array by Parity II: https://leetcode.com/problems/sort-array-by-parity-ii/
 * - Shuffle the Array: https://leetcode.com/problems/shuffle-the-array/
 */
public class RearrangeArrayElementsBySign {

    /**
     * Rearranges array using two-pointer technique for optimal placement.
     *
     * Algorithm:
     * 1. Create result array of same size as input
     * 2. Use two pointers: one for even indices (positives), one for odd indices (negatives)
     * 3. Iterate through original array once
     * 4. Place positive numbers at even indices (0,2,4...), increment by 2
     * 5. Place negative numbers at odd indices (1,3,5...), increment by 2
     * 6. Return rearranged array
     *
     * Time Complexity: O(n) where n is length of array
     * Space Complexity: O(1) excluding output array
     *
     * @param nums array with equal positive and negative integers
     * @return rearranged array with alternating signs starting with positive
     */
    public int[] rearrangeArray(int[] nums) {
        if (nums == null || nums.length == 0) {
            return new int[0];
        }

        int length = nums.length;
        int[] result = new int[length];

        // Two pointers: even indices for positives, odd indices for negatives
        int positiveIndex = 0;  // Points to even positions: 0, 2, 4, ...
        int negativeIndex = 1;  // Points to odd positions: 1, 3, 5, ...

        // Single pass through original array
        for (int currentNumber : nums) {
            if (currentNumber > 0) {
                // Place positive number at next even index
                result[positiveIndex] = currentNumber;
                positiveIndex += 2;
            } else {
                // Place negative number at next odd index
                result[negativeIndex] = currentNumber;
                negativeIndex += 2;
            }
        }

        return result;
    }

    /**
     * Alternative approach using separate lists for positive and negative numbers.
     * More intuitive but slightly less space efficient due to additional lists.
     *
     * Algorithm:
     * 1. Separate positive and negative numbers into different lists
     * 2. Create result array and alternate between the two lists
     * 3. Place positive numbers at even indices, negative at odd indices
     * 4. Maintain relative order within each sign group
     *
     * Time Complexity: O(n) where n is length of array
     * Space Complexity: O(n) for the lists and result array
     *
     * @param nums array with equal positive and negative integers
     * @return rearranged array with alternating signs starting with positive
     */
    public int[] rearrangeArrayWithLists(int[] nums) {
        if (nums == null || nums.length == 0) {
            return new int[0];
        }

        List<Integer> positiveNumbers = new ArrayList<>();
        List<Integer> negativeNumbers = new ArrayList<>();

        // Separate numbers by sign while preserving order
        for (int number : nums) {
            if (number > 0) {
                positiveNumbers.add(number);
            } else {
                negativeNumbers.add(number);
            }
        }

        int[] result = new int[nums.length];
        int resultIndex = 0;

        // Alternate between positive and negative numbers
        for (int i = 0; i < positiveNumbers.size(); i++) {
            // Place positive at even index
            result[resultIndex++] = positiveNumbers.get(i);
            // Place negative at odd index
            result[resultIndex++] = negativeNumbers.get(i);
        }

        return result;
    }
}
