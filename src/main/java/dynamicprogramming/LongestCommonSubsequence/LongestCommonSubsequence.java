package dynamicprogramming.longestcommonsubsequence;

import java.util.Arrays;


/**
 * Problem: Longest Common Subsequence
 *
 * Given two strings, return the length of the longest sequence that appears in
 * both strings in the same relative order. Characters do not need to be
 * contiguous, but their order must be preserved in both strings.
 *
 * Leetcode: https://leetcode.com/problems/longest-common-subsequence/ (Medium)
 * Rating:   acceptance 59.4% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Dynamic Programming | 2D table | Prefix matching
 *
 * Example:
 *   Input:  text1 = "abcde", text2 = "ace"
 *   Output: 3
 *   Why:    "ace" appears in both strings in order, and no longer common
 *           subsequence exists.
 *
 * Follow-ups:
 *   1. Return the actual subsequence, not just its length?
 *      Backtrack through the filled dp table from dp[m][n].
 *   2. Can space be reduced to O(min(m, n))?
 *      Keep only the previous and current rows when only the length is needed.
 *   3. How would you handle three strings?
 *      Use a 3D DP table over the three prefix lengths.
 *
 * Related: Shortest Common Supersequence (1092), Delete Operation for Two Strings (583).
 */
public class LongestCommonSubsequence {

  public static void main(String[] args) {
    String[][] cases = { {"abcde", "ace"}, {"abc", "def"} };
    int[] expected = { 3, 0 };

    for (int i = 0; i < cases.length; i++) {
      int got = findLcsRecursive(cases[i][0], cases[i][1]);
      System.out.printf("texts=%s -> %d  expected=%d%n",
          Arrays.toString(cases[i]), got, expected[i]);
    }
  }

    /**
   * Intuition: dp[i][j] caches the LCS length for the suffixes starting at
   * text1[i] and text2[j]. If the two current characters match, that character
   * must contribute one to the answer and both indices advance. Otherwise, the
   * best subsequence must skip either text1[i] or text2[j], so the recurrence
   * keeps the larger of those two choices.
   *
   * Algorithm:
   *   1. Allocate a memo table for every pair of indices and fill it with -1.
   *   2. Recursively solve from indices (0, 0), stopping when either string ends.
   *   3. On a match, take 1 plus the diagonal subproblem; otherwise take the max of the two skip choices.
   *
   * Time:  O(m * n) - each pair of indices is computed at most once.
   * Space: O(m * n) - the memo table stores one value per index pair.
   *
   * @param text1 first input string
   * @param text2 second input string
   * @return length of the longest common subsequence
   */
  public static int findLcsRecursive(String text1, String text2) {
    // Initialize memoization table with -1 (meaning un calculated)
    int[][] dp = new int[text1.length()][text2.length()];
    for (int[] row : dp) {
      Arrays.fill(row, -1);
    }

    return findLcsHelper(text1, text2, 0, 0, dp);
  }

  private static int findLcsHelper(String text1, String text2, int i, int j, int[][] dp) {
    // Base Case: If any string is exhausted
    if (i == text1.length() || j == text2.length()) {
      return 0;
    }

    // If already computed, return stored value
    if (dp[i][j] != -1) {
      return dp[i][j];
    }

    // If characters match,  daddamove diagonally
    if (text1.charAt(i) == text2.charAt(j)) {
      dp[i][j] = 1 + findLcsHelper(text1, text2, i + 1, j + 1, dp);
    } else {
      // Else, try skipping one character either from text1 or text2
      dp[i][j] = Math.max(
          findLcsHelper(text1, text2, i + 1, j, dp),
          findLcsHelper(text1, text2, i, j + 1, dp)
      );
    }
    return dp[i][j];
  }

  /**
   * Iterative Approach (Bottom-Up DP):
   *
   * Intuition:
   * - Build a DP table where dp[i][j] represents the length of LCS of text1[0..i-1] and text2[0..j-1].
   * - If characters match, increment the diagonal value.
   * - If they don't match, take the maximum from left or top cell.
   *
   * Time Complexity: O(M * N)
   * Space Complexity: O(M * N)
   *
   * @param text1
   * @param text2
   * @return
   */
  public static int findLcsIterative(String text1, String text2) {
    int length1 = text1.length();
    int length2 = text2.length();

    // Create a DP table
    int[][] dp = new int[length1 + 1][length2 + 1];

    // Build the table bottom-up
    for (int i = 1; i <= length1; i++) {
      for (int j = 1; j <= length2; j++) {
        // If characters match, 1 + diagonal
        if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
          dp[i][j] = 1 + dp[i - 1][j - 1]; // check if the characters match in previous indices of both strings
        } else {
          // check if the character matches in previous indices of either string, take the max
          dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
        }
      }
    }
    printLongestCommonSubsequence(dp, text1, text2);

    return dp[length1][length2];
  }

  /**
   * Print the longest common subsequence from the dp table
   */
  private static void printLongestCommonSubsequence(int[][] dp, String text1, String text2) {
    // Backtrack from dp[m][n]
    StringBuilder sb = new StringBuilder();
    int i = text1.length();
    int j = text2.length();
    // Traverse the dp table to find the LCS
    while (i > 0 && j > 0) {
      if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
        // If characters match, add to result
        sb.append(text1.charAt(i - 1));
        i--;
        j--;
      } else if (dp[i - 1][j] > dp[i][j - 1]) {
        // Move in the direction of the larger value because it indicates a longer subsequence.
        // This is how we created the dp table to find the longest common subsequence.
        i--;
      } else {
        j--;
      }
    }

    System.out.println(sb.reverse());
  }
}
