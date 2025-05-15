package String;

import java.util.Arrays;

/**
 * LeetCode Problem: https://leetcode.com/problems/largest-number/
 *
 * Problem:
 * Given a list of non-negative integers, arrange them to form the largest possible number.
 * Return the number as a string.
 *
 * Approach:
 * - Convert all integers to strings.
 * - Define a custom comparator for sorting:
 *   - Compare `a + b` vs. `b + a` to determine order (concatenation logic).
 *   - Sort in **descending order** to maximize the number.
 * - Concatenate sorted numbers to form the result.
 * - Handle the **edge case** where all numbers are zero.
 *
 * Time Complexity: O(N log N) (due to sorting)
 * Space Complexity: O(N) (for storing string representations)
 */
public class LargestNumber {
    public static void main(String[] args) {
        int[] nums = {3, 30, 34, 5, 9};
        String result = new LargestNumber().largestNumber(nums);
        System.out.println("Largest number is: " + result);
    }

    public String largestNumber(int[] nums) {
        // Convert integers to strings
        String[] stringNumbers = Arrays.stream(nums)
                                       .mapToObj(String::valueOf)
                                       .toArray(String[]::new);

        // Custom sorting: Compare concatenated strings to determine order
        Arrays.sort(stringNumbers, (a, b) -> (b + a).compareTo(a + b));

        // If the largest number is "0", return "0" (to handle leading zero case)
        if (stringNumbers[0].equals("0")) {
            return "0";
        }

        // Build the final largest number string
        StringBuilder resultBuilder = new StringBuilder();
        for (String num : stringNumbers) {
            resultBuilder.append(num);
        }

        return resultBuilder.toString();
    }
}
