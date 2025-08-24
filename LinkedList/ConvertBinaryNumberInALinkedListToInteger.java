package LinkedList;

/**
 * 1290. Convert Binary Number in a Linked List to Integer
 * 
 * Problem: Given head which is a reference node to a singly-linked list where
 * each node contains either 0 or 1, convert the binary number in the linked list to integer.
 * 
 * Example:
 * Input: head = [1,0,1]
 * Output: 5
 * Explanation: (101) in base 2 = (5) in base 10
 * 
 * LeetCode: https://leetcode.com/problems/convert-binary-number-in-a-linked-list-to-integer
 * 
 * Follow-up questions:
 * Q: What if the linked list is very long (exceeds integer range)?
 * A: Use BigInteger or return the result modulo some value, or use string representation.
 * 
 * Q: How to handle invalid input (values other than 0 or 1)?
 * A: Add validation to check each node value, throw exception or return error code.
 * 
 * Q: Can we solve this in-place without modifying the list?
 * A: Yes, both provided solutions don't modify the original list.
 */
public class ConvertBinaryNumberInALinkedListToInteger {
    
    // Definition for singly-linked list
    public static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }
    
    /**
     * Converts binary number represented in linked list to decimal integer.
     * 
     * Algorithm: Iterate through list and build decimal value
     * - Start with result = 0
     * - For each bit, shift result left (multiply by 2) and add current bit
     * - Mathematical equivalent: result = result * 2 + current_bit
     * 
     * Time Complexity: O(n) where n is the number of nodes
     * Space Complexity: O(1) constant extra space
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
    
    // Recursive helper
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
    
    // Convert binary string to decimal string for large numbers
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
    
    // Helper to add two number strings
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
    
    // Helper to multiply string number by 2
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