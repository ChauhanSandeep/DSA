package Backtracking;

import java.util.ArrayList;
import java.util.List;

/**
 * LeetCode: https://leetcode.com/problems/gray-code/
 *
 * Problem: The Gray Code is a binary numeral system in which two successive values differ in only one bit.
 * Given a non-negative integer n representing the total number of bits in the code,
 * return the sequence of Gray Code, starting from 0.
 *
 * Approach:
 * - The recursive approach generates the (n-1)-bit sequence and then reflects it.
 * - For each reflected sequence, prepend '0' to the first half and '1' to the second half.
 * - Convert the binary strings to integers before returning the result.
 *
 * Time Complexity:  O(2^n) → Each level of recursion generates 2^n elements.
 * Space Complexity: O(2^n) → Result list stores all 2^n sequences.
 */

public class GrayCode {
    public static void main(String[] args) {
        List<Integer> grayCodeSequence = generateGrayCode(4);
        for (Integer num : grayCodeSequence) {
            System.out.println(Integer.toBinaryString(num));  // Print as binary
        }
    }

    /**
     * Generates the Gray Code sequence for a given number of bits.
     * @param n The number of bits in the Gray Code sequence.
     * @return A list of integers representing the Gray Code sequence.
     */
    public static List<Integer> generateGrayCode(int n) {
        List<String> binaryGrayCode = generateGrayCodeRecursive(n);
        List<Integer> grayCodeNumbers = new ArrayList<>();

        // Convert binary Gray codes to integers
        for (String binaryCode : binaryGrayCode) {
            grayCodeNumbers.add(Integer.parseInt(binaryCode, 2));
        }
        return grayCodeNumbers;
    }

    /**
     * Recursively generates the Gray Code sequence in binary string format.
     * @param n The number of bits.
     * @return A list of binary strings representing the Gray Code sequence.
     */
    private static List<String> generateGrayCodeRecursive(int n) {
        if (n == 0) {
            List<String> baseCase = new ArrayList<>();
            baseCase.add("0");
            return baseCase;
        }
        if (n == 1) {
            List<String> baseCase = new ArrayList<>();
            baseCase.add("0");
            baseCase.add("1");
            return baseCase;
        }

        // Recursive call for (n-1) bits
        List<String> previousGrayCode = generateGrayCodeRecursive(n - 1);
        List<String> currentGrayCode = new ArrayList<>();

        // Add '0' prefix to the first half
        for (String code : previousGrayCode) {
            currentGrayCode.add("0" + code);
        }

        // Add '1' prefix to the reversed second half
        for (int i = previousGrayCode.size() - 1; i >= 0; i--) {
            currentGrayCode.add("1" + previousGrayCode.get(i));
        }

        return currentGrayCode;
    }
}
