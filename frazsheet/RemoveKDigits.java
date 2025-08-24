package frazsheet;

import java.util.Stack;

/**
 * Given string num representing a non-negative integer num, and an integer k,
 * return the smallest possible integer after removing k digits from num.
 * 
 * Example 1:
 * Input: num = "1432219", k = 3
 * Output: "1219"
 * Explanation: Remove the three digits 4, 3, and 2 to form the new number 1219 which is the smallest.
 * 
 * Example 2:
 * Input: num = "10200", k = 1
 * Output: "200"
 * Explanation: Remove the leading 1 to form 200.
 * 
 * LeetCode: https://leetcode.com/problems/remove-k-digits/
 * 
 * Follow-up Questions:
 * 1. How would you handle very large input strings (e.g., 1,000,000 digits)?
 *    - The stack-based solution is O(n) time and O(n) space, which is efficient enough.
 * 2. What if we need to remove exactly k digits to form the largest possible number?
 *    - We would modify the algorithm to remove smaller digits when a larger digit is found.
 * 3. How would you modify the solution to work with negative numbers?
 *    - We would need to handle the negative sign separately and apply similar logic to the digits.
 * 
 * Related Problems:
 * - Create Maximum Number (https://leetcode.com/problems/create-maximum-number/)
 * - Monotone Increasing Digits (https://leetcode.com/problems/monotone-increasing-digits/)
 */
public class RemoveKDigits {
    /**
     * Removes k digits from the number to form the smallest possible number.
     * 
     * @param num String representation of the number
     * @param k Number of digits to remove
     * @return The smallest possible number as a string after removing k digits
     */
    public String removeKdigits(String num, int k) {
        // Edge cases
        if (k == 0) return num;
        if (num.length() == k) return "0";
        
        Stack<Character> stack = new Stack<>();
        
        for (char digit : num.toCharArray()) {
            // While the last digit in the stack is larger than the current digit and we can still remove digits
            while (k > 0 && !stack.isEmpty() && stack.peek() > digit) {
                stack.pop();
                k--;
            }
            stack.push(digit);
        }
        
        // Remove remaining k digits from the end if needed
        while (k > 0 && !stack.isEmpty()) {
            stack.pop();
            k--;
        }
        
        // Build the result string from the stack
        StringBuilder result = new StringBuilder();
        while (!stack.isEmpty()) {
            result.insert(0, stack.pop());
        }
        
        // Remove leading zeros
        int i = 0;
        while (i < result.length() && result.charAt(i) == '0') {
            i++;
        }
        
        // If all digits were zeros, return "0"
        if (i == result.length()) {
            return "0";
        }
        
        return result.substring(i);
    }
    
    /**
     * Optimized solution using a char array instead of a stack.
     * This approach is more memory efficient.
     */
    public String removeKdigitsOptimized(String num, int k) {
        int n = num.length();
        if (k == n) return "0";
        
        char[] digits = num.toCharArray();
        char[] stack = new char[n];
        int top = -1; // Points to the top of the stack
        
        for (char digit : digits) {
            while (k > 0 && top >= 0 && stack[top] > digit) {
                top--; // Remove the top element
                k--;
            }
            stack[++top] = digit; // Push to stack
        }
        
        // If there are still digits to remove, remove from the end
        int end = top - k;
        
        // Find the first non-zero digit
        int start = 0;
        while (start <= end && stack[start] == '0') {
            start++;
        }
        
        // If all digits were zeros, return "0"
        if (start > end) {
            return "0";
        }
        
        return new String(stack, start, end - start + 1);
    }
    
    /**
     * Recursive solution (less efficient but demonstrates an alternative approach).
     * This approach is not recommended for large inputs due to stack overflow risk.
     */
    public String removeKdigitsRecursive(String num, int k) {
        // Base cases
        if (k == 0) return num;
        if (num.length() == k) return "0";
        
        // Find the first digit that's greater than the next digit
        int i = 0;
        while (i < num.length() - 1 && num.charAt(i) <= num.charAt(i + 1)) {
            i++;
        }
        
        // Remove the digit at position i
        String newNum = num.substring(0, i) + num.substring(i + 1);
        
        // Remove leading zeros
        int j = 0;
        while (j < newNum.length() && newNum.charAt(j) == '0') {
            j++;
        }
        newNum = j == newNum.length() ? "0" : newNum.substring(j);
        
        // Recurse with k-1
        return removeKdigitsRecursive(newNum, k - 1);
    }
}
