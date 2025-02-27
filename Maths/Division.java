package maths;

import java.util.HashMap;

/**
 * Find division of two numbers. If the decimal part repeats, then put the repeating part in brackets ().
 *
 * https://leetcode.com/problems/fraction-to-recurring-decimal/
 */
public class Division {
    public static void main(String[] args) {
        System.out.println(new Division().fractionToDecimal(2, 1)); // Expected output: "2"
        System.out.println(new Division().fractionToDecimal(2, 3)); // Expected output: "0.(6)"
        System.out.println(new Division().fractionToDecimal(4, 333)); // Expected output: "0.(012)"
        System.out.println(new Division().fractionToDecimal(1, 5)); // Expected output: "0.2"
        System.out.println(new Division().fractionToDecimal(-1, -2147483648)); // Expected output: "0.0000000004656612873077392578125"
    }

    /**
     * Converts a fraction to a decimal representation.
     * If the decimal part is recurring, it encloses the repeating sequence in parentheses.
     *
     * @param numerator the numerator of the fraction
     * @param denominator the denominator of the fraction
     * @return a string representing the decimal format of the fraction
     */
    public String fractionToDecimal(int numerator, int denominator) {
        if (numerator == 0) return "0";
        StringBuilder result = new StringBuilder();
        result.append(((numerator > 0) ^ (denominator > 0)) ? "-" : "");

        long num = Math.abs((long) numerator);
        long den = Math.abs((long) denominator);

        // Integral part
        result.append(num / den);
        num %= den;
        if (num == 0) {
            return result.toString();
        }

        // Fractional part
        result.append(".");
        HashMap<Long, Integer> remainderMap = new HashMap<>(); // Stores remainder positions
        remainderMap.put(num, result.length());
        
        while (num != 0) {
            num *= 10;
            result.append(num / den);
            num %= den;
            
            if (remainderMap.containsKey(num)) {
                int index = remainderMap.get(num);
                result.insert(index, "(");
                result.append(")");
                break;
            } else {
                remainderMap.put(num, result.length());
            }
        }
        return result.toString();
    }
}
