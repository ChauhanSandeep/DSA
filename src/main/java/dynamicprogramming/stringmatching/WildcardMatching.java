package dynamicprogramming.stringmatching;

/**
 * Problem: Wildcard Matching
 *
 * Match an entire string against a wildcard pattern where '?' matches one character and '*' matches any sequence.
 *
 * Leetcode: https://leetcode.com/problems/wildcard-matching/ (Hard)
 * Rating:   not available (not a contest problem)
 * Pattern:  Dynamic programming | String matching | Wildcard operators
 *
 * Example:
 *   Input:  str = "baaababac", pattern = "ba?a*ac"
 *   Output: true
 *   Why:    '?' matches one a and '*' absorbs the middle bab segment.
 *
 * Follow-ups:
 *   1. How would you return an actual solution, not only the value?
 *      Store predecessor or choice information while filling the same states.
 *   2. How can space be reduced?
 *      Keep only the previous row or active states when the recurrence allows it.
 *   3. How would constraints such as fees, limits, or weights change it?
 *      Add the constraint to the state or transition and keep the same invariant.
 *
 * Related: Regular Expression Matching (10), Edit Distance (72).
 */
public class WildcardMatching {

        public static void main(String[] args) {
        WildcardMatching solver = new WildcardMatching();
        String[][] inputs = { {"baaababac", "ba?a*ac"}, {"aa", "a"}, {"aa", "*"} };
        boolean[] expected = {true, false, true};
        for (int i = 0; i < inputs.length; i++) {
            boolean got = solver.isMatch(inputs[i][0], inputs[i][1]);
            System.out.printf("str=%s pattern=%s -> %s  expected=%s%n", inputs[i][0], inputs[i][1], got, expected[i]);
        }
    }

        /**
     * Intuition: dp[strIdx][patternIdx] means the prefixes match. A question mark or exact character consumes one from both; a star either matches empty from the left cell or absorbs the current string character from the cell above.
     *
     * Algorithm:
     *   1. Handle empty pattern.
     *   2. Create dp and set dp[0][0] = true.
     *   3. Initialize leading stars for the empty string.
     *   4. Fill all string and pattern prefixes.
     *   5. Return dp[strLen][patternLen].
     *
     * Time:  O(strLen * patternLen) - each state is filled once.
     * Space: O(strLen * patternLen) - stores the table.
     *
     * @param str input string
     * @param pattern wildcard pattern
     * @return true if the whole string matches
     */
public boolean isMatch(String str, String pattern) {
        int strLen = str.length();
        int patternLen = pattern.length();

        // If the pattern is empty, it only matches an empty string
        if (patternLen == 0) return strLen == 0;

        // DP table: dp[i][j] represents if str[0...i-1] matches pattern[0...j-1]
        boolean[][] dp = new boolean[strLen + 1][patternLen + 1];

        // Empty pattern matches empty string
        dp[0][0] = true;

        // Handling leading '*' in the pattern, as '*' can match an empty string
        for (int j = 1; j <= patternLen; j++) {
            if (pattern.charAt(j - 1) == '*') {
                dp[0][j] = dp[0][j - 1]; // '*' can represent an empty sequence
            }
        }

        // Fill the DP table
        for (int strIdx = 1; strIdx <= strLen; strIdx++) {
            for (int patternIdx = 1; patternIdx <= patternLen; patternIdx++) {
                char pChar = pattern.charAt(patternIdx - 1);
                char sChar = str.charAt(strIdx - 1);

                if (pChar == '*') {
                    // '*' can match zero (`dp[strIdx][patternIdx-1]`) or more characters (`dp[strIdx-1][patternIdx]`)
                    dp[strIdx][patternIdx] = dp[strIdx][patternIdx - 1] || dp[strIdx - 1][patternIdx];
                } else if (pChar == '?' || sChar == pChar) {
                    // '?' matches any character, or exact character match
                    dp[strIdx][patternIdx] = dp[strIdx - 1][patternIdx - 1];
                } else {
                    // Characters don't match
                    dp[strIdx][patternIdx] = false;
                }
            }
        }

        return dp[strLen][patternLen];
    }
}
