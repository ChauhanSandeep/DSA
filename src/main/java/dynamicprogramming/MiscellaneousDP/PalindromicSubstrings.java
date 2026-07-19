package dynamicprogramming.MiscellaneousDP;

/**
 * Problem: Palindromic Substrings
 *
 * Given a string, count how many substrings are palindromes. Substrings are
 * contiguous, and equal text at different positions is counted separately.
 *
 * Leetcode: https://leetcode.com/problems/palindromic-substrings/
 * Rating:   acceptance 73.0% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Dynamic programming | Expand around center | Interval DP alternative
 *
 * Example:
 *   Input:  s = "aaa"
 *   Output: 6
 *   Why:    the three single letters, two "aa" substrings, and one "aaa" are all palindromes.
 *
 * Follow-ups:
 *   1. Return all distinct palindromic substrings?
 *      Add each palindrome to a set, or use a palindromic tree for large strings.
 *   2. Find the longest palindromic substring?
 *      Keep the best bounds while expanding centers.
 *   3. Can counting be O(n)?
 *      Yes, Manacher's algorithm counts palindromic radii in linear time.
 *
 * Related: Longest Palindromic Substring (5), Count Different Palindromic Subsequences (730).
 */
public class PalindromicSubstrings {

    private int count = 0;

    /**
     * Intuition (interview default): every palindrome has a center. Odd-length
     * palindromes center on a character, while even-length palindromes center
     * between two characters. If we expand from each possible center while the two
     * sides match, every successful expansion is one palindromic substring, and no
     * palindrome is missed because it has exactly one center.
     *
     * Algorithm:
     *   1. Reset the shared count before scanning centers.
     *   2. For each index, expand once for odd length and once for even length.
     *   3. Each matching expansion increments count until the pointers leave the string or mismatch.
     *   4. Return the accumulated count.
     *
     * Time:  O(n^2) - each center may expand across the whole string in the worst case.
     * Space: O(1) - only counters and two expanding pointers are stored.
     *
     * @param input string to inspect
     * @return number of palindromic substrings
     */
    public int countSubstrings(String input) {
        if (input == null || input.length() == 0) {
            return 0;
        }

        count = 0;

        for (int i = 0; i < input.length(); i++) {
            expandAroundCenter(input, i, i);      // Odd length palindromes
            expandAroundCenter(input, i, i + 1);  // Even length palindromes
        }

        return count;
    }

    private void expandAroundCenter(String input, int left, int right) {
        while (left >= 0 && right < input.length() && input.charAt(left) == input.charAt(right)) {
            count++;
            left--;
            right++;
        }
    }

    /**
     * Dynamic Programming Solution
     *
     * Approach:
     * 1. Create a 2D DP array where dp[i][j] is true if s[i..j] is a palindrome
     * 2. Initialize single character palindromes (dp[i][i] = true)
     * 3. Check for two character palindromes (s[i] == s[i+1])
     * 4. For substrings longer than 2 characters, use the DP table to check
     *
     * Time Complexity: O(n²)
     * Space Complexity: O(n²)
     */
    public int countSubstringsDP(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }

        int n = s.length();
        boolean[][] dp = new boolean[n][n];
        int count = 0;

        // Every single character is a palindrome
        for (int i = 0; i < n; i++) {
            dp[i][i] = true;
            count++;
        }

        // Check for two character palindromes
        for (int i = 0; i < n - 1; i++) {
            if (s.charAt(i) == s.charAt(i + 1)) {
                dp[i][i + 1] = true;
                count++;
            }
        }

        // Check for substrings of length 3 to n
        for (int len = 3; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;

                // If the first and last characters match and the substring between them is a palindrome
                if (s.charAt(i) == s.charAt(j) && dp[i + 1][j - 1]) {
                    dp[i][j] = true;
                    count++;
                }
            }
        }

        return count;
    }

    /**
     * Space-Optimized DP Solution
     *
     * Approach:
     * 1. Instead of using a 2D array, we can optimize space by using a 1D array
     * 2. We only need the previous row to compute the current row
     *
     * Time Complexity: O(n²)
     * Space Complexity: O(n)
     */
    public int countSubstringsOptimized(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }

        int n = s.length();
        boolean[] dp = new boolean[n];
        int count = 0;

        for (int i = n - 1; i >= 0; i--) {
            for (int j = n - 1; j >= i; j--) {
                // If characters at i and j are same and the substring between them is a palindrome
                dp[j] = (s.charAt(i) == s.charAt(j)) && (j - i < 3 || dp[j - 1]);

                if (dp[j]) {
                    count++;
                }
            }
        }

        return count;
    }

    public static void main(String[] args) {
        PalindromicSubstrings solver = new PalindromicSubstrings();
        String[] inputs = {"", "abc", "aaa", "a"};
        int[] expected = {0, 3, 6, 1};

        for (int i = 0; i < inputs.length; i++) {
            int got = solver.countSubstrings(inputs[i]);
            System.out.printf("s=\"%s\" -> %d  expected=%d%n", inputs[i], got, expected[i]);
        }
    }

}
