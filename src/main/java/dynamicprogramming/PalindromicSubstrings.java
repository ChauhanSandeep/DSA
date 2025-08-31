package dynamicprogramming;

/**
 * Problem: Palindromic Substrings (LeetCode #647)
 *
 * Problem Statement:
 * Given a string s, return the number of palindromic substrings in it.
 * A string is a palindrome when it reads the same backward as forward.
 * A substring is a contiguous sequence of characters within the string.
 *
 * Example 1:
 * Input: s = "abc"
 * Output: 3
 * Explanation: Three palindromic strings: "a", "b", "c".
 *
 * Example 2:
 * Input: s = "aaa"
 * Output: 6
 * Explanation: Six palindromic strings: "a", "a", "a", "aa", "aa", "aaa".
 *
 * Approaches:
 * 1. Expand Around Center (Optimal): O(n²) time, O(1) space
 *    - Consider each character and each pair of characters as the center of a palindrome
 *    - Expand outwards to count all palindromes with that center
 *
 * 2. Dynamic Programming: O(n²) time, O(n²) space
 *    - Use a 2D DP array where dp[i][j] is true if s[i..j] is a palindrome
 *    - Fill the DP table and count palindromic substrings
 *
 * 3. Brute Force: O(n³) time, O(1) space
 *    - Check all possible substrings and count palindromes
 *
 * Time Complexity: O(n²) for optimal solution
 * Space Complexity: O(1) for optimal solution
 *
 * Follow-up Questions:
 * 1. What if we need to return all the distinct palindromic substrings instead of just counting them?
 *    Answer: We can modify the solution to collect all palindromic substrings in a set to avoid duplicates.
 *
 * 2. How would you find the longest palindromic substring?
 *    Answer: We can modify the expand around center approach to keep track of the longest palindrome found.
 *
 * 3. What if the string is very large (millions of characters)?
 *    Answer: For very large strings, we might need more advanced algorithms like Manacher's algorithm (O(n) time).
 *
 * LeetCode: https://leetcode.com/problems/palindromic-substrings/
 */
public class PalindromicSubstrings {

    private int count = 0;

    /**
     * Optimal Expand Around Center Solution
     *
     * Approach:
     * 1. For each character in the string, consider it as the center of a palindrome
     * 2. Expand around the center to find all palindromes
     * 3. Handle both odd-length and even-length palindromes
     *
     * Time Complexity: O(n²) - Each expansion can take O(n) time in the worst case
     * Space Complexity: O(1) - Only constant extra space is used
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
        PalindromicSubstrings solution = new PalindromicSubstrings();

        // Test cases
        System.out.println("Test 1: " + solution.countSubstrings("abc"));      // Expected: 3
        System.out.println("Test 2: " + solution.countSubstrings("aaa"));      // Expected: 6
        System.out.println("Test 3: " + solution.countSubstrings("a"));        // Expected: 1
        System.out.println("Test 4: " + solution.countSubstrings(""));         // Expected: 0
        System.out.println("Test 5: " + solution.countSubstrings("racecar"));  // Expected: 10

        // Test DP solution
        System.out.println("\nDP Solution:");
        System.out.println("Test 1: " + solution.countSubstringsDP("abc"));     // Expected: 3
        System.out.println("Test 2: " + solution.countSubstringsDP("aaa"));     // Expected: 6

        // Test optimized solution
        System.out.println("\nOptimized Solution:");
        System.out.println("Test 1: " + solution.countSubstringsOptimized("abc"));  // Expected: 3
        System.out.println("Test 2: " + solution.countSubstringsOptimized("aaa"));  // Expected: 6
    }
}
