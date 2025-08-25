package strings;

/**
 * LeetCode 415. Add Strings
 *
 * Given two non-negative integers num1 and num2 represented as strings, return the sum of num1 and num2 as a string.
 * You must solve the problem without using any built-in library for handling large integers (such as BigInteger).
 * You must also not convert the inputs to integers directly.
 *
 * Example 1:
 * Input: num1 = "11", num2 = "123"
 * Output: "134"
 * Explanation: 11 + 123 = 134
 *
 * LeetCode Link: https://leetcode.com/problems/add-strings/
 *
 * Follow-up Questions:
 * - How would you handle negative numbers? (Extend to support subtraction with sign handling)
 * - Can you optimize for very large strings? (Consider chunked processing for memory efficiency)
 * - How would you extend this to support decimal numbers? (Add decimal point handling and alignment)
 */
public class AddStrings {

    /**
     * Adds two non-negative integer strings using elementary addition algorithm.
     *
     * Algorithm:
     * 1. Start from the rightmost digits of both strings
     * 2. Add corresponding digits along with carry from previous position
     * 3. Calculate new digit (sum % 10) and new carry (sum / 10)
     * 4. Continue until both strings are processed and no carry remains
     *
     * Time Complexity: O(max(m, n)) where m, n are lengths of num1, num2
     * Space Complexity: O(max(m, n)) for the result string
     */
    public String addStrings(String num1, String num2) {
        StringBuilder result = new StringBuilder();
        int carry = 0;
        int i = num1.length() - 1;
        int j = num2.length() - 1;

        // Process both strings from right to left
        while (i >= 0 || j >= 0 || carry > 0) {
            int digit1 = i >= 0 ? num1.charAt(i) - '0' : 0;
            int digit2 = j >= 0 ? num2.charAt(j) - '0' : 0;

            int sum = digit1 + digit2 + carry;
            result.append(sum % 10);
            carry = sum / 10;

            i--;
            j--;
        }

        // Result was built backwards, so reverse it
        return result.reverse().toString();
    }
}
