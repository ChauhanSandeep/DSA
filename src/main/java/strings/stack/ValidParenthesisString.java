package strings.stack;

/**
 * LeetCode 678. Valid Parenthesis String
 *
 * Given a string s containing only three types of characters: '(', ')' and '*', return true if s is valid.
 * The following rules define a valid string:
 * - Any left parenthesis '(' must have a corresponding right parenthesis ')'.
 * - Any right parenthesis ')' must have a corresponding left parenthesis '('.
 * - Left parenthesis '(' must go before the corresponding right parenthesis ')'.
 * - '*' could be treated as a single right parenthesis ')' or a single left parenthesis '(' or an empty string "".
 *
 * Example 1:
 * Input: s = "()"
 * Output: true
 *
 * Example 2:
 * Input: s = "(*)"
 * Output: true
 *
 * Example 3:
 * Input: s = "(*))"
 * Output: true
 *
 * LeetCode Link: https://leetcode.com/problems/valid-parenthesis-string/
 *
 * Follow-up Questions:
 * - How would you handle multiple types of brackets with wildcards? (Extend tracking for each bracket type)
 * - Can you solve this using dynamic programming? (DP approach with state: position and open count)
 * - How would you find the actual assignment of '*' characters? (Backtracking with assignment tracking)
 * - What if '*' could represent more than one character? (Modify the range expansion logic)
 */
public class ValidParenthesisString {

    /**
     * Validates parenthesis string with wildcards using range tracking approach.
     *
     * The idea is that we maintain a range of possible open parentheses counts as we iterate through the string.
     * At each step, we update the range based on the current character:
     * - '(' : increment both min and max (must add one open)
     * - ')' : decrement both min and max, ensure min >= 0 (close one open)
     * - '*' : decrement min (use as ')'), increment max (use as '(')
     *
     * If at any point maxOpen < 0, it means we can't balance the string, so we return false.
     * At the end, we check if 0 is within the range [minOpen, maxOpen].
     *
     * Algorithm:
     * 1. Track range of possible open parentheses count: [minOpen, maxOpen]
     * 2. For '(': increment both min and max (must add one open)
     * 3. For ')': decrement both min and max, ensure min >= 0 (close one open)
     * 4. For '*': decrement min (use as ')'), increment max (use as '(')
     * 5. If maxOpen < 0 at any point, impossible to balance
     * 6. At end, check if 0 is within range [minOpen, maxOpen]
     *
     * Time Complexity: O(n) where n is length of string
     * Space Complexity: O(1) - only uses constant extra space
     *
     * @param input String containing '(', ')', and '*' characters
     * @return true if string can be made valid, false otherwise
     */
    public boolean checkValidString(String input) {
        if (input == null) {
            return true;
        }

        // Min and max possible open parentheses
        int minOpenParCount = 0; // Minimum possible open parentheses
        int maxOpenParCount = 0; // Maximum possible open parentheses

        for (char c : input.toCharArray()) {
            if (c == '(') {
                // Must treat as open parenthesis
                minOpenParCount++;
                maxOpenParCount++;
            } else if (c == ')') {
                // Must treat as close parenthesis
                minOpenParCount = Math.max(0, minOpenParCount - 1);
                maxOpenParCount--;
            } else { // c == '*'
                // Can treat as ')', '(', or empty
                minOpenParCount = Math.max(0, minOpenParCount - 1); // Treat as ')'. If goes negative, reset to 0 (taking * as empty string)
                maxOpenParCount++; // Treat as '('
            }

            // If maximum possible opens becomes negative anywhere in traversal, its impossible to balance
            if (maxOpenParCount < 0) {
                return false;
            }
        }

        // Valid if 0 is within the range of possible open parentheses
        return minOpenParCount <= 0 && maxOpenParCount >= 0;
    }

    /**
     * Two-pass approach.
     *
     * Algorithm:
     * 1. First pass: left to right, check if we can balance all ')'. Here we treat '*' as '('
     * 2. Second pass: right to left, check if we can balance all '('. Here we treat '*' as ')'
     * 3. If both passes are valid, return true
     * 
     * Why this works:
     * - First pass ensures that at no point do we have more closing parentheses ) than we can 
     *  possibly match. It's done by treating every * optimistically as an opening parenthesis (
     * - Second pass ensures that at no point do we have more opening parentheses ( than we can
     * possibly match. Here we treat every * as a closing parenthesis )
     * - If both conditions are satisfied, it means there exists a way to assign * such that
     * all parentheses are balanced.
     *
     * Time Complexity: O(n) where n is length of string
     * Space Complexity: O(1) - only uses constant extra space
     */
    public boolean checkValidStringTwoPass(String input) {
        // First pass: left to right, check if we can balance all ')'. Here we treat '*' as '('
        int balance = 0;
        for (char c : input.toCharArray()) {
            if (c == '(' || c == '*') {
                balance++;
            } else { // c == ')'
                balance--;
            }

            if (balance < 0) {
                return false;
            }
        }

        // Second pass: right to left, check if we can balance all '('. Here we treat '*' as ')'
        balance = 0;
        for (int i = input.length() - 1; i >= 0; i--) {
            char c = input.charAt(i);
            if (c == ')' || c == '*') {
                balance++;
            } else { // c == '('
                balance--;
            }

            if (balance < 0) {
                return false;
            }
        }

        return true;
    }
}
