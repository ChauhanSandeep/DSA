package strings.twopointers;

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
        System.out.println("Longest Palindromic Substring (Expand Around Center Approach): " + new LongestPalindrome().longestPalindrome(input));
    }

    /**
     * Main method: Expand Around Center approach (Optimal for interviews).
     * Step-by-step:
     *  1. For each position in string, treat it as potential palindrome center
     *  2. Expand around center in two cases:
     *     a. Odd-length palindrome: single character as center (i, i)
     *     b. Even-length palindrome: two characters as center (i, i+1)
     *  3. For each center, expand outward while characters match
     *  4. Track longest palindrome found (start index and length)
     *  5. Return substring using tracked indices
     *
     * Key Insight:
     * Every palindrome has a center. We try each possible center and expand
     * outward while characters match. Need to check both odd-length (single center)
     * and even-length (two-character center) palindromes.
     *
     * Algorithm: Expand Around Center.
     * Time Complexity: O(n²), where n is string length. For each of n centers, expand takes O(n).
     * Space Complexity: O(1), only storing indices and result.
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

    /**
     * Helper: Expands around center and returns length of palindrome.
     * 
     * @param str Input string
     * @param left Left pointer (center or left of center)
     * @param right Right pointer (center or right of center)
     * @return Length of palindrome found by expanding from center
     */
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
