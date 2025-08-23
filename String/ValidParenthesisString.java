package String;

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
     * @param s String containing '(', ')', and '*' characters
     * @return true if string can be made valid, false otherwise
     */
    public boolean checkValidString(String s) {
        if (s == null) {
            return true;
        }

        int minOpen = 0; // Minimum possible open parentheses
        int maxOpen = 0; // Maximum possible open parentheses

        for (char c : s.toCharArray()) {
            if (c == '(') {
                // Must treat as open parenthesis
                minOpen++;
                maxOpen++;
            } else if (c == ')') {
                // Must treat as close parenthesis
                minOpen = Math.max(0, minOpen - 1);
                maxOpen--;
            } else { // c == '*'
                // Can treat as ')', '(', or empty
                minOpen = Math.max(0, minOpen - 1); // Treat as ')'
                maxOpen++; // Treat as '('
            }

            // If maximum possible opens becomes negative, impossible to balance
            if (maxOpen < 0) {
                return false;
            }
        }

        // Valid if we can have exactly 0 open parentheses at the end
        return minOpen <= 0 && maxOpen >= 0;
    }

    /**
     * Alternative two-pass approach for validation.
     */
    public boolean checkValidStringTwoPass(String s) {
        // First pass: left to right, check if we can balance all ')'
        int balance = 0;
        for (char c : s.toCharArray()) {
            if (c == '(' || c == '*') {
                balance++;
            } else { // c == ')'
                balance--;
            }

            if (balance < 0) {
                return false;
            }
        }

        // Second pass: right to left, check if we can balance all '('
        balance = 0;
        for (int i = s.length() - 1; i >= 0; i--) {
            char c = s.charAt(i);
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

    /**
     * Dynamic Programming approach for educational purposes.
     */
    public boolean checkValidStringDP(String s) {
        int n = s.length();
        // dp[i][j] = true if substring s[i...j] can be made valid
        boolean[][] dp = new boolean[n][n];

        // Base case: single characters
        for (int i = 0; i < n; i++) {
            if (s.charAt(i) == '*') {
                dp[i][i] = true;
            }
        }

        // Fill DP table for increasing lengths
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;

                // Case 1: s[i] and s[j] form a pair
                char left = s.charAt(i);
                char right = s.charAt(j);

                if ((left == '(' || left == '*') && (right == ')' || right == '*')) {
                    if (len == 2) {
                        dp[i][j] = true;
                    } else if (dp[i + 1][j - 1]) {
                        dp[i][j] = true;
                    }
                }

                // Case 2: split into two valid substrings
                for (int k = i; k < j; k++) {
                    if (dp[i][k] && dp[k + 1][j]) {
                        dp[i][j] = true;
                        break;
                    }
                }

                // Case 3: s[i] is treated as empty (if it's '*')
                if (s.charAt(i) == '*' && dp[i + 1][j]) {
                    dp[i][j] = true;
                }

                // Case 4: s[j] is treated as empty (if it's '*')
                if (s.charAt(j) == '*' && dp[i][j - 1]) {
                    dp[i][j] = true;
                }
            }
        }

        return dp[0][n - 1];
    }
}
