package arrays;

import java.util.*;

/**
 * Problem: Count Elements With Strictly Smaller and Greater Elements
 *
 * Count how many numbers in the array have at least one strictly smaller number
 * and at least one strictly greater number also present in the array. Duplicates
 * of the minimum or maximum do not count because one side is missing for them.
 *
 * Leetcode: https://leetcode.com/problems/count-elements-with-strictly-smaller-and-greater-elements/ (Easy)
 * Rating:   1202 (zerotrac Elo)
 * Pattern:  Array | Min/max scan | Boundary exclusion
 *
 * Example:
 *   Input:  nums = [11,7,2,15]
 *   Output: 2
 *   Why:    7 and 11 sit strictly between the minimum 2 and maximum 15, while the
 *           endpoints themselves are missing one required side.
 *
 * Follow-ups:
 *   1. Return the actual elements instead of the count?
 *      Add qualifying values to a list during the final scan.
 *   2. Require at least k smaller and k greater elements?
 *      Sort the array or use order statistics so duplicates can be counted correctly.
 *   3. Process a stream where the full array is not stored?
 *      One pass can find min/max, but exact qualifying output needs a second pass or buffering.
 *
 * Related: Count Elements (1426), Find Pivot Index (724).
 */
public class CountElementsWithStrictlySmallerAndGreaterElements {

    public static void main(String[] args) {
        CountElementsWithStrictlySmallerAndGreaterElements solver =
            new CountElementsWithStrictlySmallerAndGreaterElements();

        int[][] inputs = { {11, 7, 2, 15}, {-3, 3, 3, 90}, {5, 5, 5} };
        int[] expected = { 2, 2, 0 };

        for (int i = 0; i < inputs.length; i++) {
            int got = solver.countElements(inputs[i]);
            System.out.printf("nums=%s  ->  %d  expected=%d%n",
                Arrays.toString(inputs[i]), got, expected[i]);
        }
    }

    /**
     * Intuition: a number has something strictly smaller and something strictly larger
     * if and only if it is neither equal to the global minimum nor equal to the global
     * maximum. Once those two boundaries are known, every interior value qualifies and
     * all boundary duplicates do not.
     *
     * Algorithm:
     *   1. Return 0 immediately when fewer than three values are present.
     *   2. Scan for the global minimum and maximum.
     *   3. If they are equal, all values are the same and none qualify.
     *   4. Count values strictly between the minimum and maximum.
     *
     * Time:  O(n) - the stream scans and final count are linear.
     * Space: O(1) - only min, max, and count are stored.
     *
     * @param nums input values
     * @return count of elements with both a strictly smaller and strictly greater value present
     */
    public int countElements(int[] nums) {
        if (nums.length <= 2) return 0;

        int minValue = Arrays.stream(nums).min().orElse(Integer.MAX_VALUE);
        int maxValue = Arrays.stream(nums).max().orElse(Integer.MIN_VALUE);

        // If all elements are same, no element satisfies condition
        if (minValue == maxValue) return 0;

        int count = 0;
        for (int num : nums) {
            if (num > minValue && num < maxValue) {
                count++;
            }
        }

        return count;
    }
}
