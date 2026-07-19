package strings.stack;

/**
 * Problem: Valid Parenthesis String
 *
 * Given a string containing '(', ')', and '*', decide whether '*' characters can
 * be assigned as '(', ')', or empty so the final parentheses string is valid.
 *
 * Leetcode: https://leetcode.com/problems/valid-parenthesis-string/ (Medium)
 * Rating:   no contest Elo (pre-contest problem)
 * Pattern:  Greedy | Range of open counts | Wildcard balancing
 *
 * Example:
 *   Input:  s = "(*))"
 *   Output: true
 *   Why:    treating '*' as '(' gives "(())", which is balanced.
 *
 * Follow-ups:
 *   1. Return one valid assignment of each '*'
 *      Track choices with stacks or backtracking once the greedy says a solution exists.
 *   2. Support multiple bracket types?
 *      Greedy ranges are not enough; use stack or DP state per bracket type.
 *   3. Count all valid assignments?
 *      Use dynamic programming over index and open-parenthesis count.
 *
 * Related: Valid Parentheses (20), Generate Parentheses (22), Decode String (394).
 */
public class ValidParenthesisString {

    public static void main(String[] args) {
        ValidParenthesisString solver = new ValidParenthesisString();
        String[] inputs = {"()", "(*)", "(*))", ")("};
        boolean[] expected = {true, true, true, false};

        for (int i = 0; i < inputs.length; i++) {
            boolean got = solver.checkValidString(inputs[i]);
            System.out.printf("s=%s -> %s  expected=%s%n", inputs[i], got, expected[i]);
        }
    }


    /**
     * Intuition: after reading a prefix, '*' choices create a range of possible open
     * parenthesis counts rather than one exact count. minOpenParCount is the fewest
     * opens we might have, and maxOpenParCount is the most. The string remains
     * viable while that range can still include a non-negative count.
     *
     * Algorithm:
     *   1. Return true for null input as the restored original code does.
     *   2. Scan each character while maintaining minimum and maximum possible open counts.
     *   3. Update the range for '(', ')', and '*' according to their possible meanings.
     *   4. If maxOpenParCount becomes negative, return false; otherwise accept when zero remains reachable.
     *
     * Time:  O(n) - one pass over the string.
     * Space: O(1) - only two counters are stored.
     *
     * @param input string containing parentheses and wildcard stars
     * @return true if some assignment of '*' makes the string valid
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
