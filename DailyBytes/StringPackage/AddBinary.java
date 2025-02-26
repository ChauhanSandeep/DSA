package DailyBytes.StringPackage;

/**
 * This class contains a method to find the sum of two binary numbers.
 * 
 * Algorithm:
 * - Traverse both binary strings from right to left.
 * - Add corresponding bits along with carry.
 * - Handle remaining bits and carry.
 * - Time Complexity: O(max(n, m)) where n and m are the lengths of the binary strings.
 * - Space Complexity: O(max(n, m))
 * 
 * LeetCode Problem Link: https://leetcode.com/problems/add-binary/
 */
public class AddBinary {
    public static void main(String[] args) {
        System.out.println("The sum of binary is " + addBinary("100", "1"));
        System.out.println("The sum of binary is " + addBinary("11", "1"));
        System.out.println("The sum of binary is " + addBinary("1", "0"));
        System.out.println("The sum of binary is " + addBinary("11", "11"));
    }

    /**
     * Finds the sum of two binary numbers.
     * @param first The first binary number as a string.
     * @param second The second binary number as a string.
     * @return The sum of the two binary numbers as a string.
     */
    public static String addBinary(String first, String second) {
        int firstIndex = first.length() - 1;
        int secondIndex = second.length() - 1;
        int carry = 0;
        StringBuilder result = new StringBuilder();

        // Traverse both strings from right to left
        while (firstIndex >= 0 || secondIndex >= 0) {
            int firstBit = firstIndex >= 0 ? first.charAt(firstIndex) - '0' : 0;
            int secondBit = secondIndex >= 0 ? second.charAt(secondIndex) - '0' : 0;
            int sum = firstBit + secondBit + carry;
            result.insert(0, sum % 2);
            carry = sum / 2;
            firstIndex--;
            secondIndex--;
        }

        // Handle remaining carry
        if (carry != 0) {
            result.insert(0, carry);
        }

        return result.toString();
    }
}
