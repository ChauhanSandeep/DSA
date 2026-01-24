package arrays;

/**
 * 66. Plus One
 *
 * Problem Statement:
 * You are given a large integer represented as an integer array digits, where each
 * digits[i] is the ith digit of the integer. The digits are ordered from most
 * significant to least significant in left-to-right order. The large integer does
 * not contain any leading zeros. Increment the large integer by one and return
 * the resulting array of digits.
 *
 * Example:
 * Input: digits = [1,2,3]
 * Output: [1,2,4]
 * Explanation: The array represents the integer 123. Incrementing by one gives
 * 123 + 1 = 124. Thus, the result should be [1,2,4].
 *
 * Input: digits = [9,9]
 * Output: [1,0,0]
 * Explanation: 99 + 1 = 100, so we need to add a new leading digit.
 *
 * LeetCode Link: https://leetcode.com/problems/plus-one/
 *
 * Follow-up Questions:
 * 1. What if we need to add a number other than 1?
 *    Answer: Extend the algorithm to handle carry propagation for any addend, not just 1.
 * 2. How would you handle negative numbers represented as arrays?
 *    Answer: Add sign bit handling and implement subtraction logic for negative results.
 * 3. What if we need to support floating point numbers?
 *    Answer: Split into integer and fractional parts, handle decimal point position.
 * 4. How to optimize for very large arrays with sparse operations?
 *    Answer: Use compressed representations or segment-based processing for efficiency.
 *
 * Related Problems:
 * - 67. Add Binary: https://leetcode.com/problems/add-binary/
 * - 415. Add Strings: https://leetcode.com/problems/add-strings/
 * - 989. Add to Array-Form of Integer: https://leetcode.com/problems/add-to-array-form-of-integer/
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class PlusOne {

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
     * Optimized for common case of no overflow.
     * What we need to understand is that the only time we need to
     * create a new array is when all the digits are 9.
     * In all other cases, we can simply increment the last digit
     * that is not 9 and set all following digits to 0. This appraoch uses this insight
     * to optimize for the common case where no carry is needed.
     *
     * Algorithm:
     * 1. Check if the last digit is less than 9, if so increment
     *   and return.
     * 2. If last digit is 9, iterate backwards to find first non-9 digit,
     *   increment it, and set all following digits to 0.
     * 3. If all digits are 9, create new array with leading 1  and rest 0s.
     *
     * Time Complexity: O(n), Space Complexity: O(1) average case
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
