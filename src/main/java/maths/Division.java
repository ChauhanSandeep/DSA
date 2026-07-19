package maths;

import java.util.HashMap;

/**
 * Problem: Fraction to Recurring Decimal
 *
 * Given a numerator and denominator, return the decimal representation of the
 * fraction as a string. If the fractional part repeats, wrap the repeating
 * cycle in parentheses.
 *
 * Leetcode: https://leetcode.com/problems/fraction-to-recurring-decimal/ (Medium)
 * Rating:   acceptance 31.1% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Math | Long division | Remainder cycle detection
 *
 * Example:
 *   Input:  numerator = 4, denominator = 333
 *   Output: "0.(012)"
 *   Why:    long division repeats the remainder sequence that emits 012 forever.
 *
 * Follow-ups:
 *   1. How would you cap the output to k decimal places?
 *      Run the same long division for at most k emitted fractional digits.
 *   2. How would you support arbitrary precision integer inputs?
 *      Replace long arithmetic with BigInteger-style string arithmetic.
 *   3. How would you return the repeating part separately?
 *      Keep the cycle start index and split the final string around it.
 *
 * Related: Divide Two Integers (29), Multiply Strings (43).
 */

public class Division {
    public static void main(String[] args) {
        Division solver = new Division();
        int[][] inputs = { {2, 1}, {2, 3}, {4, 333}, {-50, 8} };
        String[] expected = { "2", "0.(6)", "0.(012)", "-6.25" };

        for (int i = 0; i < inputs.length; i++) {
            String got = solver.fractionToDecimal(inputs[i][0], inputs[i][1]);
            System.out.printf("numerator=%d denominator=%d -> %s  expected=%s%n",
                inputs[i][0], inputs[i][1], got, expected[i]);
        }
    }

        /**
     * Intuition: decimal expansion is just long division. The integer part is
     * numerator / denominator, and every fractional digit is determined by the
     * current remainder multiplied by 10. If the same remainder appears again,
     * the exact same future digits will repeat from that earlier position.
     *
     * Algorithm:
     *   1. Handle zero numerator, append the sign, and convert operands to long.
     *   2. Append the integral part and stop if the remainder is zero.
     *   3. Store each remainder with the output index where its digit cycle starts.
     *   4. Repeatedly multiply the remainder by 10, append the digit, and update it.
     *   5. When a remainder repeats, insert parentheses around the repeating suffix.
     *
     * Time:  O(k) - k fractional digits are emitted before termination or repeat.
     * Space: O(k) - each distinct fractional remainder is stored once.
     *
     * @param numerator numerator of the fraction
     * @param denominator denominator of the fraction
     * @return decimal string, with a repeating cycle in parentheses when needed
     */

    public String fractionToDecimal(int numerator, int denominator) {
        if (numerator == 0) return "0"; // If numerator is zero, the result is always "0"

        StringBuilder result = new StringBuilder();

        // Handle negative sign
        if ((numerator < 0) ^ (denominator < 0)) {
            result.append("-");
        }

        // Convert to long to prevent integer overflow
        long num = Math.abs((long) numerator);
        long den = Math.abs((long) denominator);

        // Compute integral part
        result.append(num / den);
        long remainder = num % den;

        // If there's no fractional part, return the result
        if (remainder == 0) {
            return result.toString();
        }

        // Compute fractional part
        result.append(".");
        HashMap<Long, Integer> remainderIndexMap = new HashMap<>(); // Stores remainder positions
        remainderIndexMap.put(remainder, result.length());

        while (remainder != 0) {
            remainder *= 10;
            result.append(remainder / den);
            remainder %= den;

            // If the remainder is seen before, a repeating cycle exists
            if (remainderIndexMap.containsKey(remainder)) {
                int cycleStartIndex = remainderIndexMap.get(remainder);
                result.insert(cycleStartIndex, "(");
                result.append(")");
                break;
            } else {
                remainderIndexMap.put(remainder, result.length());
            }
        }

        return result.toString();
    }
}
