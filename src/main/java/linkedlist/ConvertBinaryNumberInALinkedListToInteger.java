package linkedlist;

import java.util.Arrays;

/**
 * Problem: Convert Binary Number in a Linked List to Integer
 *
 * A linked list stores one binary digit per node from most significant to least
 * significant. Convert that bit sequence into its decimal integer value without
 * changing the list.
 *
 * Leetcode: https://leetcode.com/problems/convert-binary-number-in-a-linked-list-to-integer/ (Easy)
 * Rating:   1151
 * Pattern:  Linked list | Binary accumulation | Horner's rule
 *
 * Example:
 *   Input:  head = [1,0,1]
 *   Output: 5
 *   Why:    binary 101 equals decimal 5.
 *
 * Follow-ups:
 *   1. The list exceeds int range?
 *      Return a decimal string or BigInteger representation.
 *   2. Node values may not be binary?
 *      Validate each value before adding it to the result.
 *   3. Can this be recursive?
 *      Carry the accumulated result through recursive calls.
 *
 * Related: Add Binary (67), Binary Number with Alternating Bits (693).
 */
public class ConvertBinaryNumberInALinkedListToInteger {

    public static void main(String[] args) {
        ConvertBinaryNumberInALinkedListToInteger solver = new ConvertBinaryNumberInALinkedListToInteger();
        int[][] inputs = { {1, 0, 1}, {0}, {1, 1, 1, 1, 1} };
        int[] expected = { 5, 0, 31 };
        for (int i = 0; i < inputs.length; i++) {
            ListNode head = null, tail = null;
            for (int bit : inputs[i]) {
                ListNode node = new ListNode(bit);
                if (head == null) { head = node; tail = node; } else { tail.next = node; tail = node; }
            }
            int output = solver.getDecimalValue(head);
            System.out.printf("head=%s -> %d  expected=%d%n", Arrays.toString(inputs[i]), output, expected[i]);
        }
    }





    // Definition for singly-linked list
    public static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

                        /**
     * Intuition: reading a binary number left to right, each new bit shifts the
     * old value one place left. Multiplying result by 2 creates that place, and
     * adding current.val writes the current bit.
     *
     * Algorithm:
     *   1. Start result at 0 and current at head.
     *   2. For each node, set result = result * 2 + current.val.
     *   3. Move current to current.next until the list ends.
     *   4. Return result.
     *
     * Time:  O(n) - every node is visited once.
     * Space: O(1) - only result and current are stored.
     *
     * @param head first bit of the binary number
     * @return decimal value represented by the list
     */
    public int getDecimalValue(ListNode head) {
        int result = 0;
        ListNode current = head;

        while (current != null) {
            result = result * 2 + current.val;
            current = current.next;
        }

        return result;
    }

    /**
     * Alternative approach using bit shifting operations.
     * More explicit about the binary nature of the conversion.
     */
    public int getDecimalValueBitShift(ListNode head) {
        int result = 0;
        ListNode current = head;

        while (current != null) {
            result = (result << 1) | current.val;
            current = current.next;
        }

        return result;
    }

    /**
     * Approach that first calculates the length, then builds the result.
     * Useful for educational purposes to understand positional values.
     */
    public int getDecimalValuePositional(ListNode head) {
        if (head == null) return 0;

        // First pass: calculate length
        int length = 0;
        ListNode current = head;
        while (current != null) {
            length++;
            current = current.next;
        }

        // Second pass: calculate decimal value
        int result = 0;
        current = head;
        int position = length - 1;

        while (current != null) {
            if (current.val == 1) {
                result += (1 << position); // 2^position
            }
            position--;
            current = current.next;
        }

        return result;
    }

    /**
     * Recursive approach for educational purposes.
     * Less efficient due to call stack overhead but demonstrates recursion.
     */
    public int getDecimalValueRecursive(ListNode head) {
        return convertRecursive(head, 0);
    }

    /** Recursively carries the accumulated binary value to the end of the list. */
    /** Recursively carries the accumulated binary value to the end of the list. */
    /** Recursively carries the accumulated binary value to the end of the list. */
    /** Recursively carries the accumulated binary value to the end of the list. */
    /** Recursively carries the accumulated binary value to the end of the list. */
    private int convertRecursive(ListNode node, int result) {
        if (node == null) {
            return result;
        }

        return convertRecursive(node.next, result * 2 + node.val);
    }

    /**
     * Robust version with input validation and overflow handling.
     * Handles edge cases and validates input constraints.
     */
    public int getDecimalValueRobust(ListNode head) {
        if (head == null) {
            return 0;
        }

        long result = 0;
        ListNode current = head;

        while (current != null) {
            // Validate input
            if (current.val != 0 && current.val != 1) {
                throw new IllegalArgumentException("Invalid binary digit: " + current.val);
            }

            // Check for potential overflow
            if (result > (Integer.MAX_VALUE - current.val) / 2) {
                throw new ArithmeticException("Result exceeds integer range");
            }

            result = result * 2 + current.val;
            current = current.next;
        }

        return (int) result;
    }

    /**
     * Version that handles very long binary numbers using string representation.
     * Useful when the result might exceed integer or even long range.
     */
    public String getDecimalValueAsString(ListNode head) {
        if (head == null) return "0";

        StringBuilder binary = new StringBuilder();
        ListNode current = head;

        // Build binary string
        while (current != null) {
            binary.append(current.val);
            current = current.next;
        }

        // Convert binary string to decimal
        return convertBinaryStringToDecimal(binary.toString());
    }

    /** Converts a binary string into a decimal string without integer overflow. */
    /** Converts a binary string into a decimal string without integer overflow. */
    /** Converts a binary string into a decimal string without integer overflow. */
    /** Converts a binary string into a decimal string without integer overflow. */
    /** Converts a binary string into a decimal string without integer overflow. */
    private String convertBinaryStringToDecimal(String binary) {
        if (binary.isEmpty() || binary.equals("0")) return "0";

        String result = "0";
        String power = "1";

        // Process from right to left
        for (int i = binary.length() - 1; i >= 0; i--) {
            if (binary.charAt(i) == '1') {
                result = addStrings(result, power);
            }
            power = multiplyByTwo(power);
        }

        return result;
    }

    /** Adds two non-negative decimal strings. */
    /** Adds two non-negative decimal strings. */
    /** Adds two non-negative decimal strings. */
    /** Adds two non-negative decimal strings. */
    /** Adds two non-negative decimal strings. */
    private String addStrings(String num1, String num2) {
        StringBuilder result = new StringBuilder();
        int carry = 0;
        int i = num1.length() - 1, j = num2.length() - 1;

        while (i >= 0 || j >= 0 || carry > 0) {
            int sum = carry;
            if (i >= 0) sum += num1.charAt(i--) - '0';
            if (j >= 0) sum += num2.charAt(j--) - '0';

            result.append(sum % 10);
            carry = sum / 10;
        }

        return result.reverse().toString();
    }

    /** Multiplies a non-negative decimal string by two. */
    /** Multiplies a non-negative decimal string by two. */
    /** Multiplies a non-negative decimal string by two. */
    /** Multiplies a non-negative decimal string by two. */
    /** Multiplies a non-negative decimal string by two. */
    private String multiplyByTwo(String num) {
        StringBuilder result = new StringBuilder();
        int carry = 0;

        for (int i = num.length() - 1; i >= 0; i--) {
            int prod = (num.charAt(i) - '0') * 2 + carry;
            result.append(prod % 10);
            carry = prod / 10;
        }

        if (carry > 0) {
            result.append(carry);
        }

        return result.reverse().toString();
    }
}