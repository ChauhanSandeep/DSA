package dynamicprogramming.MiscellaneousDP;

import java.util.*;


/**
 * Problem: Longest Valid Parentheses
 *
 * Given a string containing only '(' and ')', return the length of the longest
 * contiguous substring that forms valid parentheses.
 *
 * Leetcode: https://leetcode.com/problems/longest-valid-parentheses/
 * Rating:   acceptance 39.2% (Hard) - no contest Elo (pre-contest problem)
 * Pattern:  Dynamic programming adjacent | Two-pass counters | Stack alternative
 *
 * Example:
 *   Input:  s = ")()())"
 *   Output: 4
 *   Why:    the substring "()()" is valid and has length 4; no longer contiguous valid block exists.
 *
 * Follow-ups:
 *   1. Return the substring itself?
 *      Track the start index whenever maxLength is updated.
 *   2. Support multiple bracket types?
 *      Use the stack approach with bracket matching instead of simple counters.
 *   3. Count all valid substrings?
 *      Use DP where dp[i] is the longest valid suffix ending at i, then aggregate carefully.
 *
 * Related: Valid Parentheses (20), Generate Parentheses (22), Remove Invalid Parentheses (301).
 */
public class LongestValidParentheses {

    public static void main(String[] args) {
        LongestValidParentheses solver = new LongestValidParentheses();
        String[] inputs = {"", "(()", ")()())", "(())"};
        int[] expected = {0, 2, 4, 4};

        for (int i = 0; i < inputs.length; i++) {
            int got = solver.longestValidParentheses(inputs[i]);
            System.out.printf("s=\"%s\" -> %d  expected=%d%n", inputs[i], got, expected[i]);
        }
    }


  /**
     * Intuition (interview default): a left-to-right scan can recognize valid
     * blocks whenever opens and closes balance, but it must reset when closes get
     * ahead because no earlier open can fix that prefix. The mirror problem happens
     * with extra opens, so a right-to-left scan catches cases like "(()". Together,
     * the two scans cover both kinds of imbalance while using only counters.
     *
     * Algorithm:
     *   1. Scan left to right, counting open and closed parentheses.
     *   2. Update maxLength when counts match, and reset when closed exceeds open.
     *   3. Scan right to left with the same counters.
     *   4. Update on balance again, and reset when open exceeds closed.
     *
     * Time:  O(n) - the string is scanned twice.
     * Space: O(1) - only counters and the best length are stored.
     *
     * @param input parentheses string
     * @return length of the longest valid parentheses substring
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
