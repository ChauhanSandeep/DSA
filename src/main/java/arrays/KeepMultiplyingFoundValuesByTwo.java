package arrays;

import java.util.*;

/**
 * Keep Multiplying Found Values By Two
 *
 * Problem: Start with initial value, search in array, multiply by 2 if found, repeat.
 * Return final value after no more matching elements found.
 *
 * Example: nums = [5,3,6,1,12], start = 3 -> Output: 24
 * 3 found -> 6, 6 found -> 12, 12 found -> 24, 24 not found -> return 24
 *
 * LeetCode: https://leetcode.com/problems/keep-multiplying-found-values-by-two
 *
 * Follow-up Questions:
 * - What if we multiply by a different factor? (Change multiplication factor)
 * - How to track the sequence of multiplications? (Store intermediate values)
 * - What if array contains duplicates of target? (Current solution handles correctly)
 */
public class KeepMultiplyingFoundValuesByTwo {

    /**
     * Keeps multiplying value by 2 while it exists in array.
     *
     * Algorithm:
     * 1. Create set for O(1) lookup of array elements
     * 2. Start with initial value
     * 3. While current value exists in set:
     *    - Multiply by 2
     *    - Continue search
     * 4. Return final value when not found
     *
     * Time Complexity: O(n + k) where n is array length, k is number of multiplications
     * Space Complexity: O(n) for the set
     *
     * @param nums array of integers to search in
     * @param start initial value to multiply
     * @return final value after all possible multiplications
     */
    public int findFinalValue(int[] nums, int start) {
        Set<Integer> numSet = new HashSet<>();
        for (int num : nums) {
            numSet.add(num);
        }

        int current = start;
        while (numSet.contains(current)) {
            current *= 2;
        }

        return current;
    }

    /**
     * Array-based linear search approach (less efficient but simpler)
     * Time Complexity: O(n * k), Space Complexity: O(1)
     */
    public int findFinalValueLinearSearch(int[] nums, int start) {
        int current = start;

        while (true) {
            boolean found = false;

            // Linear search for current value
            for (int num : nums) {
                if (num == current) {
                    found = true;
                    break;
                }
            }

            if (found) {
                current *= 2;
            } else {
                break;
            }
        }

        return current;
    }

    /**
     * Sorting-based approach with binary search
     * Time Complexity: O(n log n + k log n), Space Complexity: O(1)
     */
    public int findFinalValueBinarySearch(int[] nums, int start) {
        Arrays.sort(nums);

        int current = start;
        while (Arrays.binarySearch(nums, current) >= 0) {
            current *= 2;
        }

        return current;
    }

    /**
     * Optimized approach tracking multiplication path
     * Returns both final value and multiplication count
     */
    public int[] findFinalValueWithSteps(int[] nums, int start) {
        Set<Integer> numSet = new HashSet<>();
        for (int num : nums) {
            numSet.add(num);
        }

        int current = start;
        int steps = 0;

        while (numSet.contains(current)) {
            current *= 2;
            steps++;
        }

        return new int[]{current, steps};
    }
}
