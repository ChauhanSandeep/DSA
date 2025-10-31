package strings.twopointers;

/**
 * LeetCode Problem: https://leetcode.com/problems/minimum-insertion-steps-to-make-a-string-palindrome/
 *
 * Problem Statement:
 * Given a string `s`, find the **minimum number of insertions** needed to make the string a **palindrome**.
 * The insertions can be made at any position in the string.
 *
 * Example:
 * Input: "geeks"
 * Output: 3
 * Explanation: Insert 'e', 'e', and 'g' to make "geekskeeg"
 *
 * Follow-up Questions:
 * 1. Can you return the actual palindrome instead of just the count?
 *    - Yes, by reconstructing the result during DP table filling.
 * 2. Can this be solved using LCS (Longest Common Subsequence)?
 *    - Yes. The minimum insertions = len(s) - LCS(s, reverse(s))
 *      Leetcode: https://leetcode.com/problems/longest-palindromic-subsequence/
 * 3. Can space be optimized from O(n²) to O(n)?
 *    - Yes, with rolling 1D arrays since we only need `dp[i+1][j-1]`, `dp[i+1][j]`, and `dp[i][j-1]`
 */

public class MinInsertionPalindrome {

  public static void main(String[] args) {
    String input = "geeks";

    System.out.println("Min Insertions (Recursive): " + minInsertionsRecursive(input));
    System.out.println("Min Insertions (DP): " + minInsertionsDP(input));
  }

  /**
   * Recursive approach to calculate minimum insertions needed to make a string palindrome.
   * Inutition behind this approach:
   * - A palindrome reads the same forwards and backwards.
   * - To convert a string into a palindrome, we can insert characters at either end.
   * - The goal is to minimize the number of insertions required.
   *
   * Steps:
   * - Compare characters from both ends.
   * - If they match, recurse inward.
   * - If they don’t, insert at either end and recurse. Return the minimum.
   *
   * Time Complexity: O(2^n) — exponential due to overlapping subproblems
   * Space Complexity: O(n) — recursion depth
   */
  public static int minInsertionsRecursive(String input) {
    return recursiveHelper(input, 0, input.length() - 1);
  }

  private static int recursiveHelper(String input, int left, int right) {
    // Base case: string is already a palindrome
      if (left >= right) {
          return 0;
      }

    if (input.charAt(left) == input.charAt(right)) {
      return recursiveHelper(input, left + 1, right - 1);
    }

    int insertLeft = recursiveHelper(input, left + 1, right);
    int insertRight = recursiveHelper(input, left, right - 1);

    return Math.min(insertLeft, insertRight) + 1;
  }

  /**
   * Optimized Dynamic Programming approach to calculate minimum insertions needed.
   *
   * Steps:
   * - Define dp[i][j] = min insertions to make input[i...j] a palindrome.
   * - If input[i] == input[j], no insertions needed: dp[i][j] = dp[i+1][j-1]
   * - Else, insert at i or j: dp[i][j] = min(dp[i+1][j], dp[i][j-1]) + 1
   * - Fill dp table bottom-up by increasing substring length.
   *
   * Time Complexity: O(n²)
   * Space Complexity: O(n²)
   */
  public static int minInsertionsDP(String input) {
    int length = input.length();
    int[][] dp = new int[length][length];

    // Bottom-up: fill diagonally by increasing substring length
    for (int substringLength = 2; substringLength <= length; substringLength++) {
      for (int left = 0; left <= length - substringLength; left++) {
        int right = left + substringLength - 1;

        if (input.charAt(left) == input.charAt(right)) {
          dp[left][right] = dp[left + 1][right - 1]; // min insertions required in the inner substring
        } else {
          dp[left][right] = Math.min(
              dp[left + 1][right],
              dp[left][right - 1]
          ) + 1;
        }
      }
    }

    return dp[0][length - 1]; // Min insertions to convert entire string to palindrome
  }
}
