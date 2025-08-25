package dynamicprogramming;

/**
 * Problem: Wildcard Pattern Matching
 * LeetCode: https://leetcode.com/problems/wildcard-matching/
 *
 * Given an input string and a pattern, implement wildcard matching with support for:
 * - '?' → matches any single character
 * - '*' → matches any sequence of characters (including the empty sequence)
 *
 * Example:
 * Input: str = "baaababac", pattern = "ba?a*ac"
 * Output: true
 * Explanation: '?' matches a, '*' matches "bab", so full match is possible.
 *
 * Follow-up Questions:
 * - Can you optimize space to O(pattern.length())? (Yes, use rolling arrays)
 * - Can you implement it using recursion + memoization? [LC link: https://leetcode.com/problems/wildcard-matching/]
 *
 */
public class WildcardMatching {

    public static void main(String[] args) {
        String str = "baaababac";
        String pattern = "ba?a*ac";
        WildcardMatching solver = new WildcardMatching();
        System.out.println(solver.isMatch(str, pattern)); // Expected: true
    }

    /**
     * Dynamic Programming Bottom-Up approach.
     * Uses a 2D DP table to compute if str matches the pattern.
     *
     * Steps:
     * 1. Initialize a DP table where dp[i][j] indicates if str[0...i-1] matches pattern[0...j-1].
     * 2. Handle the base case where both str and pattern are empty.
     * 3. Fill the first row for patterns starting with '*' since '*' can match an empty sequence.
     * 4. Iterate through each character in str and pattern:
     *   - If the current pattern character is '*', it can match zero characters (dp[i][j-1]) or one/more characters (dp[i-1][j]).
     *   - If the current pattern character is '?', it matches any single character, or if it matches the current string character, we check the previous state (dp[i-1][j-1]).
     *   - If the characters do not match and the pattern character is not '?', set dp[i][j] to false.
     * 5. The final answer is found in dp[str.length()][pattern.length()].
     *
     * Time Complexity: O(N * M), where N = str.length, M = pattern.length
     * Space Complexity: O(N * M)
     *
     * @param str     The input string
     * @param pattern The wildcard pattern
     * @return True if the pattern matches the string, false otherwise
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
