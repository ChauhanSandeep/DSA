package dailybytes.string;

/**
 * ✅ Problem: Add Binary
 *
 * Given two binary strings, return their sum (also a binary string).
 * The input strings are guaranteed to contain only '0' or '1'.
 *
 * 🔗 Leetcode: https://leetcode.com/problems/add-binary/
 *
 * 🧠 Example:
 * Input: a = "11", b = "1"
 * Output: "100"
 *
 * 🔍 Follow-up:
 * 1. What if inputs are very large (millions of bits)? ➤ Use streaming or chunking
 * 2. What if inputs are mutable char arrays? ➤ Do in-place with reverse traversal
 * 3. What if base is not binary but k-ary (base-k)? ➤ Generalize logic with modular arithmetic
 */
public class AddBinary {

    public static void main(String[] args) {
        System.out.println("The sum of binary is " + addBinary("100", "1"));     // Expected: 101
        System.out.println("The sum of binary is " + addBinary("11", "1"));      // Expected: 100
        System.out.println("The sum of binary is " + addBinary("1", "0"));       // Expected: 1
        System.out.println("The sum of binary is " + addBinary("11", "11"));     // Expected: 110
        System.out.println("The sum of binary is " + addBinary("", ""));         // Expected: 0
    }

    /**
     * ✅ Adds two binary strings and returns their binary sum.
     *
     * 🔧 Approach:
     * - Traverse both strings from end to start
     * - Add digit-wise along with carry
     * - Use StringBuilder (reversed) for efficient append
     *
     * Time Complexity: O(max(n, m))
     * Space Complexity: O(max(n, m)) for result string
     *
     * @param a Binary string a
     * @param b Binary string b
     * @return Binary string representing the sum of a and b
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