package array;

/**
 * Plus One
 *
 * Problem: Given array representing a large integer, increment it by one and return result.
 * Each element represents a single digit in left-to-right order.
 *
 * Example: digits = [1,2,3] -> Output: [1,2,4]
 * The number 123 + 1 = 124, so return [1,2,4].
 *
 * LeetCode: https://leetcode.com/problems/plus-one
 *
 * Follow-up Questions:
 * - How to add any number k instead of just 1? (Similar carry logic with k)
 * - What if digits can be negative? (Handle sign separately)
 * - Can we solve without creating new array? (Only when no overflow, otherwise need extra space)
 */
public class PlusOne {

    /**
     * Increments array-represented number by one.
     *
     * Algorithm:
     * 1. Start from rightmost digit (least significant)
     * 2. Add 1 to current digit
     * 3. If result < 10, we're done
     * 4. If result = 10, set current digit to 0 and carry 1 to next position
     * 5. If we reach leftmost digit with carry, prepend 1 to result
     *
     * Time Complexity: O(n) where n is number of digits
     * Space Complexity: O(1) if no overflow, O(n+1) if overflow occurs
     *
     * @param digits array representing digits of a number
     * @return array representing digits of number + 1
     */
    public int[] plusOne(int[] digits) {
        int n = digits.length;

        // Process digits from right to left
        for (int i = n - 1; i >= 0; i--) {
            // Add 1 to current digit
            digits[i]++;

            // If no carry needed, we're done
            if (digits[i] < 10) {
                return digits;
            }

            // Set current digit to 0 (carry will be handled in next iteration)
            digits[i] = 0;
        }

        // If we reach here, we had carry from most significant digit
        // Need to create new array with additional digit
        int[] result = new int[n + 1];
        result[0] = 1; // All other positions are 0 by default

        return result;
    }

    /**
     * Alternative implementation with explicit carry tracking
     * Time Complexity: O(n), Space Complexity: O(1) or O(n+1)
     */
    public int[] plusOneWithCarry(int[] digits) {
        int carry = 1; // We're adding 1
        int n = digits.length;

        for (int i = n - 1; i >= 0 && carry > 0; i--) {
            int sum = digits[i] + carry;
            digits[i] = sum % 10;
            carry = sum / 10;
        }

        // If still have carry, need new array
        if (carry > 0) {
            int[] result = new int[n + 1];
            result[0] = carry;
            System.arraycopy(digits, 0, result, 1, n);
            return result;
        }

        return digits;
    }

    /**
     * Recursive approach for educational purposes
     * Time Complexity: O(n), Space Complexity: O(n) due to recursion stack
     */
    public int[] plusOneRecursive(int[] digits) {
        return plusOneHelper(digits, digits.length - 1);
    }

    private int[] plusOneHelper(int[] digits, int index) {
        // Base case: adding to position before first digit
        if (index < 0) {
            int[] result = new int[digits.length + 1];
            result[0] = 1;
            System.arraycopy(digits, 0, result, 1, digits.length);
            return result;
        }

        digits[index]++;

        if (digits[index] < 10) {
            return digits; // No carry needed
        }

        digits[index] = 0;
        return plusOneHelper(digits, index - 1);
    }

    /**
     * Optimized for common case of no overflow
     * Time Complexity: O(n), Space Complexity: O(1) average case
     */
    public int[] plusOneOptimized(int[] digits) {
        // Handle most common case: no carry needed
        int lastIndex = digits.length - 1;
        if (digits[lastIndex] < 9) {
            digits[lastIndex]++;
            return digits;
        }

        // Handle carry case
        for (int i = lastIndex; i >= 0; i--) {
            if (digits[i] < 9) {
                digits[i]++;
                return digits;
            }
            digits[i] = 0;
        }

        // All digits were 9, need overflow handling
        int[] result = new int[digits.length + 1];
        result[0] = 1;
        return result;
    }

    /**
     * Generalized version that can add any single digit
     * Time Complexity: O(n), Space Complexity: O(1) or O(n+1)
     */
    public int[] addDigit(int[] digits, int digitToAdd) {
        if (digitToAdd < 0 || digitToAdd > 9) {
            throw new IllegalArgumentException("Digit must be between 0 and 9");
        }

        int carry = digitToAdd;

        for (int i = digits.length - 1; i >= 0 && carry > 0; i--) {
            int sum = digits[i] + carry;
            digits[i] = sum % 10;
            carry = sum / 10;
        }

        if (carry > 0) {
            int[] result = new int[digits.length + 1];
            result[0] = carry;
            System.arraycopy(digits, 0, result, 1, digits.length);
            return result;
        }

        return digits;
    }
}
