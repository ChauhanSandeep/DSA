package arrays;

import java.util.*;

/**
 * Problem: Add to Array-Form of Integer
 *
 * A non-negative integer is stored as an array of decimal digits, most significant
 * digit first. Add the integer k to it and return the sum in the same array-form.
 * The input number may be too large to fit in a primitive numeric type.
 *
 * Leetcode: https://leetcode.com/problems/add-to-array-form-of-integer/ (Easy)
 * Rating:   1235 (zerotrac Elo)
 * Pattern:  Array | Digit carry | Right-to-left addition
 *
 * Example:
 *   Input:  nums = [2,1,5], k = 806
 *   Output: [1,0,2,1]
 *   Why:    215 + 806 = 1021, so the returned digits must include the new
 *           leading 1 created by the final carry.
 *
 * Follow-ups:
 *   1. Add two array-form integers instead of one array and one int?
 *      Walk both arrays from right to left and carry exactly like elementary addition.
 *   2. Support negative k?
 *      Add a sign comparison first, then use borrow-based subtraction when needed.
 *   3. Stream digits from disk instead of storing the whole number?
 *      Process from the least significant end if possible, or use a stack/chunked buffer.
 *
 * Related: Plus One (66), Add Binary (67), Add Strings (415).
 */
public class AddToArrayFormOfInteger {

    public static void main(String[] args) {
        AddToArrayFormOfInteger solver = new AddToArrayFormOfInteger();

        int[][] inputs = { {1, 2, 0, 0}, {2, 1, 5}, {9, 9, 9} };
        int[] additions = { 34, 806, 1 };
        String[] expected = { "[1, 2, 3, 4]", "[1, 0, 2, 1]", "[1, 0, 0, 0]" };

        for (int i = 0; i < inputs.length; i++) {
            List<Integer> got = solver.addToArrayForm(inputs[i], additions[i]);
            System.out.printf("nums=%s k=%d  ->  %s  expected=%s%n",
                java.util.Arrays.toString(inputs[i]), additions[i], got, expected[i]);
        }
    }

    /**
     * Intuition: this is the same addition taught on paper, except one addend is
     * stored as digits and the other is k. Work from the least significant side,
     * combine the current array digit, the current decimal digit of k, and carry,
     * then reverse the collected digits because they were produced from right to left.
     *
     * Algorithm:
     *   1. Start at the last digit of nums with carry set to 0.
     *   2. While nums, k, or carry still has digits, add the current pieces.
     *   3. Append sum % 10, update carry to sum / 10, and move left.
     *   4. Reverse the built list so the most significant digit comes first.
     *
     * Time:  O(max(n, log k)) - one loop consumes one digit from nums and k each time.
     * Space: O(max(n, log k)) - the returned list stores the sum digits.
     *
     * @param nums decimal digits of a non-negative integer
     * @param k non-negative value to add
     * @return array-form digits of nums + k
     */
    public List<Integer> addToArrayForm(int[] nums, int k) {
        List<Integer> result = new ArrayList<>();
        int carry = 0;
        int index = nums.length - 1;

        // Process until all digits are handled and no carry remains
        while (index >= 0 || k > 0 || carry > 0) {
            // Get current digit from nums array (0 if out of bounds)
            int currentDigit = index >= 0 ? nums[index] : 0;

            // Get last digit of k
            int kDigit = k%10;
            k = k/10;

            // Add current digit + last digit of k + carry
            int sum = currentDigit + kDigit + carry;

            // Add the unit digit to result
            result.add(sum % 10);

            // Update carry and move to next digit of k
            carry = sum / 10;
            index--;
        }

        // Reverse since we built the result backwards
        Collections.reverse(result);
        return result;
    }

    /**
     * Alternative approach using string conversion (less efficient but more intuitive)
     * This approach may lead to overflow for very large numbers and would not be allowed in an interview setting.
     *
     * Steps:
     * 1. Convert array to number, add k
     * 2. Convert result back to array form
     *
     * Time Complexity: O(N + log K),
     * Space Complexity: O(N + log K)
     */
    public List<Integer> addToArrayFormAlternative(int[] nums, int k) {
        // Convert array to number, add k, then convert back
        // Note: This approach has overflow risks for large numbers
        long number = 0;
        for (int digit : nums) {
            number = number * 10 + digit;
        }

        number += k;

        // Convert result back to list
        List<Integer> result = new ArrayList<>();
        if (number == 0) {
            result.add(0);
            return result;
        }

        while (number > 0) {
            result.add(0, (int)(number % 10));
            number /= 10;
        }

        return result;
    }
}
