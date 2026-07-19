package arrays.twopointers;

import java.util.*;

/**
 * Problem: Rearrange Array Elements by Sign
 *
 * Given an even-length array with the same number of positive and negative
 * values, rearrange it so signs alternate, the first value is positive, and the
 * relative order within each sign is preserved.
 *
 * Leetcode: https://leetcode.com/problems/rearrange-array-elements-by-sign/ (Medium)
 * Rating:   zerotrac 1236 (Q2, weekly-contest-277)
 * Pattern:  Array | Two write pointers | Stable sign partition
 *
 * Example:
 *   Input:  nums = [3,1,-2,-5,2,-4]
 *   Output: [3,-2,1,-5,2,-4]
 *   Why:    positives stay [3,1,2], negatives stay [-2,-5,-4], and signs alternate from positive.
 *
 * Follow-ups:
 *   1. What if positive and negative counts differ?
 *      Alternate while both signs remain, then append the leftover sign group by rule.
 *   2. Start with a negative value instead?
 *      Swap the initial write positions so negatives write to even indices.
 *   3. Can this be stable and in-place?
 *      Yes, but it needs rotations/cyclic moves and is more complex than the output-array solution.
 *
 * Related: Sort Array by Parity II (922), Shuffle the Array (1470).
 */
public class RearrangeArrayElementsBySign {

public static void main(String[] args) {
    RearrangeArrayElementsBySign solver = new RearrangeArrayElementsBySign();
    int[][] inputs = { {3, 1, -2, -5, 2, -4}, {-1, 1} };
    int[][] expected = { {3, -2, 1, -5, 2, -4}, {1, -1} };

    for (int i = 0; i < inputs.length; i++) {
        int[] got = solver.rearrangeArray(inputs[i]);
        System.out.printf("nums=%s -> %s  expected=%s%n",
            Arrays.toString(inputs[i]), Arrays.toString(got), Arrays.toString(expected[i]));
    }
}

    /**
 * Intuition: the output positions are known before scanning. Positive values
 * must occupy even indices and negative values must occupy odd indices. Two
 * write pointers preserve order because each sign group is written exactly in
 * the order encountered in the original array.
 *
 * Algorithm:
 *   1. Create a result array of the same length.
 *   2. Start positiveIndex at 0 and negativeIndex at 1.
 *   3. Scan nums once, writing positives to positiveIndex and negatives to negativeIndex.
 *   4. Advance the matching index by 2 after each write and return result.
 *
 * Time:  O(n) - each input value is read and written once.
 * Space: O(1) - excluding the required output array, only two indices are kept.
 *
 * @param nums array with equal positive and negative counts
 * @return rearranged array with alternating signs starting positive
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
