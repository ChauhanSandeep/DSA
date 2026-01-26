package maths;

import java.util.HashMap;

/**
 * Problem: Convert a fraction to its decimal representation.
 * 
 * Given two integers (numerator and denominator), return their division result as a string.
 * If the fractional part is repeating, enclose the repeating sequence in parentheses.
 * 
 * Examples:
 *  - fractionToDecimal(2, 1)     → "2"
 *  - fractionToDecimal(2, 3)     → "0.(6)"
 *  - fractionToDecimal(4, 333)   → "0.(012)"
 *  - fractionToDecimal(1, 5)     → "0.2"
 *  - fractionToDecimal(-1, -2147483648) → "0.0000000004656612873077392578125"
 * 
 * Approach:
 *  - Handle sign and convert numbers to long to prevent overflow.
 *  - Compute the integral part using integer division.
 *  - If there’s a remainder, compute the fractional part using a HashMap to detect cycles.
 * 
 * Time Complexity: O(N) - N is the length of the repeating sequence (worst case).
 * Space Complexity: O(N) - To store remainders in a HashMap.
 * 
 * LeetCode Link: https://leetcode.com/problems/fraction-to-recurring-decimal/
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class Division {
    public static void main(String[] args) {
        Division division = new Division();
        System.out.println(division.fractionToDecimal(2, 1));      // "2"
        System.out.println(division.fractionToDecimal(2, 3));      // "0.(6)"
        System.out.println(division.fractionToDecimal(4, 333));    // "0.(012)"
        System.out.println(division.fractionToDecimal(1, 5));      // "0.2"
        System.out.println(division.fractionToDecimal(-1, -2147483648)); // "0.0000000004656612873077392578125"
    }

    /**
     * Converts a fraction to a decimal string representation.
     * If the decimal part is repeating, it encloses the repeating sequence in parentheses.
     *
     * @param numerator   the numerator of the fraction
     * @param denominator the denominator of the fraction
     * @return the decimal representation of the fraction as a string
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
