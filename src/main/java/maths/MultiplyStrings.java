package maths;

/**
 * MultiplyStrings.java (LeetCode 43)
 *
 * Problem Statement:
 * Given two non-negative integers num1 and num2 represented as strings,
 * return the product of num1 and num2, also represented as a string.
 * 
 * Note: You must not use any built-in BigInteger library or convert the
 * inputs to integer directly.
 *
 * Example 1:
 * Input: num1 = "2", num2 = "3"
 * Output: "6"
 *
 * Example 2:
 * Input: num1 = "123", num2 = "456"
 * Output: "56088"
 *
 * LeetCode link: https://leetcode.com/problems/multiply-strings/
 *
 * Follow-up Questions FAANG Interviews Might Ask:
 * - How would you handle negative numbers?
 * → Track sign separately, multiply absolute values, apply sign to result.
 * - Can you optimize for very large numbers?
 * → Use Karatsuba algorithm (O(n^1.58)) instead of grade-school (O(n²)).
 * - What if numbers have different bases (not base 10)?
 * → Generalize digit extraction/combination logic with modulo base.
 * - How would you implement division similarly?
 * → Long division with string manipulation, more complex.
 *
 * Relevant Follow-up Problems:
 * - LeetCode 2 (Add Two Numbers):
 * https://leetcode.com/problems/add-two-numbers/
 * - LeetCode 415 (Add Strings): https://leetcode.com/problems/add-strings/
 * - LeetCode 67 (Add Binary): https://leetcode.com/problems/add-binary/
 */
public class MultiplyStrings {

    /**
     * Main method: Grade-school multiplication simulation (Optimal for interviews).
     * Step-by-step:
     * 1. Edge case: if either number is "0", return "0"
     * 2. Create result array of size (m + n) - max possible length
     * 3. Multiply each digit of num1 with each digit of num2 (nested loops)
     * 4. For digits at positions i and j:
     * - Their product contributes to positions (i + j) and (i + j + 1)
     * - Add product to position (i + j + 1)
     * 5. Handle carries by processing result array right-to-left
     * 6. Convert array to string, skip leading zeros
     *
     * Key Insight:
     * When multiplying digit at index i (from right) with digit at index j,
     * the result affects positions i+j and i+j+1 in the final answer.
     * 
     * Example: 123 × 45
     * Position indexing: 1 2 3
     * × 4 5
     * -------
     * digit[2]=3 × digit[1]=5 = 15 → goes to position[2+1]=3 (ones) and [2+1+1]=4
     * (tens)
     *
     * Algorithm: Grade-school multiplication.
     * Time Complexity: O(m × n) where m, n are lengths of num1, num2.
     * Space Complexity: O(m + n) for result array.
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
