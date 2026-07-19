package arrays;

import java.util.Arrays;
/**
 * Problem: Plus One
 *
 * A large non-negative integer is stored as decimal digits from most significant
 * to least significant. Add one to that integer and return the resulting digits.
 * The input has no leading zeros except for the number zero itself.
 *
 * Leetcode: https://leetcode.com/problems/plus-one/ (Easy)
 * Rating:   acceptance 50.3% (Easy) - no contest Elo (pre-contest problem)
 * Pattern:  Array | Digit carry | Right-to-left scan
 *
 * Example:
 *   Input:  digits = [9,9]
 *   Output: [1,0,0]
 *   Why:    99 + 1 creates a carry past the most significant digit, so a new
 *           leading 1 is needed.
 *
 * Follow-ups:
 *   1. Add an arbitrary non-negative integer instead of one?
 *      Use the same carry loop as Add to Array-Form of Integer.
 *   2. Add two digit arrays?
 *      Walk both arrays from right to left and build a result with carry.
 *   3. Support a linked-list digit representation?
 *      Reverse the list first, or use recursion/stack to process from the tail.
 *
 * Related: Add Binary (67), Add Strings (415), Add to Array-Form of Integer (989).
 */
public class PlusOne {

    public static void main(String[] args) {
        PlusOne solver = new PlusOne();

        int[][] inputs = { {1, 2, 3}, {9, 9}, {0} };
        String[] expected = { "[1, 2, 4]", "[1, 0, 0]", "[1]" };

        for (int i = 0; i < inputs.length; i++) {
            int[] got = solver.plusOneOptimized(inputs[i].clone());
            System.out.printf("digits=%s  ->  %s  expected=%s%n",
                Arrays.toString(inputs[i]), Arrays.toString(got), expected[i]);
        }
    }

    /**
     * Implementation with explicit carry tracking
     * Algorithm:
     * 1. Start from the last digit and add 1 (initial carry)
     * 2. Update the digit and calculate new carry
     * 3. If carry remains after processing all digits, create new array with leading 1
     *
     * Time Complexity: O(n),
     * Space Complexity: O(1) or O(n+1)
     */
    public int[] plusOneWithCarry(int[] digits) {
        int carry = 1; // We're adding 1
        int size = digits.length;

        for (int i = size - 1; i >= 0 && carry > 0; i--) {
            int sum = digits[i] + carry;
            digits[i] = sum % 10;
            carry = sum / 10;
        }

        // If still have carry after iterating complete array. We need new array to handle overflow
        if (carry > 0) {
            int[] result = new int[size + 1];
            result[0] = carry;
            System.arraycopy(digits, 0, result, 1, size);
            return result;
        }

        return digits;
    }

    /**
     * Intuition: most additions finish at the last digit. If the last digit is below 9,
     * increment it and return immediately. Otherwise every trailing 9 becomes 0 until a
     * non-9 digit absorbs the carry; if no such digit exists, all digits were 9 and a
     * new leading 1 is needed.
     *
     * Algorithm:
     *   1. If the last digit is below 9, increment it and return digits.
     *   2. Walk backward, turning 9s into 0s until a non-9 digit appears.
     *   3. Increment the first non-9 digit and return.
     *   4. If every digit was 9, return a new array with leading 1.
     *
     * Time:  O(n) - the all-9s case touches every digit once.
     * Space: O(1) extra - a new n + 1 array is allocated only on overflow.
     *
     * @param digits decimal digits of a non-negative integer
     * @return digits representing the input integer plus one
     */
    public int[] plusOneOptimized(int[] digits) {
        // Handle most common case: no carry needed
        int lastIndex = digits.length - 1;
        if (digits[lastIndex] < 9) {
            digits[lastIndex]++;
            return digits;
        }

        // Handle carry case. Iterate backwards and set 9s to 0. Once we find a non-9, increment it and return.
        for (int i = lastIndex; i >= 0; i--) {
            if (digits[i] < 9) {
                digits[i]++;
                return digits;
            }
            digits[i] = 0;
        }

        // If we are at this point means that all digits were 9, need overflow handling
        int[] result = new int[digits.length + 1];
        result[0] = 1;
        return result;
    }
}
