package dynamicprogramming.MiscellaneousDP;

import java.util.*;


/**
 * LongestValidParentheses.java
 *
 * Problem Statement:
 * Given a string containing just the characters '(' and ')', return the length of the longest 
 * valid (well-formed) parentheses substring.
 *
 * Example:
 * Input: s = "(()"
 * Output: 2
 * Explanation: The longest valid parentheses substring is "()".
 *
 * Input: s = ")()())"
 * Output: 4
 * Explanation: The longest valid parentheses substring is "()()".
 *
 * LeetCode link: https://leetcode.com/problems/longest-valid-parentheses/
 *
 * Follow-up Questions FAANG Interviews Might Ask:
 *  - Can you solve it in one pass instead of two?
 *    → Yes, use stack-based approach for single pass O(n) solution.
 *  - What if you need to return the actual substring, not just length?
 *    → Track start index when updating maxLength in any approach.
 *  - Can you handle multiple types of brackets ([], {}, ())?
 *    → Extend stack approach to validate matching pairs.
 *  - What if string is extremely long (billions of characters)?
 *    → Use streaming approach with constant space (two-pass counter method).
 *
 * Relevant Follow-up Problems:
 *  - LeetCode 20 (Valid Parentheses): https://leetcode.com/problems/valid-parentheses/
 *  - LeetCode 22 (Generate Parentheses): https://leetcode.com/problems/generate-parentheses/
 *  - LeetCode 301 (Remove Invalid Parentheses): https://leetcode.com/problems/remove-invalid-parentheses/
 */
public class LongestValidParentheses {
  public static void main(String[] args) {
    LongestValidParentheses lvp = new LongestValidParentheses();
    System.out.println(lvp.longestValidParenthesesStack("(()"));          // Output: 2
    System.out.println(lvp.longestValidParenthesesStack("(())"));         // Output: 4
    System.out.println(lvp.longestValidParenthesesStack("))()()(()()()"));// Output: 6

    System.out.println(lvp.longestValidParentheses("(()"));           // Output: 2
    System.out.println(lvp.longestValidParentheses("(())"));          // Output: 4
    System.out.println(lvp.longestValidParentheses("))()()(()()()")); // Output: 6
  }

  /**
     * Main method: Two-pass counter approach (Optimal for space).
     * Step-by-step:
     *  1. First pass (left to right):
     *     - Count open and closed parentheses
     *     - When open == closed: valid substring, update maxLength
     *     - When closed > open: reset counters (invalid, can't recover)
     *  2. Second pass (right to left):
     *     - Same logic but swap conditions
     *     - When open == closed: valid substring, update maxLength
     *     - When open > closed: reset counters
     *  3. Return maxLength
     *
     * Why two passes?
     * - Left-to-right catches cases like "()()"
     * - Right-to-left catches cases like "(()"
     * - Example: "(()" - left pass misses it, right pass finds "()"
     *
     * Key Insight:
     * Left-to-right scan handles excess closing parentheses by resetting.
     * Right-to-left scan handles excess opening parentheses by resetting.
     * Together they cover all cases without needing extra space.
     *
     * Algorithm: Two-pass counter with reset.
     * Time Complexity: O(n), two linear passes.
     * Space Complexity: O(1), only counters used.
     */
    public int longestValidParentheses(String input) {
        if (input == null || input.length() < 2) {
            return 0;
        }

        int maxLength = 0;
        int open = 0;
        int closed = 0;
        
        // Left to right pass
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '(') {
                open++;
            } else {
                closed++;
            }
            
            // Valid substring when balanced
            if (open == closed) {
                maxLength = Math.max(maxLength, 2 * open);
            } else if (closed > open) {
                // Too many closing brackets, reset
                open = 0;
                closed = 0;
            }
        }
        
        // Reset counters for right to left pass
        open = 0;
        closed = 0;
        
        // Right to left pass
        for (int i = input.length() - 1; i >= 0; i--) {
            if (input.charAt(i) == '(') {
                open++;
            } else {
                closed++;
            }
            
            // Valid substring when balanced
            if (open == closed) {
                maxLength = Math.max(maxLength, 2 * open);
            } else if (open > closed) {
                // Too many opening brackets, reset
                open = 0;
                closed = 0;
            }
        }
        
        return maxLength;
    }

    /**
     * Alternative method 1: Stack-based approach (Single pass, more intuitive).
     * Step-by-step:
     *  1. Use stack to track indices of unmatched parentheses
     *  2. Initialize stack with -1 (base for length calculation)
     *  3. For each character:
     *     - If '(': push index to stack
     *     - If ')': pop from stack
     *       - If stack empty after pop: push current index (new base)
     *       - If stack not empty: calculate length from top of stack
     *  4. Return maxLength
     *
     * Key Insight:
     * Stack maintains indices of unmatched parentheses. When we find a match,
     * the distance from current index to top of stack gives valid length.
     * Stack top always points to last unmatched index or base.
     *
     * Algorithm: Stack-based index tracking.
     * Time Complexity: O(n), single pass.
     * Space Complexity: O(n) for stack in worst case.
     */
    public int longestValidParenthesesStack(String input) {
        if (input == null || input.length() < 2) {
            return 0;
        }

        Stack<Integer> stack = new Stack<>();
        stack.push(-1);  // Base for length calculation
        int maxLength = 0;
        
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '(') {
                // Push index of opening bracket
                stack.push(i);
            } else {
                // Pop for closing bracket
                stack.pop();
                
                if (stack.isEmpty()) {
                    // No matching opening bracket, push current as new base
                    stack.push(i);
                } else {
                    // Calculate length from last unmatched position
                    int length = i - stack.peek();
                    maxLength = Math.max(maxLength, length);
                }
            }
        }
        
        return maxLength;
    }
}
