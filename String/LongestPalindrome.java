package String;

import java.util.Arrays;

/**
 * Problem: Find the longest palindromic substring in a given string.
 * 
 * Approaches:
 * 1. **Dynamic Programming (O(n²) time, O(n²) space)**  
 *    - Maintain a DP table where dp[i][j] = true if substring(i, j) is a palindrome.
 *    - Build the table iteratively and track the longest palindromic substring.
 * 
 * 2. **Expand Around Center (O(n²) time, O(1) space) [Optimal for interviews]**  
 *    - Expand from every character (odd-length palindromes) and every pair (even-length palindromes).
 *    - Track the longest palindrome found.
 * 
 * Time Complexity: **O(n²)** for both approaches.  
 * Space Complexity: **O(n²) for DP, O(1) for Expand Around Center** (preferred for interviews).  
 * 
 * LeetCode Problem: https://leetcode.com/problems/longest-palindromic-substring/
 */
public class LongestPalindrome {
    public static void main(String[] args) {
        String input = "kjqlrfzzfmlvyoshiktodnsjjp";

        System.out.println("Longest Palindromic Substring (DP Approach): " + longestPalindromeDP(input));
        System.out.println("Longest Palindromic Substring (Expand Around Center Approach): " + longestPalindromeExpand(input));
    }

    /**
     * Dynamic Programming Approach: O(n²) time, O(n²) space
     */
    public static String longestPalindromeDP(String str) {
        int n = str.length();
        if (n < 2) return str; // If empty or single character, return the string itself.

        boolean[][] dp = new boolean[n][n];
        int maxLength = 1;
        String longestPalindrome = str.substring(0, 1); // Single character is always a palindrome.

        // All substrings of length 1 are palindromes.
        for (int i = 0; i < n; i++) {
            dp[i][i] = true;
        }

        // Check for palindromes of length 2.
        for (int i = 0; i < n - 1; i++) {
            if (str.charAt(i) == str.charAt(i + 1)) {
                dp[i][i + 1] = true;
                longestPalindrome = str.substring(i, i + 2);
                maxLength = 2;
            }
        }

        // Check for palindromes of length 3 and more.
        for (int length = 3; length <= n; length++) {
            for (int i = 0; i <= n - length; i++) {
                int j = i + length - 1;

                // A substring is a palindrome if the inner substring is a palindrome and the boundary characters match.
                if (str.charAt(i) == str.charAt(j) && dp[i + 1][j - 1]) {
                    dp[i][j] = true;

                    if (length > maxLength) {
                        maxLength = length;
                        longestPalindrome = str.substring(i, j + 1);
                    }
                }
            }
        }
        return longestPalindrome;
    }

    /**
     * Expand Around Center Approach: O(n²) time, O(1) space (Better for interviews)
     */
    public static String longestPalindromeExpand(String str) {
        if (str == null || str.length() < 2) return str;

        int start = 0, maxLength = 0;

        for (int i = 0; i < str.length(); i++) {
            // Expand for odd-length palindrome (single character center)
            int len1 = expandFromCenter(str, i, i);
            // Expand for even-length palindrome (double character center)
            int len2 = expandFromCenter(str, i, i + 1);

            int currentMax = Math.max(len1, len2);

            if (currentMax > maxLength) {
                maxLength = currentMax;
                start = i - (maxLength - 1) / 2;
            }
        }
        return str.substring(start, start + maxLength);
    }

    /**
     * Helper function to expand a palindrome from the given center.
     */
    private static int expandFromCenter(String str, int left, int right) {
        while (left >= 0 && right < str.length() && str.charAt(left) == str.charAt(right)) {
            left--;
            right++;
        }
        return right - left - 1; // Subtract extra expansion step
    }
}
