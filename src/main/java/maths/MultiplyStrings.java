package maths;

/**
 * Problem: Multiply Strings
 *
 * Given two non-negative integers represented as strings, return their product
 * as a string. The solution must not use BigInteger or directly convert the
 * whole input strings to built-in integer values.
 *
 * Leetcode: https://leetcode.com/problems/multiply-strings/ (Medium)
 * Rating:   acceptance 44.5% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Math | String arithmetic | Grade-school multiplication
 *
 * Example:
 *   Input:  num1 = "123", num2 = "456"
 *   Output: "56088"
 *   Why:    the digit products and carries match the standard multiplication by hand.
 *
 * Follow-ups:
 *   1. How would you support negative numbers?
 *      Track the sign separately, multiply absolute values, then prefix '-' if needed.
 *   2. How would you add numbers represented as strings?
 *      Process from right to left with one carry, similar to the final carry pass here.
 *   3. How would you multiply extremely long strings faster?
 *      Use Karatsuba or FFT-based multiplication instead of the O(m * n) baseline.
 *
 * Related: Add Strings (415), Add Binary (67), Divide Two Integers (29).
 */

public class MultiplyStrings {

    public static void main(String[] args) {
        MultiplyStrings solver = new MultiplyStrings();
        String[][] inputs = { {"2", "3"}, {"123", "456"}, {"0", "52"} };
        String[] expected = { "6", "56088", "0" };

        for (int i = 0; i < inputs.length; i++) {
            String got = solver.multiply(inputs[i][0], inputs[i][1]);
            System.out.printf("num1=%s num2=%s -> %s  expected=%s%n",
                inputs[i][0], inputs[i][1], got, expected[i]);
        }
    }


        /**
     * Intuition: each digit pair contributes to fixed positions in the final
     * product, just like multiplication on paper. The result array stores those
     * positional sums, and the original code pushes carry values leftward as it
     * processes each pair.
     *
     * Algorithm:
     *   1. Return "0" if either input is zero.
     *   2. Allocate result with length len1 + len2, the maximum possible digits.
     *   3. Multiply digits from right to left and add into currentDigitIndex.
     *   4. Store sum % 10 at currentDigitIndex and add sum / 10 to carryIndex.
     *   5. Build the answer string while skipping leading zeros.
     *
     * Time:  O(m * n) - every digit in num1 is multiplied by every digit in num2.
     * Space: O(m + n) - the result array stores at most m + n digits.
     *
     * @param num1 first non-negative integer string
     * @param num2 second non-negative integer string
     * @return product represented as a string
     */

    public String multiply(String num1, String num2) {
        // Edge case: if either is zero
        if (num1.equals("0") || num2.equals("0")) {
            return "0";
        }

        int len1 = num1.length();
        int len2 = num2.length();

        // Result can have at most m + n digits
        int[] result = new int[len1 + len2];

        // Multiply each digit of num1 with each digit of num2
        // Process from right to left (least significant to most significant)
        for (int i = len1 - 1; i >= 0; i--) {
            for (int j = len2 - 1; j >= 0; j--) {
                // Get digit values
                int digit1 = num1.charAt(i) - '0';
                int digit2 = num2.charAt(j) - '0';

                // Multiply digits
                int product = digit1 * digit2;

                int currentDigitIndex = i + j + 1;
                int carryIndex = i + j;

                // Add to existing value at current digit index
                int sum = product + result[currentDigitIndex];

                // Store the ones place at current digit index
                result[currentDigitIndex] = sum % 10;

                // Add carry to the next higher position carry index
                result[carryIndex] += sum / 10;
            }
        }

        // Convert result array to string, skip leading zeros
        StringBuilder sb = new StringBuilder();
        for (int digit : result) {
            // Skip leading zeros
            if (!(sb.length() == 0 && digit == 0)) {
                sb.append(digit);
            }
        }

        return sb.toString();
    }
}
