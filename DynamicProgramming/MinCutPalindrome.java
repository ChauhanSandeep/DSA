package DynamicProgramming;

import java.util.Arrays;

/**
 * Problem: Palindrome Partitioning II
 *
 * Given a string `s`, return the minimum number of cuts needed to partition
 * it such that every substring in the partition is a palindrome.
 *
 * Example:
 * Input: s = "aab"
 * Output: 1
 * Explanation: "aa|b" is a valid partition with only 1 cut.
 *
 * Leetcode Link: https://leetcode.com/problems/palindrome-partitioning-ii/
 *
 * Follow-up Questions:
 * 1. Can you print all valid palindromic partitions?
 *    - Yes, use backtracking + `isPalindrome[][]` DP table.
 * 2. Can you solve this in O(N²) time and O(N) space?
 *    - Yes, with optimized center-expansion palindrome check instead of full 2D table.
 */
public class MinCutPalindrome {

    public static void main(String[] args) {
        System.out.println(new MinCutPalindrome().minCut("aab")); // Expected Output: 1
    }

    /**
     * Top-down recursive approach with memoization.
     *
     * Strategy:
     * - Try partitioning the string at all valid palindromic substrings.
     * - Recursively compute min cuts on the remaining suffix.
     * - Memoize overlapping subproblems with a 2D dp array.
     *
     * Time: O(N^3) due to repeated isPalindrome checks.
     * Space: O(N^2) for memo table.
     */
    public int minCut(String str) {
        int length = str.length();
        int[][] dp = new int[length][length]; // dp[start][end] = min cuts needed for str[start:end]
        for (int[] row : dp) {
            Arrays.fill(row, -1);
        }
        return minCutsRecursive(str, 0, length - 1, dp);
    }

    private int minCutsRecursive(String str, int start, int end, int[][] dp) {
        if (dp[start][end] != -1) return dp[start][end];

        // If the substring is already a palindrome, no cuts are needed
        if (isPalindrome(str, start, end)) {
            dp[start][end] = 0;
            return 0;
        }

        int minCuts = end - start; // Initially assume max cuts (cut at every character)
        for (int i = start + 1; i <= end; i++) {
            if (isPalindrome(str, start, i - 1)) {
                minCuts = Math.min(minCuts, 1 + minCutsRecursive(str, i, end, dp));
            }
        }

        dp[start][end] = minCuts;
        return minCuts;
    }


    /**
     * Optimized Dynamic Programming Approach (Bottom-Up)
     * Strategy:
     * - Precompute all palindromic substrings using a DP table.
     * - Let dp[i] be the min cuts needed for str[0..i].
     * - For every end index, find all valid start indices that form palindromes.
     * - Update dp[end] based on previous valid partitions.
     *
     * Time: O(N^2) because we fill a 2D table for palindromes and iterate through it.
     * Space: O(N^2) because of the palindrome table.
     */
    public int minCutDP(String str) {
        int length = str.length();
        boolean[][] isPalindrome = new boolean[length][length]; // isPalindrome[i][j] = true if str[i:j] is a palindrome
        int[] minCutsRequired = new int[length]; // minCutsRequired[i] = min cuts needed for str[0..i]

        // Precompute palindrome substrings
        for (int end = 0; end < length; end++) {
            for (int start = 0; start <= end; start++) {
                if (str.charAt(start) == str.charAt(end) && (end - start <= 2 || isPalindrome[start + 1][end - 1])) {
                    isPalindrome[start][end] = true;
                }
            }
        }

        // Fill DP table
        for (int right = 0; right < length; right++) {
            if (isPalindrome[0][right]) {
                minCutsRequired[right] = 0; // No cut needed if whole substring is a palindrome
            } else {
                minCutsRequired[right] = right; // Max possible cuts (cut at every character)
                for (int left = 1; left <= right; left++) {
                    if (isPalindrome[left][right]) {
                        int cutsBeforeLeft = minCutsRequired[left - 1];
                        minCutsRequired[right] = Math.min(
                            minCutsRequired[right], // if we don't cut at left
                            1 + cutsBeforeLeft // if we cut at left, adding 1 cut
                        );
                    }
                }
            }
        }

        return minCutsRequired[length - 1];
    }

    /**
     * Checks if a given substring `s[left:right]` is a palindrome.
     */
    private boolean isPalindrome(String s, int left, int right) {
        while (left < right) {
            if (s.charAt(left) != s.charAt(right)) return false;
            left++;
            right--;
        }
        return true;
    }
}
