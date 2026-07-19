package dailybytes.string;

import java.util.Arrays;

/**
 * Problem: Add Binary
 *
 * Given two binary strings, return their sum as another binary string. The code
 * adds from right to left just like grade-school addition, carrying whenever a
 * column reaches two.
 *
 * Leetcode: https://leetcode.com/problems/add-binary/ (Easy)
 * Rating:   acceptance 58.3% (Easy) - no contest Elo (pre-contest problem)
 * Pattern:  String | Two pointers | Carry simulation
 *
 * Example:
 *   Input:  a = "11", b = "1"
 *   Output: "100"
 *   Why:    3 plus 1 equals 4, whose binary representation is 100.
 *
 * Follow-ups:
 *   1. Add numbers with millions of bits?
 *      Stream from the end in chunks and write output in reverse blocks.
 *   2. Generalize to base k strings?
 *      Replace division and modulo by 2 with division and modulo by k.
 *   3. Add many binary strings at once?
 *      Accumulate each column's total and carry across all strings.
 *   4. Return the result in-place in a mutable buffer?
 *      Fill from the end if capacity is available, otherwise append and reverse.
 *
 * Related: Plus One (66), Add Strings (415), Add Two Numbers (2).
 */
public class AddBinary {

    public static void main(String[] args) {
        String[][] inputs = { { "100", "1" }, { "11", "1" }, { "0", "0" }, { "11", "11" } };
        String[] expected = { "101", "100", "0", "110" };

        for (int i = 0; i < inputs.length; i++) {
            String output = addBinary(inputs[i][0], inputs[i][1]);
            System.out.printf("binary=%s -> %s  expected=%s%n",
                Arrays.toString(inputs[i]), output, expected[i]);
        }
    }

    /**
     * Intuition: binary addition is decimal column addition with a smaller base.
     * Start at the least significant characters of a and b, add the two bits plus
     * carry, append the output bit, and carry the overflow into the next column.
     * The builder is reversed at the end because bits are produced from right to
     * left.
     *
     * Algorithm:
     *   1. Handle null and empty input shortcuts.
     *   2. Walk indexA and indexB from the end while either string or carry remains.
     *   3. Add bitA, bitB, and carry; append sum % 2 and update carry = sum / 2.
     *   4. Reverse result to return the most significant bit first.
     *
     * Time:  O(max(n, m)) - one pass over the longer string.
     * Space: O(max(n, m)) - the output builder stores the resulting bits.
     *
     * @param a first binary string
     * @param b second binary string
     * @return binary string representing a + b
     */
    public static String addBinary(String a, String b) {
        if (a == null || b == null) return "0";
        if (a.isEmpty()) return b;
        if (b.isEmpty()) return a;

        StringBuilder result = new StringBuilder();
        int indexA = a.length() - 1;
        int indexB = b.length() - 1;
        int carry = 0;

        while (indexA >= 0 || indexB >= 0 || carry != 0) {
            int bitA = (indexA >= 0) ? a.charAt(indexA--) - '0' : 0;
            int bitB = (indexB >= 0) ? b.charAt(indexB--) - '0' : 0;

            int sum = bitA + bitB + carry;
            carry = sum / 2;
            result.append(sum % 2); // Append 0 or 1
        }

        return result.reverse().toString(); // Reverse for correct order
    }
}