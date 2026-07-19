package strings.twopointers;

/**
 * Problem: Longest Palindromic Substring
 *
 * Given a string, return the longest contiguous substring that reads the same
 * forward and backward. If multiple answers have the same length, returning any
 * one of them is acceptable.
 *
 * Leetcode: https://leetcode.com/problems/longest-palindromic-substring/ (Medium)
 * Rating:   no contest Elo (pre-contest problem)
 * Pattern:  Strings | Two pointers | Expand around center
 *
 * Example:
 *   Input:  s = "babad"
 *   Output: "bab"
 *   Why:    "bab" is a length-3 palindrome; "aba" is also valid with the same length.
 *
 * Follow-ups:
 *   1. Can this be solved in O(n) time?
 *      Yes, Manacher's algorithm finds all palindrome radii in linear time.
 *   2. How would you return all longest palindromic substrings?
 *      Collect every substring whose expanded length equals the current best.
 *   3. What if you need many palindrome-range queries?
 *      Precompute DP or rolling hashes to answer each query quickly.
 *
 * Related: Palindromic Substrings (647), Longest Palindromic Subsequence (516).
 */
public class LongestPalindrome {
    public static void main(String[] args) {
        LongestPalindrome solver = new LongestPalindrome();

        String[] inputs = {"babad", "cbbd", ""};
        String[] expected = {"bab", "bb", ""};

        for (int i = 0; i < inputs.length; i++) {
            String got = solver.longestPalindrome(inputs[i]);
            System.out.printf("s=%s -> %s  expected=%s%n",
                inputs[i], got, expected[i]);
        }
    }


        /**
     * Intuition: every palindrome expands from a center. The center can be one
     * character for odd lengths or the gap between two characters for even lengths,
     * so trying both centers at each index finds every candidate palindrome.
     *
     * Algorithm:
     *   1. Return "" for null or empty input.
     *   2. For every index, expand around the odd and even centers.
     *   3. Keep the start and length when a longer palindrome is found.
     *   4. Return the substring described by the best start and length.
     *
     * Time:  O(n^2) - each center can expand across the string.
     * Space: O(1) - only indices and lengths are stored.
     *
     * @param str Input string.
     * @return Longest palindromic substring, or "" for null/empty input.
     */
    public String longestPalindrome(String str) {
        if (str == null || str.length() < 1) {
            return "";
        }
        
        int start = 0;  // Start index of longest palindrome
        int maxLen = 0; // Length of longest palindrome
        
        for (int i = 0; i < str.length(); i++) {
            // Check odd-length palindrome (single character center)
            int len1 = expandAroundCenter(str, i, i);
            
            // Check even-length palindrome (two character center)
            int len2 = expandAroundCenter(str, i, i + 1);
            
            // Take maximum of both
            int len = Math.max(len1, len2);
            
            // Update longest palindrome if current is longer
            if (len > maxLen) {
                maxLen = len;
                // Calculate start index based on center and length
                start = i - (len - 1) / 2;
            }
        }
        
        return str.substring(start, start + maxLen);
    }

        /** Expands from left and right and returns the palindrome length found. */
    private int expandAroundCenter(String str, int left, int right) {
        // Expand while within bounds and characters match
        while (left >= 0 && right < str.length() && str.charAt(left) == str.charAt(right)) {
            left--;
            right++;
        }
        
        // Return length of palindrome
        // When loop exits, left and right point to non-matching characters
        // Length = (right - 1) - (left + 1) + 1 = right - left - 1
        return right - left - 1;
    }

    /**
     * Dynamic Programming Approach:
     * Step-by-step:
     * 1. Create a 2D boolean DP table dp[i][j] indicating if substring(i, j) is a palindrome
     * 2. Initialize single character substrings as palindromes (dp[i][i] = true)
     * 3. Check for palindromes of length 2 and mark in DP table
     * 4. For lengths 3 to n, check substrings:
     *    - If boundary characters match and inner substring is palindrome, mark dp[i][j] = true
     * 5. Track the longest palindrome found during the process
     * 6. Return the longest palindromic substring using tracked indices
     * 
     * Key Insight:
     * A substring is a palindrome if its boundary characters match and the inner substring is also a palindrome.
     * This allows building solutions for longer substrings based on shorter ones.
     * 
     * Algorithm: Dynamic Programming.
     * Time Complexity: O(n²) due to nested loops for substring lengths and start indices.
     * Space Complexity: O(n²) for the DP table.
     */
    public static String longestPalindromeDP(String str) {
        int length = str.length();
        if (length < 2) return str; // If empty or single character, return the string itself.

        boolean[][] dp = new boolean[length][length];
        int maxLength = 1;
        String longestPalindrome = str.substring(0, 1); // Single character is always a palindrome.

        // All substrings of length 1 are palindromes.
        for (int i = 0; i < length; i++) {
            dp[i][i] = true;
        }

        // Check for palindromes of length 2.
        for (int i = 0; i < length - 1; i++) {
            if (str.charAt(i) == str.charAt(i + 1)) {
                dp[i][i + 1] = true;
                longestPalindrome = str.substring(i, i + 2);
                maxLength = 2;
            }
        }

        // Check for palindromes of length 3 and more.
        for (int gap = 3; gap <= length; gap++) {
            for (int startIndex = 0; startIndex <= length - gap; startIndex++) {
                int endIndex = startIndex + gap - 1;

                // A substring is a palindrome if the inner substring is a palindrome and the boundary characters match.
                if (str.charAt(startIndex) == str.charAt(endIndex) && dp[startIndex + 1][endIndex - 1]) {
                    dp[startIndex][endIndex] = true;

                    if (gap > maxLength) {
                        maxLength = gap;
                        longestPalindrome = str.substring(startIndex, endIndex + 1);
                    }
                }
            }
        }
        return longestPalindrome;
    }
}
