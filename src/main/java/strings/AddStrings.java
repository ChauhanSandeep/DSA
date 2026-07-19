package strings;

/**
 * Problem: Add Strings
 *
 * Add two non-negative integers represented as decimal strings and return their
 * sum as a string. The solution cannot use BigInteger or convert the full inputs
 * directly to numeric types.
 *
 * Leetcode: https://leetcode.com/problems/add-strings/ (Easy)
 * Rating:   acceptance 52.2% (Easy) - no contest Elo (pre-contest problem)
 * Pattern:  String | Elementary math | Carry from right to left
 *
 * Example:
 *   Input:  num1 = "456", num2 = "77"
 *   Output: "533"
 *   Why:    column addition produces 3, then 3 with carry, then 5.
 *
 * Follow-ups:
 *   1. Support negative numbers? Compare absolute values and choose add or subtract by sign.
 *   2. Support decimal strings? Align decimal points before doing the same carry pass.
 *   3. Add many strings? Accumulate columns from right to left or add pairwise.
 *
 * Related: Multiply Strings (43), Add Binary (67), Plus One (66).
 */
public class AddStrings {

    public static void main(String[] args) {
        AddStrings solver = new AddStrings();
        String[] first = {"11", "456", "0"};
        String[] second = {"123", "77", "0"};
        String[] expected = {"134", "533", "0"};
        for (int i = 0; i < first.length; i++) {
            String got = solver.addStrings(first[i], second[i]);
            System.out.printf("num1=%s num2=%s -> %s  expected=%s%n", first[i], second[i], got, expected[i]);
        }
    }


        /**
     * Intuition: this is paper addition from right to left. Keep a carry, append
     * each result digit as it is discovered, then reverse because the builder was
     * filled from least significant digit to most significant digit.
     *
     * Algorithm:
     *   1. Start i and j at the end of num1 and num2.
     *   2. Read missing digits as 0, add both digits and carry, and append sum % 10.
     *   3. Move both indices left and keep sum / 10 as the next carry.
     *   4. Reverse the builder and return it.
     *
     * Time:  O(max(m, n)) - one pass over the longer string.
     * Space: O(max(m, n)) - the returned sum stores the output digits.
     *
     * @param num1 first non-negative integer string
     * @param num2 second non-negative integer string
     * @return decimal string for num1 + num2
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
